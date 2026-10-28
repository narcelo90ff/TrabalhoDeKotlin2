@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
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
import com.example.myapplication.data.repository.SocialRepositorio
import com.example.myapplication.modelo.foto
import com.example.myapplication.modelo.Pessoa
import com.example.myapplication.navegacao.Rotas
import kotlinx.coroutines.launch
import com.example.myapplication.componentes.BarraRolagem
import androidx.compose.foundation.lazy.grid.rememberLazyGridState

@Composable
fun TelaListaCurtidas(navController: NavController) {
    val contexto = LocalContext.current
    val repositorio = remember { SocialRepositorio.obter(contexto) }
    val escopo = rememberCoroutineScope()

    var mostrarDialogo by remember { mutableStateOf(false) }
    var pessoaEditando by remember { mutableStateOf<Pessoa?>(null) }
    var ordem by remember { mutableStateOf("Recomendado") }
    var mostrarOrdenacao by remember { mutableStateOf(false) }

    var curtidas by remember { mutableStateOf<List<Pessoa>>(emptyList()) }
    LaunchedEffect(Unit) { curtidas = repositorio.listarPessoas() }

    fun alterar(operacao: suspend () -> Unit) {
        escopo.launch {
            operacao()
            curtidas = repositorio.listarPessoas()
        }
    }

    val lista = ordenarCurtidas(curtidas, ordem)
    val estadoGrade = rememberLazyGridState()

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
                        text = curtidas.size.toString(),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFF1F1F1))
                    .clickable { mostrarOrdenacao = true }
                    .padding(start = 20.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(ordem, color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color.Black)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Todas as curtidas",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 4.dp)
            )

            LazyVerticalGrid(
                state = estadoGrade,
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(lista, key = { it.id }) { pessoa ->
                    CartaoCurtida(
                        pessoa = pessoa,
                        aoClicar = {
                            escopo.launch {
                                repositorio.iniciarConversaComPessoa(pessoa)?.let { conversa ->
                                    navController.navigate(Rotas.detalheConversa(conversa.id))
                                }
                            }
                        },
                        aoFavoritar = { alterar { repositorio.alternarFavorito(pessoa) } },
                        aoEditar = { pessoaEditando = pessoa },
                        aoRemover = { alterar { repositorio.removerPessoa(pessoa) } }
                    )
                }
            }
        }

        BarraRolagem(
            estado = estadoGrade,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .statusBarsPadding()
                .padding(top = 200.dp, bottom = 100.dp)
        )

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

    if (mostrarOrdenacao) {
        SheetOrdenacao(
            ordemAtual = ordem,
            aoAplicar = { ordem = it },
            aoFechar = { mostrarOrdenacao = false }
        )
    }

    if (mostrarDialogo) {
        DialogNovaCurtida(
            aoFechar = { mostrarDialogo = false },
            aoConfirmar = { nome, idade, mensagem ->
                alterar { repositorio.adicionarPessoa(nome = nome, idade = idade, bio = mensagem) }
                mostrarDialogo = false
            }
        )
    }

    pessoaEditando?.let { alvo ->
        DialogNovaCurtida(
            inicial = alvo,
            aoFechar = { pessoaEditando = null },
            aoConfirmar = { nome, idade, mensagem ->
                alterar {
                    repositorio.atualizarPessoa(alvo.copy(nome = nome.trim(), idade = idade, bio = mensagem))
                }
                pessoaEditando = null
            }
        )
    }
}

@Composable
private fun CartaoCurtida(
    pessoa: Pessoa,
    aoClicar: () -> Unit,
    aoFavoritar: () -> Unit,
    aoEditar: () -> Unit,
    aoRemover: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.72f)
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = aoClicar)
    ) {
        FotoOuInicial(
            foto = pessoa.foto,
            nome = pessoa.nome,
            modifier = Modifier.fillMaxSize(),
            tamanhoLetra = 64.sp
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
                .align(Alignment.BottomCenter)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xE6000000))))
        )

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
                icone = Icons.Filled.Edit,
                cor = Color.White,
                descricao = "Editar",
                aoClicar = aoEditar
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
    aoConfirmar: (String, Int, String) -> Unit,
    inicial: Pessoa? = null
) {
    var nomeNovo by remember { mutableStateOf(inicial?.nome ?: "") }
    var idadeNova by remember { mutableStateOf(inicial?.idade?.toString() ?: "") }
    var bioNova by remember { mutableStateOf(inicial?.bio ?: "") }

    AlertDialog(
        onDismissRequest = aoFechar,
        title = { Text(if (inicial == null) "Adicionar curtida" else "Editar curtida") },
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
                    aoConfirmar(
                        nomeNovo,
                        idadeNova.toIntOrNull() ?: 18,
                        bioNova.ifBlank { "Nova curtida!" }
                    )
                }
            }) {
                Text(if (inicial == null) "Adicionar" else "Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = aoFechar) { Text("Cancelar") }
        }
    )
}

private val opcoesOrdem = listOf(
    "Recomendado",
    "Mais recentes",
    "Destaques",
    "Distância",
    "Online recentemente"
)

private fun ordenarCurtidas(curtidas: List<Pessoa>, ordem: String): List<Pessoa> = when (ordem) {
    "Mais recentes" -> curtidas.sortedByDescending { it.id }
    "Destaques" -> curtidas.sortedByDescending { it.favorito }
    "Distância" -> curtidas.sortedBy { it.distanciaKm ?: Int.MAX_VALUE }
    "Online recentemente" -> curtidas.sortedBy { (it.id * 37) % 90 }
    else -> curtidas.toList()
}

@Composable
private fun SheetOrdenacao(
    ordemAtual: String,
    aoAplicar: (String) -> Unit,
    aoFechar: () -> Unit
) {
    val estadoSheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val escopo = rememberCoroutineScope()
    var escolhida by remember { mutableStateOf(ordemAtual) }

    fun fechar(depois: () -> Unit = {}) {
        escopo.launch { estadoSheet.hide() }.invokeOnCompletion {
            depois()
            aoFechar()
        }
    }

    ModalBottomSheet(
        onDismissRequest = aoFechar,
        sheetState = estadoSheet,
        containerColor = Color(0xFF1A1A1A),
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .navigationBarsPadding()
                .padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BotaoRedondoSheet(
                    icone = Icons.Filled.Close,
                    descricao = "Fechar",
                    fundo = Color.Transparent,
                    corIcone = Color.White,
                    aoClicar = { fechar() }
                )
                Text(
                    text = "Organize suas curtidas por",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                BotaoRedondoSheet(
                    icone = Icons.Filled.Check,
                    descricao = "Aplicar",
                    fundo = Color.White,
                    corIcone = Color.Black,
                    aoClicar = { fechar { aoAplicar(escolhida) } }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            opcoesOrdem.forEach { opcao ->
                OpcaoOrdem(
                    texto = opcao,
                    selecionada = escolhida == opcao,
                    aoClicar = { escolhida = opcao }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun BotaoRedondoSheet(
    icone: androidx.compose.ui.graphics.vector.ImageVector,
    descricao: String,
    fundo: Color,
    corIcone: Color,
    aoClicar: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(fundo)
            .clickable(onClick = aoClicar),
        contentAlignment = Alignment.Center
    ) {
        Icon(icone, contentDescription = descricao, tint = corIcone, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun OpcaoOrdem(texto: String, selecionada: Boolean, aoClicar: () -> Unit) {
    val forma = RoundedCornerShape(10.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(Color(0xFF121212))
            .border(1.5.dp, if (selecionada) Color(0xFFD32F2F) else Color(0xFF2E2E2E), forma)
            .clickable(onClick = aoClicar)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = texto,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        if (selecionada) {
            Icon(
                Icons.Filled.Check,
                contentDescription = "Selecionado",
                tint = Color(0xFFD32F2F),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}