package com.example.myapplication.modelo

import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


data class Pessoa(
    val id: Int,
    val nome: String,
    val idade: Int,
    val bio: String,
    @DrawableRes val foto: Int? = null,
    val distanciaKm: Int? = null
) {
    var favorito by mutableStateOf(false)
}


data class Conversa(
    val id: Int,
    val pessoaId: Int?,
    val nome: String,
    val mensagens: MutableList<String>
) {

    val minhasMensagens = mutableStateListOf<Int>()
}


data class Perfil(
    val id: Int,
    val nome: String,
    val idade: Int,
    @DrawableRes val foto: Int,
    val distancia: String,
    val procurando: String,
    val tipoRelacionamento: String,
    val sobreMim: String,
    val altura: String,
    val escolaridade: String,
    val genero: String,
    val pergunta: String,
    val resposta: String,
    val bebida: String,
    val fuma: String,
    val atividadeFisica: String,
    val pets: String,
    val comunicacao: String,
    val signo: String,
    val interesses: List<String>
) {
    var pessoaId: Int? = null
}