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
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.componentes.FotoOuInicial
import com.example.myapplication.data.repository.ResumoConversa
import com.example.myapplication.data.repository.SocialRepositorio
import com.example.myapplication.modelo.foto
import kotlinx.coroutines.launch
import com.example.myapplication.modelo.Conversa
import com.example.myapplication.modelo.Pessoa
import com.example.myapplication.navegacao.Rotas
import com.example.myapplication.componentes.BarraRolagem
import androidx.compose.foundation.lazy.rememberLazyListState

private val AmareloChat = Color(0xFFF2B600)
private val CorFracaChat = Color(0xFFB5B5B5)
private val CorBordaChat = Color(0xFF2E2E2E)
private val CorPilulaChat = Color(0xFF1A1A1A)



@Composable
fun TelaListaConversas(navController: NavController) {
    val contexto = LocalContext.current
    val repositorio = remember { SocialRepositorio.obter(contexto) }
    val escopo = rememberCoroutineScope()

    var mostrarDialogo by remember { mutableStateOf(false) }
    var folhaSeguranca by remember { mutableStateOf(FolhaSeguranca.NENHUMA) }
    var conversaEditando by remember { mutableStateOf<Conversa?>(null) }

    var curtidas by remember { mutableStateOf<List<Pessoa>>(emptyList()) }
    var resumos by remember { mutableStateOf<List<ResumoConversa>>(emptyList()) }

    suspend fun recarregar() {
        curtidas = repositorio.listarPessoas()
        resumos = repositorio.listarResumos()
    }
    LaunchedEffect(Unit) { recarregar() }

    fun alterar(operacao: suspend () -> Unit) {
        escopo.launch {
            operacao()
            recarregar()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
    ) {
        CabecalhoChat(
            aoNovaConversa = { mostrarDialogo = true },
            aoSeguranca = { folhaSeguranca = FolhaSeguranca.KIT },
            aoConvidar = {
                navController.navigate(Rotas.DOUBLE_DATE) { launchSingleTop = true }
            }
        )

        Text(
            text = "Novos matches",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LinhaNovosMatches(
            curtidas = curtidas,
            aoClicarCurtidas = {
                navController.navigate(Rotas.CURTIDAS) { launchSingleTop = true }
            },
            aoClicarPessoa = { pessoa ->
                escopo.launch {
                    repositorio.iniciarConversaComPessoa(pessoa)?.let { conversa ->
                        navController.navigate(Rotas.detalheConversa(conversa.id))
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        PainelMensagens(
            navController = navController,
            resumos = resumos,
            aoEditar = { conversaEditando = it },
            aoRemover = { alterar { repositorio.removerConversa(it) } },
            modifier = Modifier.weight(1f)
        )
    }

    when (folhaSeguranca) {
        FolhaSeguranca.KIT -> KitSegurancaSheet(
            aoFechar = { folhaSeguranca = FolhaSeguranca.NENHUMA },
            aoDenunciar = { folhaSeguranca = FolhaSeguranca.DENUNCIA },
            aoConfigMensagens = {
                folhaSeguranca = FolhaSeguranca.NENHUMA
                navController.navigate(Rotas.CONFIG_MENSAGENS) { launchSingleTop = true }
            },
            aoCentralSeguranca = {
                folhaSeguranca = FolhaSeguranca.NENHUMA
                navController.navigate(Rotas.CENTRAL_SEGURANCA) { launchSingleTop = true }
            }
        )
        FolhaSeguranca.DENUNCIA -> DenunciaSheet(
            aoFechar = { folhaSeguranca = FolhaSeguranca.NENHUMA }
        )
        FolhaSeguranca.NENHUMA -> Unit
    }

    if (mostrarDialogo) {
        DialogNovaConversa(
            aoFechar = { mostrarDialogo = false },
            aoConfirmar = { nome ->
                alterar { repositorio.criarConversa(nome) }
                mostrarDialogo = false
            }
        )
    }

    conversaEditando?.let { alvo ->
        DialogNovaConversa(
            titulo = "Renomear conversa",
            rotulo = "Nome da conversa",
            botao = "Salvar",
            valorInicial = alvo.nome,
            aoFechar = { conversaEditando = null },
            aoConfirmar = { nome ->
                alterar { repositorio.renomearConversa(alvo, nome) }
                conversaEditando = null
            }
        )
    }
}


@Composable
private fun CabecalhoChat(
    aoNovaConversa: () -> Unit,
    aoSeguranca: () -> Unit,
    aoConvidar: () -> Unit
) {
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
            IconButton(onClick = aoSeguranca) {
                Icon(Icons.Filled.Shield, contentDescription = "Segurança", tint = Color.White)
            }
            IconButton(onClick = aoConvidar) {
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
    curtidas: List<Pessoa>,
    aoClicarCurtidas: () -> Unit,
    aoClicarPessoa: (Pessoa) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { CartaoResumoCurtidas(total = curtidas.size, aoClicar = aoClicarCurtidas) }
        items(curtidas, key = { it.id }) { pessoa ->
            CartaoMatch(pessoa = pessoa, aoClicar = { aoClicarPessoa(pessoa) })
        }
    }
}

@Composable
private fun CartaoResumoCurtidas(total: Int, aoClicar: () -> Unit) {
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
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(AmareloChat),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = total.toString(),
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
private fun PainelMensagens(
    navController: NavController,
    resumos: List<ResumoConversa>,
    aoEditar: (Conversa) -> Unit,
    aoRemover: (Conversa) -> Unit,
    modifier: Modifier = Modifier
) {
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

        val estadoLista = rememberLazyListState()
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            LazyColumn(
                state = estadoLista,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 110.dp)
            ) {
                item {
                    LinhaMensagemDoApp(aoClicar = {
                        navController.navigate(Rotas.PERFIL) { launchSingleTop = true }
                    })
                }
                items(resumos, key = { it.conversa.id }) { resumo ->
                    LinhaConversa(
                        resumo = resumo,
                        aoClicar = { navController.navigate(Rotas.detalheConversa(resumo.conversa.id)) },
                        aoEditar = { aoEditar(resumo.conversa) },
                        aoRemover = { aoRemover(resumo.conversa) }
                    )
                }
                if (resumos.isEmpty()) {
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
            BarraRolagem(
                estado = estadoLista,
                modifier = Modifier.align(Alignment.CenterEnd).padding(bottom = 100.dp)
            )
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
    resumo: ResumoConversa,
    aoClicar: () -> Unit,
    aoEditar: () -> Unit,
    aoRemover: () -> Unit
) {
    val conversa = resumo.conversa
    val pessoa = resumo.pessoa

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
                    text = resumo.ultimaMensagem ?: "Nenhuma mensagem ainda",
                    color = CorFracaChat,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = aoEditar) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Renomear conversa",
                    tint = Color(0xFF8A8A8A)
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
private fun DialogNovaConversa(
    aoFechar: () -> Unit,
    aoConfirmar: (String) -> Unit,
    titulo: String = "Nova conversa",
    rotulo: String = "Nova conversa com...",
    botao: String = "Iniciar",
    valorInicial: String = ""
) {
    var nome by remember { mutableStateOf(valorInicial) }

    AlertDialog(
        onDismissRequest = aoFechar,
        title = { Text(titulo) },
        text = {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text(rotulo) },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { if (nome.isNotBlank()) aoConfirmar(nome) }) {
                Text(botao)
            }
        },
        dismissButton = {
            TextButton(onClick = aoFechar) { Text("Cancelar") }
        }
    )
}