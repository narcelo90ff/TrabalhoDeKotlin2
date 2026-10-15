package com.example.myapplication.dados.banco

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Upsert
import com.example.myapplication.dados.banco.CarteiraEntity
import com.example.myapplication.dados.banco.CompraEntity
import com.example.myapplication.dados.banco.StatusCompra
import com.example.myapplication.dados.banco.TipoCompra

@Dao
abstract class LojaDao {

    @Insert
    abstract suspend fun inserirCompra(compra: CompraEntity): Long

    @Query("SELECT * FROM compras WHERE id = :id")
    abstract suspend fun buscarCompra(id: Long): CompraEntity?

    @Query("SELECT * FROM compras ORDER BY criadoEm DESC")
    abstract suspend fun listarCompras(): List<CompraEntity>

    @Query("UPDATE compras SET status = :status, pagoEm = :pagoEm WHERE id = :id")
    abstract suspend fun atualizarStatus(id: Long, status: String, pagoEm: Long?)

    @Query("SELECT * FROM carteira WHERE id = 1")
    abstract suspend fun buscarCarteira(): CarteiraEntity?

    @Upsert
    abstract suspend fun salvarCarteira(carteira: CarteiraEntity)

    /**
     * Confirma o pagamento e credita o item na carteira, tudo ou nada
     * (se algo falhar no meio, nada é gravado). Se a compra já não estiver
     * pendente, não credita de novo.
     */
    @Transaction
    open suspend fun confirmarPagamento(id: Long): CarteiraEntity? {
        val compra = buscarCompra(id) ?: return null
        val atual = buscarCarteira() ?: CarteiraEntity()
        if (compra.status != StatusCompra.PENDENTE) return atual

        atualizarStatus(id, StatusCompra.PAGO, System.currentTimeMillis())
        val nova = when (compra.tipo) {
            TipoCompra.BOOST -> atual.copy(boosts = atual.boosts + compra.quantidade)
            TipoCompra.SUPERLIKE -> atual.copy(superLikes = atual.superLikes + compra.quantidade)
            else -> atual.copy(plano = compra.plano)
        }
        salvarCarteira(nova)
        return nova
    }

    /** Cancela só se ainda estiver pendente (uma compra paga não pode ser cancelada). */
    @Transaction
    open suspend fun cancelarSePendente(id: Long) {
        val compra = buscarCompra(id) ?: return
        if (compra.status == StatusCompra.PENDENTE) {
            atualizarStatus(id, StatusCompra.CANCELADO, null)
        }
    }
}