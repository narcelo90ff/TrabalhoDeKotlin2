package com.example.myapplication.modelo

import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Representa uma pessoa que curtiu você (tela de Curtidas).
 *
 * "favorito" fica fora do construtor e é guardado com mutableStateOf
 * de propósito: assim, quando o usuário marca/desmarca o favorito,
 * só esse pedacinho de estado muda e a tela recompõe sozinha, mesmo
 * sendo um item dentro de uma lista.
 */
data class Pessoa(
    val id: Int,
    val nome: String,
    val idade: Int,
    val bio: String,
    @DrawableRes val foto: Int? = null
) {
    var favorito by mutableStateOf(false)
}

/**
 * Representa uma conversa (tela de Chat). Pode estar ligada a uma
 * Pessoa (pessoaId) ou não — assim a tela de Detalhe da conversa
 * consegue combinar dados das duas listas (Conversa + Pessoa).
 */
data class Conversa(
    val id: Int,
    val pessoaId: Int?,
    val nome: String,
    val mensagens: MutableList<String>
) {
    /**
     * Posições (índices) das mensagens que EU enviei. As outras mensagens
     * da lista são da outra pessoa. Fica fora do construtor, igual ao
     * "favorito" da Pessoa, para não precisar mudar o AppDados.
     */
    val minhasMensagens = mutableStateListOf<Int>()
}

/**
 * Perfil que aparece na tela Descobrir (foto cheia + tela de detalhes).
 * Todos os dados são inventados. "pessoaId" guarda a Pessoa criada na lista
 * de Curtidas quando o usuário curte esse perfil (null = ainda não curtiu).
 */
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