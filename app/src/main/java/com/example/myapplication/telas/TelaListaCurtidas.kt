package com.example.myapplication.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.componentes.FotoOuInicial
import com.example.myapplication.dados.AppDados
import com.example.myapplication.modelo.Pessoa
import com.example.myapplication.navegacao.Rotas

@Composable
fun TelaListaCurtidas(navController: NavController) {
    var mostrarDialogo by remember { mutableStateOf(false) }
    var ordem by remember { mutableStateOf("Recomendado") }
    var menuAberto by remember { mutableStateOf(false) }

    // a lista muda de ordem conforme o filtro escolhido
    val lista = when (ordem) {
        "Nome" -> AppDados.curtidas.sortedBy { it.nome }
        "Idade" -> AppDados.curtidas.sortedBy { it.idade }
        else -> AppDados.curtidas.toList()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            // título + contador
            Row(
                modifier = Modifier.padding(start = 4.dp, top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Curtidas", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF1E1E1E))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = AppDados.curtidas.size.toString(),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // filtro "Recomendado" (muda a ordem da lista)
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFF1F1F1))
                        .clickable { menuAberto = true }
                        .padding(start = 20.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(ordem, color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color.Black)
                }
                DropdownMenu(expanded = menuAberto, onDismissRequest = { menuAberto = false }) {
                    listOf("Recomendado", "Nome", "Idade").forEach { opcao ->
                        DropdownMenuItem(
                            text = { Text(opcao) },
                            onClick = {
                                ordem = opcao
                                menuAberto = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Todas as curtidas",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 4.dp)
            )

            // grade de 2 colunas, de verdade reativa
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(lista, key = { it.id }) { pessoa ->
                    CartaoCurtida(
                        pessoa = pessoa,
                        aoClicar = { navController.navigate(Rotas.detalheCurtida(pessoa.id)) },
                        aoFavoritar = { pessoa.favorito = !pessoa.favorito },
                        aoRemover = { AppDados.removerPessoa(pessoa) }
                    )
                }
            }
        }

        // botão amarelo (adicionar uma curtida nova)
        Button(
            onClick = { mostrarDialogo = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF2B600),
                contentColor = Color.Black
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 12.dp)
                .height(58.dp)
        ) {
            Text("Adicionar curtida", fontSize = 19.sp, fontWeight = FontWeight.Bold)
        }
    }

    if (mostrarDialogo) {
        DialogNovaCurtida(
            aoFechar = { mostrarDialogo = false },
            aoAdicionar = { nome, idade, mensagem ->
                AppDados.adicionarPessoa(nome = nome, idade = idade, bio = mensagem)
                mostrarDialogo = false
            }
        )
    }
}

@Composable
private fun CartaoCurtida(
    pessoa: Pessoa,
    aoClicar: () -> Unit,
    aoFavoritar: () -> Unit,
    aoRemover: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.72f)
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = aoClicar) // clicar no card (fora dos ícones) abre o Detalhe
    ) {
        FotoOuInicial(
            foto = pessoa.foto,
            nome = pessoa.nome,
            modifier = Modifier.fillMaxSize(),
            tamanhoLetra = 64.sp
        )

        // sombra embaixo para o texto ler bem
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
                .align(Alignment.BottomCenter)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xE6000000))))
        )

        // favoritar e remover, pequenos no canto de cima
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            BotaoMini(
                icone = if (pessoa.favorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                cor = Color(0xFFFE3C72),
                descricao = "Favoritar",
                aoClicar = aoFavoritar
            )
            BotaoMini(
                icone = Icons.Filled.Delete,
                cor = Color.White,
                descricao = "Remover",
                aoClicar = aoRemover
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = pessoa.nome,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(pessoa.idade.toString(), color = Color.White, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3DAA5C))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (pessoa.favorito) "Favorito" else "Online recentemente",
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun BotaoMini(
    icone: androidx.compose.ui.graphics.vector.ImageVector,
    cor: Color,
    descricao: String,
    aoClicar: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(Color(0x66000000))
            .clickable(onClick = aoClicar),
        contentAlignment = Alignment.Center
    ) {
        Icon(icone, contentDescription = descricao, tint = cor, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun DialogNovaCurtida(
    aoFechar: () -> Unit,
    aoAdicionar: (String, Int, String) -> Unit
) {
    // variáveis de estado do formulário de adicionar
    var nomeNovo by remember { mutableStateOf("") }
    var idadeNova by remember { mutableStateOf("") }
    var bioNova by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = aoFechar,
        title = { Text("Adicionar curtida") },
        text = {
            Column {
                OutlinedTextField(
                    value = nomeNovo,
                    onValueChange = { nomeNovo = it },
                    label = { Text("Nome") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = idadeNova,
                    onValueChange = { novoValor -> idadeNova = novoValor.filter { it.isDigit() } },
                    label = { Text("Idade") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = bioNova,
                    onValueChange = { bioNova = it },
                    label = { Text("Mensagem") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (nomeNovo.isNotBlank()) {
                    aoAdicionar(
                        nomeNovo,
                        idadeNova.toIntOrNull() ?: 18,
                        bioNova.ifBlank { "Nova curtida!" }
                    )
                }
            }) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = aoFechar) { Text("Cancelar") }
        }
    )
}