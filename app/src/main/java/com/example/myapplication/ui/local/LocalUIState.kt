package com.example.myapplication.ui.local

import com.example.myapplication.data.local.LocalEntity


data class LocalUiState(
    val locais: List<LocalEntity> = emptyList(),
    val mostrarDialogoNovo: Boolean = false,
    val localEmEdicao: LocalEntity? = null,
    val erroEdicao: String? = null
)