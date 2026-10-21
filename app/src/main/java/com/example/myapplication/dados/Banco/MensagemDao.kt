package com.example.myapplication.dados.banco

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.myapplication.modelo.Mensagem

@Dao
interface MensagemDao {

    @Insert
    suspend fun inserir(mensagem: Mensagem): Long

    @Query("SELECT * FROM mensagens WHERE conversaId = :conversaId ORDER BY id")
    suspend fun buscarPorConversa(conversaId: Int): List<Mensagem>

    @Query("SELECT * FROM mensagens WHERE conversaId = :conversaId ORDER BY id DESC LIMIT 1")
    suspend fun ultimaDaConversa(conversaId: Int): Mensagem?

    @Query("DELETE FROM mensagens WHERE conversaId = :conversaId")
    suspend fun excluirDaConversa(conversaId: Int)
}