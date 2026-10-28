package com.example.myapplication.ui.local

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.LocalRepositorio
class LocalViewModelFactory(
    private val repository: LocalRepositorio
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocalViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}