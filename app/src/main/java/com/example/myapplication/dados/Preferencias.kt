package com.example.myapplication.dados

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Preferências de descoberta (filtros), em memória, igual ao AppDados.
 * A tela de Filtros e a tela de Configurações leem e escrevem aqui,
 * então as duas ficam sempre sincronizadas.
 */
object Preferencias {
    var minimoFotos by mutableStateOf(1f)
    var comBio by mutableStateOf(false)

    var distanciaMaxima by mutableStateOf(35f)
    var mostrarMaisLonge by mutableStateOf(false)
    var interesseEm by mutableStateOf("Mulheres")
    var faixaEtaria by mutableStateOf(18f..28f)
    var mostrarForaDaFaixa by mutableStateOf(true)

    /** Alterna Mulheres -> Homens -> Todos -> Mulheres... */
    fun proximoInteresse() {
        val opcoes = listOf("Mulheres", "Homens", "Todos")
        interesseEm = opcoes[(opcoes.indexOf(interesseEm) + 1) % opcoes.size]
    }
}