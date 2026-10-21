package com.example.myapplication.dados.banco

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.myapplication.modelo.Pessoa

@Dao
interface PessoaDao {

    @Insert
    suspend fun inserir(pessoa: Pessoa): Long

    @Query("SELECT * FROM pessoas ORDER BY id")
    suspend fun buscarTodas(): List<Pessoa>

    @Query("SELECT * FROM pessoas WHERE id = :id")
    suspend fun buscarPorId(id: Int): Pessoa?

    @Query("SELECT * FROM pessoas WHERE nome = :nome COLLATE NOCASE AND idade = :idade LIMIT 1")
    suspend fun buscarPorNomeEIdade(nome: String, idade: Int): Pessoa?

    @Query("SELECT COUNT(*) FROM pessoas")
    suspend fun contar(): Int

    @Update
    suspend fun atualizar(pessoa: Pessoa)

    @Delete
    suspend fun deletar(pessoa: Pessoa)
}