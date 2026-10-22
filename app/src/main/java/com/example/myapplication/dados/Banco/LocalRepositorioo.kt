package com.example.myapplication.dados.banco

import android.content.Context
import android.util.Log
import com.example.myapplication.dados.Preferencias
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalRepositorio private constructor(private val dao: LocalDao) {

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

    suspend fun carregar() {
        io(Unit) {
            if (dao.contar() == 0) {
                dao.inserir(LocalEntity(nome = "São Paulo, SP", atual = true))
            }
        }
        sincronizar()
    }

    suspend fun adicionar(nome: String) {
        val limpo = nome.trim()
        if (limpo.isEmpty()) return
        io(Unit) {
            val existente = dao.listar().firstOrNull { it.nome.equals(limpo, ignoreCase = true) }
            val id = existente?.id ?: dao.inserir(LocalEntity(nome = limpo))
            dao.definirAtual(id)
        }
        sincronizar()
    }

    suspend fun renomear(id: Long, novoNome: String): Boolean {
        val limpo = novoNome.trim()
        if (limpo.isEmpty()) return false
        val renomeou = io(false) {
            val repetido = dao.listar().any { it.id != id && it.nome.equals(limpo, ignoreCase = true) }
            if (repetido) {
                false
            } else {
                dao.renomear(id, limpo)
                true
            }
        }
        if (renomeou) sincronizar()
        return renomeou
    }

    suspend fun escolher(id: Long) {
        io(Unit) { dao.definirAtual(id) }
        sincronizar()
    }

    suspend fun remover(id: Long) {
        io(Unit) { dao.excluir(id) }
        sincronizar()
    }

    private suspend fun sincronizar() {
        val todos = io<List<LocalEntity>?>(null) { dao.listar() } ?: return
        Preferencias.locaisSalvos.clear()
        Preferencias.locaisSalvos.addAll(todos)
        Preferencias.locais.clear()
        Preferencias.locais.addAll(todos.map { it.nome })
        todos.firstOrNull { it.atual }?.let { Preferencias.localAtual = it.nome }
    }

    companion object {
        private const val TAG = "LocalRepositorio"

        fun obter(contexto: Context): LocalRepositorio =
            LocalRepositorio(BancoDeDados.obter(contexto).localDao())
    }
}