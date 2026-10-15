package com.example.myapplication.modelo

import androidx.annotation.DrawableRes
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.myapplication.R

/**
 * Pessoa que curtiu o usuário / deu match. Era a data class do Trabalho 2 (guardada em
 * mutableStateListOf); agora é uma tabela do Room.
 *
 * A foto é guardada pelo NOME do drawable (ex.: "pessoa_1") e não pelo número R.drawable.x,
 * porque esse número pode mudar quando o app é recompilado com novos recursos.
 * Para obter o id do drawable use a propriedade [foto] (extensão logo abaixo).
 */
@Entity(tableName = "pessoas")
data class Pessoa(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val idade: Int,
    val bio: String,
    val fotoNome: String? = null,
    val distanciaKm: Int? = null,
    val favorito: Boolean = false
)

/** Id do drawable da foto (ou null se a pessoa não tem foto). */
val Pessoa.foto: Int? get() = Fotos.resolver(fotoNome)

/**
 * Uma conversa. As mensagens deixaram de ser uma MutableList<String> dentro da classe
 * (o Room não guarda listas numa coluna) e viraram a tabela [Mensagem], ligada por conversaId.
 */
@Entity(tableName = "conversas")
data class Conversa(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pessoaId: Int? = null,
    val nome: String
)

/** Uma mensagem de uma conversa. [minha] = true quando fui eu que enviei. */
@Entity(tableName = "mensagens")
data class Mensagem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val conversaId: Int,
    val texto: String,
    val minha: Boolean
)

/** Liga o nome salvo no banco ao drawable correspondente. */
object Fotos {
    private val mapa = mapOf(
        "pessoa_1" to R.drawable.pessoa_1,
        "pessoa_2" to R.drawable.pessoa_2,
        "pessoa_3" to R.drawable.pessoa_3,
        "pessoa_4" to R.drawable.pessoa_4,
        "pessoa_5" to R.drawable.pessoa_5
    )

    fun resolver(nome: String?): Int? = nome?.let { mapa[it] }

    fun nomeDe(@DrawableRes recurso: Int?): String? =
        mapa.entries.firstOrNull { it.value == recurso }?.key
}

/** Perfil do Descobrir (catálogo fixo do app, não é salvo no banco). */
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