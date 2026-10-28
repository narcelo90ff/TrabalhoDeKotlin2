package com.example.myapplication.ui.local

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.LocalEntity
import com.example.myapplication.data.repository.LocalRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class LocalViewModel(
    private val repository: LocalRepositorio
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocalUiState())
    val uiState: StateFlow<LocalUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observarLocais().collect { locais ->
                _uiState.update { it.copy(locais = locais) }
            }
        }
    }

    fun onAbrirNovo() {
        _uiState.update { it.copy(mostrarDialogoNovo = true) }
    }

    fun onFecharNovo() {
        _uiState.update { it.copy(mostrarDialogoNovo = false) }
    }

    fun onAdicionar(nome: String) {
        viewModelScope.launch {
            repository.adicionar(nome)
            onFecharNovo()
        }
    }

    fun onEditar(local: LocalEntity) {
        _uiState.update { it.copy(localEmEdicao = local, erroEdicao = null) }
    }

    fun onCancelarEdicao() {
        _uiState.update { it.copy(localEmEdicao = null, erroEdicao = null) }
    }

    fun onSalvarEdicao(novoNome: String) {
        val local = _uiState.value.localEmEdicao ?: return
        viewModelScope.launch {
            if (repository.renomear(local.id, novoNome)) {
                onCancelarEdicao()
            } else {
                _uiState.update { it.copy(erroEdicao = "Já existe um local com esse nome.") }
            }
        }
    }

    fun onEscolher(local: LocalEntity) {
        viewModelScope.launch { repository.escolher(local.id) }
    }

    fun onRemover(local: LocalEntity) {
        viewModelScope.launch { repository.remover(local.id) }
    }
}