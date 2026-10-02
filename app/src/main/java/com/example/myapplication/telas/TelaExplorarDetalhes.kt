@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/**
 * Tela de perfis filtrados por categoria (Explorar -> categoria -> aqui).
 * Reaproveita o mesmo estilo de card de "Descobrir", só que com um topo
 * mostrando a categoria escolhida — igual ao fluxo real do app.
 */
@Composable
fun TelaExplorarDetalhe(navController: NavController, categoria: String) {
    // contador de "super likes" dado nessa sessão, só pra mostrar estado de verdade
    var contador by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoria, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.Close, contentDescription = "Fechar")
                    }
                },
                actions = {
                    Icon(
                        Icons.Filled.Bolt,
                        contentDescription = "Boost",
                        tint = Color(0xFFFF3D9A),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Icon(Icons.Filled.MoreVert, contentDescription = "Mais opções")
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Card do perfil (dados fictícios/placeholder)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF2A2A2A))
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF1E1E1E))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("Perfil novo", color = Color.White, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Talita 22",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color.White)
                        Text("a 16 km de distância", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BotaoCircular(Icons.Filled.Replay, Color.Gray) { }
                BotaoCircular(Icons.Filled.Close, Color.White) { }
                BotaoComContador(contador) { contador++ }
                BotaoCircular(Icons.Filled.Favorite, Color.Red) { }
                BotaoCircular(Icons.Filled.Send, Color(0xFF5AB2FF)) { }
            }
        }
    }
}

@Composable
private fun BotaoCircular(
    icone: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    aoClicar: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(Color(0xFF1C1C1E)),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = aoClicar) {
            Icon(icone, contentDescription = null, tint = tint)
        }
    }
}

/** O botão azul com número, igual ao da tela real (contador de super likes etc). */
@Composable
private fun BotaoComContador(contador: Int, aoClicar: () -> Unit) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(Color(0xFF1C1C1E)),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = aoClicar) {
            Text(text = contador.toString(), color = Color(0xFF5AB2FF), fontWeight = FontWeight.Bold)
        }
    }
}