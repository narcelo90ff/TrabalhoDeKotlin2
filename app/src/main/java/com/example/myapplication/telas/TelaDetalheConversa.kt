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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.dados.AppDados

/**
 * Representa uma mensagem enviada ou recebida, só para controlar o lado
 * do balão — não é um dado novo salvo em lugar nenhum, só um "rótulo"
 * calculado na hora de desenhar a tela.
 */
private data class Mensagem(val texto: String, val enviadaPorMim: Boolean)

@Composable
fun TelaDetalheConversa(navController: NavController, conversaId: Int) {
    val conversa = AppDados.buscarConversa(conversaId)
    var textoNovaMensagem by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF3A3A3A))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(conversa?.nome ?: "Conversa")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    Icon(Icons.Filled.Videocam, contentDescription = "Chamada de vídeo", tint = Color(0xFF5AB2FF))
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(Icons.Filled.MoreVert, contentDescription = "Mais opções")
                    Spacer(modifier = Modifier.width(8.dp))
                }
            )
        }
    ) { padding ->
        if (conversa == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Essa conversa não existe mais.")
            }
            return@Scaffold
        }

        // combina dado das duas listas: busca a Pessoa ligada a essa conversa
        val pessoa = conversa.pessoaId?.let { AppDados.buscarPessoa(it) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "VOCÊ DEU MATCH COM ${conversa.nome.uppercase()}",
                color = Color.Gray,
                fontSize = 11.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            if (pessoa != null) {
                Text(
                    text = "${pessoa.idade} anos — ${pessoa.bio}",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // alterna o lado das mensagens: ímpar = enviada por mim, par = recebida
            // (só pra ilustrar visualmente, já que aqui é só uma lista simples de texto)
            val mensagens = conversa.mensagens.mapIndexed { indice, texto ->
                Mensagem(texto = texto, enviadaPorMim = indice % 2 == 0)
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(mensagens) { mensagem ->
                    BalaoMensagem(mensagem)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textoNovaMensagem,
                    onValueChange = { textoNovaMensagem = it },
                    label = { Text("Mensagem") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = {
                    if (textoNovaMensagem.isNotBlank()) {
                        conversa.mensagens.add(textoNovaMensagem)
                        textoNovaMensagem = ""
                    }
                }) {
                    Icon(Icons.Filled.Send, contentDescription = "Enviar", tint = Color(0xFF5AB2FF))
                }
            }
        }
    }
}

@Composable
private fun BalaoMensagem(mensagem: Mensagem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (mensagem.enviadaPorMim) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 260.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (mensagem.enviadaPorMim) Color(0xFF3B82F6) else Color(0xFF2A2A2A))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(text = mensagem.texto, color = Color.White)
        }

        if (!mensagem.enviadaPorMim) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                Icons.Filled.FavoriteBorder,
                contentDescription = "Curtir mensagem",
                tint = Color.Gray,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}