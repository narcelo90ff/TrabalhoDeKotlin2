package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.dados.banco.LocalRepositorio
import com.example.myapplication.dados.banco.LojaRepositorio
import com.example.myapplication.dados.banco.SocialRepositorio
import com.example.myapplication.navegacao.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val contexto = LocalContext.current

            // Ao abrir o app, lê do banco (Room) a carteira, os locais salvos
            // e os totais de curtidas/conversas (na 1ª vez grava os dados de exemplo)
            LaunchedEffect(Unit) {
                LojaRepositorio.obter(contexto).carregarCarteira()
                LocalRepositorio.obter(contexto).carregar()
                SocialRepositorio.obter(contexto).carregarInicial()
            }

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF121212)
                ) {
                    AppNavigation()
                }
            }
        }
    }
}