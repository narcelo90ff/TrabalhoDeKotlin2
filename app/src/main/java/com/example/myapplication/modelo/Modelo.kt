package com.example.myapplication.modelo

import androidx.annotation.DrawableRes
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.myapplication.R


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

val Pessoa.foto: Int? get() = Fotos.resolver(fotoNome)


@Entity(tableName = "conversas")
data class Conversa(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pessoaId: Int? = null,
    val nome: String
)

@Entity(tableName = "mensagens")
data class Mensagem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val conversaId: Int,
    val texto: String,
    val minha: Boolean
)

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