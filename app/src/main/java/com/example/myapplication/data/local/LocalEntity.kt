package com.example.myapplication.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "locais")
data class LocalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val atual: Boolean = false
)