package com.example.myapplication.dados

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myapplication.data.local.CarteiraEntity

object Carteira {
    var superLikes by mutableIntStateOf(0)
    var boosts by mutableIntStateOf(0)

    var plano by mutableStateOf<String?>(null)

    fun aplicar(salva: CarteiraEntity) {
        superLikes = salva.superLikes
        boosts = salva.boosts
        plano = salva.plano
    }
}