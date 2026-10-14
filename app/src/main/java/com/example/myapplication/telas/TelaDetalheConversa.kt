package com.example.myapplication.telas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.componentes.FotoOuInicial
import com.example.myapplication.dados.AppDados
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.myapplication.componentes.BarraRolagem

private val CorFundoChat = Color(0xFF121212)
private val CorBotaoChat = Color(0xFF1E1E1E)
private val CorBalaoMeu = Color(0xFFF1DADA)
private val CorBalaoDela = Color(0xFF2A2A2A)
private val CorTextoFracoChat = Color(0xFF9A9A9A)

@Composable
fun TelaDetalheConversa(navController: NavController, conversaId: Int) {
    val conversa = AppDados.buscarConversa(conversaId)

    if (conversa == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CorFundoChat)
                .statusBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Text("Essa conversa não existe mais.", color = Color.White)
        }
        return
    }

    val pessoa = conversa.pessoaId?.let { AppDados.buscarPessoa(it) }
    val temFotoNaIntro = pessoa?.foto != null

    var texto by remember { mutableStateOf("") }
    val estadoLista = rememberLazyListState()

    val ptBR = remember { Locale("pt", "BR") }
    val agora = remember { Date() }
    val dataMatch = remember { SimpleDateFormat("dd/MM/yy", ptBR).format(agora) }
    val dataHora = remember { SimpleDateFormat("d 'de' MMM., HH:mm", ptBR).format(agora) }

    val itensFixos = if (temFotoNaIntro) 3 else 2
    val totalItens = itensFixos + conversa.mensagens.size

    LaunchedEffect(totalItens) {
        estadoLista.animateScrollToItem(totalItens - 1)
    }

    fun enviar() {
        if (texto.isNotBlank()) {
            conversa.mensagens.add(texto.trim())
            conversa.minhasMensagens.add(conversa.mensagens.size - 1)
            texto = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CorFundoChat)
            .statusBarsPadding()
            .imePadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BotaoRedondoChat(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                FotoOuInicial(
                    foto = pessoa?.foto,
                    nome = conversa.nome,
                    modifier = Modifier.size(56.dp),
                    forma = CircleShape,
                    tamanhoLetra = 22.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = conversa.nome,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(CorBotaoChat)
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                )
            }
            BotaoRedondoChat(onClick = { }) {
                Icon(Icons.Filled.MoreHoriz, contentDescription = "Mais opções", tint = Color.White)
            }
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            LazyColumn(
                state = estadoLista,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom)
            ) {
                item {
                    Text(
                        text = "Você deu match com ${conversa.nome} em $dataMatch",
                        color = CorTextoFracoChat,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    )
                }
                item {
                    Text(
                        text = dataHora,
                        color = CorTextoFracoChat,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (temFotoNaIntro) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            Image(
                                painter = painterResource(id = pessoa!!.foto!!),
                                contentDescription = "Foto de ${conversa.nome}",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(width = 86.dp, height = 104.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            BalaoChat("Você curtiu a foto do(a) ${conversa.nome}", minha = true)
                            Text("Enviado", color = CorTextoFracoChat, fontSize = 11.sp)
                        }
                    }
                }
                itemsIndexed(conversa.mensagens) { indice, mensagem ->
                    val minha = indice in conversa.minhasMensagens
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = if (minha) Alignment.End else Alignment.Start
                    ) {
                        BalaoChat(mensagem, minha = minha)
                        if (minha && indice == conversa.minhasMensagens.lastOrNull()) {
                            Text("Enviado", color = CorTextoFracoChat, fontSize = 11.sp)
                        }
                    }
                }
            }
            BarraRolagem(estado = estadoLista, modifier = Modifier.align(Alignment.CenterEnd))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(CorBotaoChat),
                contentAlignment = Alignment.Center
            ) {
                Text("GIF", color = CorTextoFracoChat, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(10.dp))
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                placeholder = { Text("Digite uma mensagem", color = CorTextoFracoChat) },
                singleLine = true,
                shape = RoundedCornerShape(50),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { enviar() }),
                trailingIcon = {
                    if (texto.isNotBlank()) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Enviar",
                            tint = Color(0xFF8FB0F0),
                            modifier = Modifier.clickable { enviar() }
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = CorBotaoChat,
                    unfocusedContainerColor = CorBotaoChat,
                    focusedBorderColor = Color(0xFF3A3A3A),
                    unfocusedBorderColor = Color(0xFF2A2A2A),
                    cursorColor = Color.White
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BotaoRedondoChat(onClick: () -> Unit, conteudo: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(CorBotaoChat)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        conteudo()
    }
}

@Composable
private fun BalaoChat(texto: String, minha: Boolean) {
    Text(
        text = texto,
        color = if (minha) Color.Black else Color.White,
        fontSize = 16.sp,
        modifier = Modifier
            .widthIn(max = 280.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (minha) CorBalaoMeu else CorBalaoDela)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    )
}