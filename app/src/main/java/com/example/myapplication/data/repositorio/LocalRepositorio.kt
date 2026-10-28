package com.example.myapplication.data.repository

import android.content.Context
import android.util.Log
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.LocalDao
import com.example.myapplication.data.local.LocalEntity
import com.example.myapplication.dados.Preferencias
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext


class LocalRepositorio private constructor(private val dao: LocalDao) {


    fun observarLocais(): Flow<List<LocalEntity>> =
        dao.observar()
            .onEach { sincronizarPreferencias(it) }
            .catch { e ->
                Log.e(TAG, "Erro ao observar locais", e)
                emit(emptyList())
            }

    suspend fun carregar() {
        io(Unit) {
            if (dao.contar() == 0) {
                dao.inserir(LocalEntity(nome = "São Paulo, SP", atual = true))
            }
        }
        io<List<LocalEntity>?>(null) { dao.listar() }?.let { sincronizarPreferencias(it) }
    }

    suspend fun adicionar(nome: String) {
        val limpo = nome.trim()
        if (limpo.isEmpty()) return
        io(Unit) {
            val existente = dao.listar().firstOrNull { it.nome.equals(limpo, ignoreCase = true) }
            val id = existente?.id ?: dao.inserir(LocalEntity(nome = limpo))
            dao.definirAtual(id)
        }
    }

    suspend fun renomear(id: Long, novoNome: String): Boolean {
        val limpo = novoNome.trim()
        if (limpo.isEmpty()) return false
        return io(false) {
            val repetido = dao.listar().any { it.id != id && it.nome.equals(limpo, ignoreCase = true) }
            if (repetido) {
                false
            } else {
                dao.renomear(id, limpo)
                true
            }
        }
    }

    suspend fun escolher(id: Long) {
        io(Unit) { dao.definirAtual(id) }
    }

    suspend fun remover(id: Long) {
        io(Unit) { dao.excluir(id) }
    }

    private fun sincronizarPreferencias(todos: List<LocalEntity>) {
        Preferencias.locaisSalvos.clear()
        Preferencias.locaisSalvos.addAll(todos)
        Preferencias.locais.clear()
        Preferencias.locais.addAll(todos.map { it.nome })
        todos.firstOrNull { it.atual }?.let { Preferencias.localAtual = it.nome }
    }

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

    companion object {
        private const val TAG = "LocalRepositorio"

        fun obter(contexto: Context): LocalRepositorio =
            LocalRepositorio(AppDatabase.getDatabase(contexto).localDao())
    }
}