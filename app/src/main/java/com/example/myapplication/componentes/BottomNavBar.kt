package com.example.myapplication.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.dados.AppDados
import com.example.myapplication.navegacao.Rotas

private data class ItemMenu(val rota: String, val titulo: String, val icone: ImageVector)

private val itensMenu = listOf(
    ItemMenu(Rotas.DESCOBRIR, "Descobrir", Icons.Filled.Whatshot),
    ItemMenu(Rotas.EXPLORAR, "Explorar", Icons.Filled.Explore),
    ItemMenu(Rotas.CURTIDAS, "Curtidas", Icons.Filled.Favorite),
    ItemMenu(Rotas.CONVERSAS, "Chat", Icons.Filled.ChatBubbleOutline),
    ItemMenu(Rotas.PERFIL, "Perfil", Icons.Filled.Person)
)

/** Só essas rotas (as abas principais) mostram a barra inferior. */
fun rotaTemBottomBar(rota: String?): Boolean = itensMenu.any { it.rota == rota }

/** Barra inferior em formato de "pílula" flutuante, com a aba atual destacada. */
@Composable
fun BottomNavBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val destinoAtual = backStackEntry?.destination
    val formaBarra = RoundedCornerShape(40.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(formaBarra)
            .background(Color(0xFF1A1A1A))
            .border(1.dp, Color(0xFF2E2E2E), formaBarra)
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        itensMenu.forEach { item ->
            val selecionado = destinoAtual?.hierarchy?.any { it.route == item.rota } == true

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(30.dp))
                    .background(if (selecionado) Color(0xFF3A3A3A) else Color.Transparent)
                    .clickable {
                        navController.navigate(item.rota) {
                            // Evita empilhar várias cópias da mesma aba
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // bolinha vermelha com a quantidade de curtidas, como no print
                val qtdCurtidas = AppDados.curtidas.size
                BadgedBox(
                    badge = {
                        if (item.rota == Rotas.CURTIDAS && qtdCurtidas > 0) {
                            Badge(containerColor = Color(0xFFD32F2F), contentColor = Color.White) {
                                Text(qtdCurtidas.toString())
                            }
                        }
                    }
                ) {
                    Icon(item.icone, contentDescription = item.titulo, tint = Color.White)
                }
                Text(
                    text = item.titulo,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 1
                )
            }
        }
    }
}