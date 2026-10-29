package com.example.myapplication.data.local

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Upsert

@Dao
abstract class LojaDao {

    @Insert
    abstract suspend fun inserirCompra(compra: CompraEntity): Long

    @Query("SELECT * FROM compras WHERE id = :id")
    abstract suspend fun buscarCompra(id: Long): CompraEntity?

    @Query("SELECT * FROM compras ORDER BY criadoEm DESC")
    abstract suspend fun listarCompras(): List<CompraEntity>

    @Query("SELECT * FROM carteira WHERE id = 1")
    abstract suspend fun buscarCarteira(): CarteiraEntity?

    @Upsert
    abstract suspend fun salvarCarteira(carteira: CarteiraEntity)

    @Query("UPDATE compras SET status = :novo, pagoEm = :pagoEm WHERE id = :id AND status = :pendente")
    abstract suspend fun mudarStatusSePendente(id: Long, pendente: String, novo: String, pagoEm: Long?): Int

    @Transaction
    open suspend fun confirmarPagamento(id: Long): CarteiraEntity? {
        val compra = buscarCompra(id) ?: return null
        val atual = buscarCarteira() ?: CarteiraEntity()

        val mudou = mudarStatusSePendente(id, StatusCompra.PENDENTE, StatusCompra.PAGO, System.currentTimeMillis())
        if (mudou == 0) return atual

        val nova = when (compra.tipo) {
            TipoCompra.BOOST -> atual.copy(boosts = atual.boosts + compra.quantidade)
            TipoCompra.SUPERLIKE -> atual.copy(superLikes = atual.superLikes + compra.quantidade)
            else -> atual.copy(plano = compra.plano)
        }
        salvarCarteira(nova)
        return nova
    }

    @Transaction
    open suspend fun cancelarSePendente(id: Long) {
        mudarStatusSePendente(id, StatusCompra.PENDENTE, StatusCompra.CANCELADO, null)
    }
}