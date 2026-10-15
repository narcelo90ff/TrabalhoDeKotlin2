package com.example.myapplication.dados.banco

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.myapplication.modelo.Conversa

@Dao
interface ConversaDao {

    @Insert
    suspend fun inserir(conversa: Conversa): Long

    @Query("SELECT * FROM conversas ORDER BY id")
    suspend fun buscarTodas(): List<Conversa>

    /** Consulta filtrada por campo: usada pela tela de Detalhes da conversa. */
    @Query("SELECT * FROM conversas WHERE id = :id")
    suspend fun buscarPorId(id: Int): Conversa?

    @Query("SELECT * FROM conversas WHERE pessoaId = :pessoaId LIMIT 1")
    suspend fun buscarPorPessoaId(pessoaId: Int): Conversa?

    @Query("SELECT COUNT(*) FROM conversas")
    suspend fun contar(): Int

    @Update
    suspend fun atualizar(conversa: Conversa)

    @Delete
    suspend fun deletar(conversa: Conversa)
}