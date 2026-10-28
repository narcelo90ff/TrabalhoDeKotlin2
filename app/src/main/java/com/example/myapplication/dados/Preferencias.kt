package com.example.myapplication.dados

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import com.example.myapplication.data.local.LocalEntity


object Preferencias {
    var minimoFotos by mutableStateOf(1f)
    var comBio by mutableStateOf(false)

    var distanciaMaxima by mutableStateOf(35f)
    var mostrarMaisLonge by mutableStateOf(false)
    var interesseEm by mutableStateOf("Mulheres")
    var faixaEtaria by mutableStateOf(18f..28f)
    var mostrarForaDaFaixa by mutableStateOf(true)

    val locais = mutableStateListOf("São Paulo, SP")
    var localAtual by mutableStateOf("São Paulo, SP")

    var mostrarMeuPerfilNoAmigo by mutableStateOf(true)
    var mostrarAmigosNoMeuPerfil by mutableStateOf(true)
    var mostrarPerfisDoubleDate by mutableStateOf(true)

    var perfilVerificado by mutableStateOf(false)
    var chatSomenteVerificados by mutableStateOf(false)
    val locaisSalvos = mutableStateListOf<LocalEntity>()

    fun proximoInteresse() {
        val opcoes = listOf("Mulheres", "Homens", "Todos")
        interesseEm = opcoes[(opcoes.indexOf(interesseEm) + 1) % opcoes.size]
    }
}