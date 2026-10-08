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
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Whatshot
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.componentes.FotoOuInicial
import com.example.myapplication.dados.AppDados
import com.example.myapplication.modelo.Conversa
import com.example.myapplication.modelo.Pessoa
import com.example.myapplication.navegacao.Rotas

private val AmareloChat = Color(0xFFF2B600)
private val CorFracaChat = Color(0xFFB5B5B5)
private val CorBordaChat = Color(0xFF2E2E2E)
private val CorPilulaChat = Color(0xFF1A1A1A)



@Composable
fun TelaListaConversas(navController: NavController) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
    ) {
        CabecalhoChat(aoNovaConversa = { mostrarDialogo = true })

        Text(
            text = "Novos matches",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LinhaNovosMatches(
            aoClicarCurtidas = {
                navController.navigate(Rotas.CURTIDAS) { launchSingleTop = true }
            },
            aoClicarPessoa = { pessoa ->
                val conversa = AppDados.iniciarConversaComPessoa(pessoa)
                navController.navigate(Rotas.detalheConversa(conversa.id))
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        PainelMensagens(
            navController = navController,
            modifier = Modifier.weight(1f)
        )
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
private fun CabecalhoChat(aoNovaConversa: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Chat", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(CorPilulaChat)
                .border(1.dp, CorBordaChat, RoundedCornerShape(50))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(Icons.Filled.Shield, contentDescription = "Segurança", tint = Color.White)
            }
            IconButton(onClick = { }) {
                Icon(Icons.Filled.Link, contentDescription = "Convidar", tint = Color.White)
            }
            IconButton(onClick = aoNovaConversa) {
                Icon(Icons.Filled.Add, contentDescription = "Nova conversa", tint = Color.White)
            }
        }
    }
}


@Composable
private fun LinhaNovosMatches(
    aoClicarCurtidas: () -> Unit,
    aoClicarPessoa: (Pessoa) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { CartaoResumoCurtidas(aoClicar = aoClicarCurtidas) }
        items(AppDados.curtidas, key = { it.id }) { pessoa ->
            CartaoMatch(pessoa = pessoa, aoClicar = { aoClicarPessoa(pessoa) })
        }
    }
}

@Composable
private fun CartaoResumoCurtidas(aoClicar: () -> Unit) {
    Column(
        modifier = Modifier
            .width(82.dp)
            .clickable(onClick = aoClicar)
    ) {
        Box(
            modifier = Modifier
                .size(82.dp, 108.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFF4A4030), Color(0xFF2A2518))))
                .border(3.dp, AmareloChat, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            // bolinha amarela com a quantidade de curtidas
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(AmareloChat),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = AppDados.curtidas.size.toString(),
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Curtidas",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun CartaoMatch(pessoa: Pessoa, aoClicar: () -> Unit) {
    Column(
        modifier = Modifier
            .width(82.dp)
            .clickable(onClick = aoClicar)
    ) {
        FotoOuInicial(
            foto = pessoa.foto,
            nome = pessoa.nome,
            modifier = Modifier.size(82.dp, 108.dp),
            forma = RoundedCornerShape(20.dp),
            tamanhoLetra = 32.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = pessoa.nome,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}



@Composable
private fun PainelMensagens(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(Color(0xFF121212))
    ) {
        Text(
            text = "Mensagens",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 110.dp)
        ) {
            item {
                LinhaMensagemDoApp(aoClicar = {
                    navController.navigate(Rotas.PERFIL) { launchSingleTop = true }
                })
            }
            items(AppDados.conversas, key = { it.id }) { conversa ->
                LinhaConversa(
                    conversa = conversa,
                    aoClicar = { navController.navigate(Rotas.detalheConversa(conversa.id)) },
                    aoRemover = { AppDados.removerConversa(conversa) }
                )
            }
            if (AppDados.conversas.isEmpty()) {
                item {
                    Text(
                        text = "Nenhuma conversa ainda. Toque no + para começar.",
                        color = CorFracaChat,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LinhaMensagemDoApp(aoClicar: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = aoClicar)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD32F2F)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Whatshot,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Equipe do App", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        Icons.Filled.Verified,
                        contentDescription = "Verificado",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "🚨 Atualização de perfil obrigatória 🚨",
                    color = CorFracaChat,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        DivisorLinha()
    }
}

@Composable
private fun LinhaConversa(
    conversa: Conversa,
    aoClicar: () -> Unit,
    aoRemover: () -> Unit
) {
    val pessoa = conversa.pessoaId?.let { AppDados.buscarPessoa(it) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = aoClicar)
                .padding(start = 16.dp, end = 4.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FotoOuInicial(
                foto = pessoa?.foto,
                nome = conversa.nome,
                modifier = Modifier.size(64.dp),
                forma = CircleShape
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = conversa.nome,
                    color = Color.White,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = conversa.mensagens.lastOrNull() ?: "Nenhuma mensagem ainda",
                    color = CorFracaChat,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = aoRemover) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Remover conversa",
                    tint = Color(0xFF8A8A8A)
                )
            }
        }
        DivisorLinha()
    }
}

@Composable
private fun DivisorLinha() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 94.dp)
            .height(1.dp)
            .background(CorBordaChat)
    )
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