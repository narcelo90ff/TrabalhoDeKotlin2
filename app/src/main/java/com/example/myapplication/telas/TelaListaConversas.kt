package com.example.myapplication.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.componentes.FotoOuInicial
import com.example.myapplication.dados.AppDados
import com.example.myapplication.modelo.Conversa
import com.example.myapplication.navegacao.Rotas

private val AmareloChat = Color(0xFFF2B600)

@Composable
fun TelaListaConversas(navController: NavController) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
    ) {
        // título + botão de nova conversa
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Chat", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1A1A1A))
                    .clickable { mostrarDialogo = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Nova conversa", tint = Color.White)
            }
        }

        Text(
            text = "Novos matches",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        // linha de matches: o primeiro cartão é o resumo das curtidas
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .width(96.dp)
                        .clickable {
                            navController.navigate(Rotas.CURTIDAS) { launchSingleTop = true }
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp, 128.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color(0xFF1A1A1A))
                            .border(4.dp, AmareloChat, RoundedCornerShape(22.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = null,
                                tint = AmareloChat,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = AppDados.curtidas.size.toString(),
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Curtidas",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
            items(AppDados.curtidas, key = { it.id }) { pessoa ->
                Column(
                    modifier = Modifier
                        .width(96.dp)
                        .clickable { navController.navigate(Rotas.detalheCurtida(pessoa.id)) }
                ) {
                    FotoOuInicial(
                        foto = pessoa.foto,
                        nome = pessoa.nome,
                        modifier = Modifier.size(96.dp, 128.dp),
                        forma = RoundedCornerShape(22.dp),
                        tamanhoLetra = 36.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = pessoa.nome,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // painel "Mensagens" com cantos de cima arredondados
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp))
                .background(Color(0xFF121212))
        ) {
            Text(
                text = "Mensagens",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 8.dp)
            )

            if (AppDados.conversas.isEmpty()) {
                Text(
                    text = "Nenhuma conversa ainda. Toque no + para começar.",
                    color = Color(0xFFB5B5B5),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(20.dp)
                )
            }

            // lista de conversas, de verdade reativa
            LazyColumn {
                items(AppDados.conversas, key = { it.id }) { conversa ->
                    LinhaConversa(
                        conversa = conversa,
                        aoClicar = { navController.navigate(Rotas.detalheConversa(conversa.id)) },
                        aoRemover = { AppDados.removerConversa(conversa) }
                    )
                }
            }
        }
    }

    if (mostrarDialogo) {
        DialogNovaConversa(
            aoFechar = { mostrarDialogo = false },
            aoIniciar = { nome ->
                AppDados.adicionarConversa(nome = nome)
                mostrarDialogo = false
            }
        )
    }
}

@Composable
private fun LinhaConversa(
    conversa: Conversa,
    aoClicar: () -> Unit,
    aoRemover: () -> Unit
) {
    // combina dado das duas listas: a Pessoa ligada à conversa dá a foto
    val pessoa = conversa.pessoaId?.let { AppDados.buscarPessoa(it) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = aoClicar)
                .padding(start = 20.dp, end = 8.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FotoOuInicial(
                foto = pessoa?.foto,
                nome = conversa.nome,
                modifier = Modifier.size(68.dp),
                forma = CircleShape
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = conversa.nome,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = conversa.mensagens.lastOrNull() ?: "Nenhuma mensagem ainda",
                    color = Color(0xFFB5B5B5),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = aoRemover) {
                Icon(Icons.Filled.Delete, contentDescription = "Remover conversa", tint = Color(0xFF8A8A8A))
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 104.dp)
                .height(1.dp)
                .background(Color(0xFF2E2E2E))
        )
    }
}

@Composable
private fun DialogNovaConversa(aoFechar: () -> Unit, aoIniciar: (String) -> Unit) {
    var nome by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = aoFechar,
        title = { Text("Nova conversa") },
        text = {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nova conversa com...") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { if (nome.isNotBlank()) aoIniciar(nome) }) {
                Text("Iniciar")
            }
        },
        dismissButton = {
            TextButton(onClick = aoFechar) { Text("Cancelar") }
        }
    )
}