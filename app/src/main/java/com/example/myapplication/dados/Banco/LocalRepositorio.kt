package com.example.myapplication.dados.banco

import android.content.Context
import com.example.myapplication.dados.Carteira
import com.example.myapplication.dados.Pix
import java.util.UUID

class LojaRepositorio private constructor(private val dao: LojaDao) {

    suspend fun carregarCarteira() {
        dao.buscarCarteira()?.let { Carteira.aplicar(it) }
    }

    suspend fun criarCompra(
        tipo: String,
        item: String,
        quantidade: Int,
        plano: String?,
        valorCentavos: Long
    ): Long {
        val txid = UUID.randomUUID().toString().replace("-", "").take(25)
        val codigo = Pix.gerarCopiaECola(valorCentavos = valorCentavos, txid = txid)
        return dao.inserirCompra(
            CompraEntity(
                tipo = tipo,
                item = item,
                quantidade = quantidade,
                plano = plano,
                valorCentavos = valorCentavos,
                codigoPix = codigo
            )
        )
    }

    suspend fun buscarCompra(id: Long): CompraEntity? = dao.buscarCompra(id)

    suspend fun listarCompras(): List<CompraEntity> = dao.listarCompras()

    suspend fun confirmarPagamento(id: Long): Boolean {
        val carteira = dao.confirmarPagamento(id) ?: return false
        Carteira.aplicar(carteira)
        return true
    }

    suspend fun cancelar(id: Long) = dao.cancelarSePendente(id)

    companion object {
        fun obter(contexto: Context): LojaRepositorio =
            LojaRepositorio(BancoDeDados.obter(contexto).lojaDao())
    }
}