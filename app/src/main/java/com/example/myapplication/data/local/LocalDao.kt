package com.example.myapplication.data.local

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow


@Dao
abstract class LocalDao {

    @Insert
    abstract suspend fun inserir(local: LocalEntity): Long

    @Query("SELECT * FROM locais ORDER BY id")
    abstract fun observar(): Flow<List<LocalEntity>>

    @Query("SELECT * FROM locais ORDER BY id")
    abstract suspend fun listar(): List<LocalEntity>

    @Query("SELECT COUNT(*) FROM locais")
    abstract suspend fun contar(): Int

    @Query("UPDATE locais SET nome = :nome WHERE id = :id")
    abstract suspend fun renomear(id: Long, nome: String)

    @Query("UPDATE locais SET atual = (id = :id)")
    abstract suspend fun definirAtual(id: Long)

    @Query("DELETE FROM locais WHERE id = :id AND atual = 0")
    abstract suspend fun excluir(id: Long)
}