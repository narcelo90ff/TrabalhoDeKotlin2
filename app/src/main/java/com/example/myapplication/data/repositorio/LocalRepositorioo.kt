package com.example.myapplication.data.repository

import android.content.Context
import android.util.Log
import com.example.myapplication.dados.Carteira
import com.example.myapplication.dados.Pix
import com.example.myapplication.data.local.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class LojaRepositorio private constructor(private val dao: LojaDao) {

    private suspend fun <T> io(padrao: T, acao: suspend () -> T): T =
        withContext(Dispatchers.IO) {
            try {
                acao()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao acessar o banco de dados", e)
                padrao
            }
        }

    suspend fun carregarCarteira() {
        io<CarteiraEntity?>(null) { dao.buscarCarteira() }?.let { Carteira.aplicar(it) }
    }

    suspend fun criarCompra(
        tipo: String,
        item: String,
        quantidade: Int,
        plano: String?,
        valorCentavos: Long
    ): Long {
        val txid = UUID.randomUUID().toString().replace("-", "").take(25)
        val codigo = Pix.gerarCopiaECola(valorCentavos, txid)
        return io(-1L) {
            dao.inserirCompra(
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
    }

    suspend fun buscarCompra(id: Long): CompraEntity? =
        io<CompraEntity?>(null) { dao.buscarCompra(id) }

    suspend fun listarCompras(): List<CompraEntity> =
        io(emptyList<CompraEntity>()) { dao.listarCompras() }

    suspend fun confirmarPagamento(id: Long): Boolean {
        val carteira = io<CarteiraEntity?>(null) { dao.confirmarPagamento(id) } ?: return false
        Carteira.aplicar(carteira)
        return true
    }

    suspend fun cancelar(id: Long) = io(Unit) { dao.cancelarSePendente(id) }

    companion object {
        private const val TAG = "LojaRepositorio"

        fun obter(contexto: Context) =
            LojaRepositorio(AppDatabase.getDatabase(contexto).lojaDao())
    }
}