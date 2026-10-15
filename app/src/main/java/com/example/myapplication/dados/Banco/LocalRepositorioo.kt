package com.example.myapplication.dados.banco

import android.content.Context
import com.example.myapplication.dados.Preferencias

class LocalRepositorio private constructor(private val dao: LocalDao) {

    suspend fun carregar() {
        if (dao.contar() == 0) {
            dao.inserir(LocalEntity(nome = "São Paulo, SP", atual = true))
        }
        sincronizar()
    }

    suspend fun adicionar(nome: String) {
        val limpo = nome.trim()
        if (limpo.isEmpty()) return
        val existente = dao.listar().firstOrNull { it.nome.equals(limpo, ignoreCase = true) }
        val id = existente?.id ?: dao.inserir(LocalEntity(nome = limpo))
        dao.definirAtual(id)
        sincronizar()
    }

    suspend fun renomear(id: Long, novoNome: String): Boolean {
        val limpo = novoNome.trim()
        if (limpo.isEmpty()) return false
        val repetido = dao.listar().any { it.id != id && it.nome.equals(limpo, ignoreCase = true) }
        if (repetido) return false
        dao.renomear(id, limpo)
        sincronizar()
        return true
    }

    suspend fun escolher(id: Long) {
        dao.definirAtual(id)
        sincronizar()
    }

    suspend fun remover(id: Long) {
        dao.excluir(id)
        sincronizar()
    }

    private suspend fun sincronizar() {
        val todos = dao.listar()
        Preferencias.locaisSalvos.clear()
        Preferencias.locaisSalvos.addAll(todos)
        Preferencias.locais.clear()
        Preferencias.locais.addAll(todos.map { it.nome })
        todos.firstOrNull { it.atual }?.let { Preferencias.localAtual = it.nome }
    }

    companion object {
        fun obter(contexto: Context): LocalRepositorio =
            LocalRepositorio(BancoDeDados.obter(contexto).localDao())
    }
}