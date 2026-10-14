package com.example.myapplication.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.dados.Preferencias

@Composable
fun TelaDoubleDateConfig(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E1E1E))
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Fechar", tint = Color.White)
            }
            Text("Double Date", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        LinhaConfig(
            titulo = "Mostrar-me no perfil do meu amigo",
            descricao = "Seu nome e foto podem aparecer no perfil do seu amigo do Double Date.",
            ligado = Preferencias.mostrarMeuPerfilNoAmigo,
            aoMudar = { Preferencias.mostrarMeuPerfilNoAmigo = it }
        )
        LinhaConfig(
            titulo = "Mostrar amigos no meu perfil",
            descricao = "O nome e foto do seu amigo do Double Date podem aparecer no seu perfil.",
            ligado = Preferencias.mostrarAmigosNoMeuPerfil,
            aoMudar = { Preferencias.mostrarAmigosNoMeuPerfil = it }
        )
        LinhaConfig(
            titulo = "Mostrar perfis do Double Date",
            descricao = "Quando essa opção estiver desativada, você não verá os perfis do Double Date no modo Pra você.",
            ligado = Preferencias.mostrarPerfisDoubleDate,
            aoMudar = { Preferencias.mostrarPerfisDoubleDate = it }
        )
    }
}

@Composable
private fun LinhaConfig(
    titulo: String,
    descricao: String,
    ligado: Boolean,
    aoMudar: (Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 18.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF1E1E1E))
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = titulo,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f).padding(end = 12.dp)
            )
            Switch(
                checked = ligado,
                onCheckedChange = aoMudar,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFFEA1D25),
                    uncheckedThumbColor = Color(0xFFB5B5B5),
                    uncheckedTrackColor = Color(0xFF3A3A3A),
                    uncheckedBorderColor = Color(0xFF3A3A3A)
                )
            )
        }
        Text(
            text = descricao,
            color = Color(0xFFB5B5B5),
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 6.dp, top = 8.dp, end = 6.dp)
        )
    }
}