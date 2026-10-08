package com.example.myapplication.telas

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.myapplication.dados.AppDados
import com.example.myapplication.modelo.Perfil
import com.example.myapplication.navegacao.Rotas


private data class CategoriaExplorar(
    val nome: String,
    val emoji: String,
    val tipos: List<String>
)

private val categorias = listOf(
    CategoriaExplorar("Relacionamento sério", "🌹", listOf("Relacionamento sério")),
    CategoriaExplorar("Algo casual", "🍭", listOf("Algo casual")),
    CategoriaExplorar("Ainda estou decidindo", "🤔", listOf("Ainda estou decidindo")),
    CategoriaExplorar("Compromisso sério", "💍", listOf("Relacionamento sério")),
    CategoriaExplorar("Planos pra hoje", "🌙", listOf("Algo casual", "Ainda estou decidindo"))
)

fun perfisDaCategoria(nomeCategoria: String): List<Perfil> {
    val tipos = categorias.firstOrNull { it.nome == nomeCategoria }?.tipos ?: return emptyList()
    return AppDados.perfis.filter { it.tipoRelacionamento in tipos }
}

@Composable
fun TelaExplorar(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Explorar", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { navController.navigate(Rotas.DOUBLE_DATE) }) {
                Icon(Icons.Filled.Style, contentDescription = "Double Date", tint = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Conheça pessoas que têm a mesma intenção que você",
            color = Color(0xFFB5B5B5),
            fontSize = 17.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 110.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(categorias) { categoria ->
                CardCategoria(
                    categoria = categoria,
                    quantidade = perfisDaCategoria(categoria.nome).size,
                    aoClicar = {
                        navController.navigate(Rotas.explorarDetalhe(Uri.encode(categoria.nome)))
                    }
                )
            }
        }
    }
}

@Composable
private fun CardCategoria(categoria: CategoriaExplorar, quantidade: Int, aoClicar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.88f)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF1A1A1A))
            .clickable(onClick = aoClicar)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = categoria.emoji, fontSize = 64.sp)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = categoria.nome,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier.weight(1f)
            )
            Text(text = quantidade.toString(), color = Color(0xFFB5B5B5), fontSize = 16.sp)
        }
    }
}