@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.myapplication.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TelaConfiguracoes(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Configurações") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            CardDestaque(
                titulo = "TINDER PLUS",
                subtitulo = "Curtidas ilimitadas e mais!",
                corFundo = Color(0xFF2A1A1A)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CardAtalho("⭐", "Quero Super Likes", Modifier.weight(1f))
                CardAtalho("⚡", "Quero Boosts", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Configurações de conta", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // Dados fictícios (placeholder) — nunca dados reais de ninguém
            LinhaConfiguracao(rotulo = "Número de telefone", valor = "+55 41 9xxxx-xxxx")
            LinhaConfiguracao(rotulo = "Contas conectadas", valor = "")
            LinhaConfiguracao(rotulo = "E-mail", valor = "seuemail@exemplo.com")

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Verifique um e-mail para ajudar a proteger a sua conta.",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun CardDestaque(titulo: String, subtitulo: String, corFundo: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(corFundo)
            .padding(16.dp)
    ) {
        Text(titulo, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(subtitulo, color = Color.Gray, fontSize = 13.sp)
    }
}

@Composable
private fun CardAtalho(emoji: String, texto: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E1E1E))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(texto, color = Color.White, fontSize = 13.sp)
    }
}

@Composable
private fun LinhaConfiguracao(rotulo: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(rotulo, color = Color.White)
            if (valor.isNotBlank()) {
                Text(valor, color = Color.Gray, fontSize = 13.sp)
            }
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.Gray)
    }
}