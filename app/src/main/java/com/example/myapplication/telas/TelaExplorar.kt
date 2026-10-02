package com.example.myapplication.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import com.example.myapplication.navegacao.Rotas

/** Uma categoria do Explorar: emoji, nome e quantidade de pessoas (só ilustrativo). */
private data class CategoriaExplorar(
    val nome: String,
    val emoji: String,
    val contagem: String,
    val corFundo: Color
)

private val categorias = listOf(
    CategoriaExplorar("Relacionamento sério", "🌹", "4 mil", Color(0xFF3A1F1F)),
    CategoriaExplorar("Compromisso sério", "💍", "3 mil", Color(0xFF3A1F33)),
    CategoriaExplorar("Planos pra hoje?", "🌙", "2 mil", Color(0xFF1F2A3A)),
    CategoriaExplorar("Algo casual", "🍭", "3 mil", Color(0xFF3A1F1F))
)

@Composable
fun TelaExplorar(navController: NavController) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Explorar", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = { navController.navigate(Rotas.DOUBLE_DATE) }) {
                    Icon(Icons.Filled.Style, contentDescription = "Double Date", tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Conheça pessoas que têm a mesma intenção que você",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(categorias) { categoria ->
                    CardCategoria(
                        categoria = categoria,
                        aoClicar = {
                            // navega pra tela de detalhe passando o nome da categoria de verdade
                            navController.navigate(Rotas.explorarDetalhe(categoria.nome))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CardCategoria(categoria: CategoriaExplorar, aoClicar: () -> Unit) {
    Column(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(categoria.corFundo)
            .clickable(onClick = aoClicar)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = categoria.emoji, fontSize = 36.sp)
        Column {
            Text(
                text = categoria.nome,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(text = categoria.contagem, color = Color.Gray, fontSize = 13.sp)
        }
    }
}