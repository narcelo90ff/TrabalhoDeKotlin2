package com.example.myapplication.telas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.dados.PerfilDados
import com.example.myapplication.dados.TipoSelecao

private val FundoEdicao = Color(0xFF121212)
private val CartaoEdicao = Color(0xFF1E1E1E)
private val TextoFracoEdicao = Color(0xFFB5B5B5)
private val VerdeSpotify = Color(0xFF3DAA5C)

@Composable
private fun BarraSuperiorEdicao(
    titulo: String,
    subtitulo: String? = null,
    aoFechar: () -> Unit,
    aoConfirmar: (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.Close,
            contentDescription = "Fechar",
            tint = Color.White,
            modifier = Modifier
                .size(28.dp)
                .clickable(onClick = aoFechar)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, maxLines = 2)
            if (subtitulo != null) {
                Text(subtitulo, color = TextoFracoEdicao, fontSize = 14.sp)
            }
        }
        if (aoConfirmar != null) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2E2E2E))
                    .clickable(onClick = aoConfirmar),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Check, contentDescription = "Confirmar", tint = Color.White)
            }
        }
    }
}

@Composable
private fun CampoTexto(
    valor: String,
    aoMudar: (String) -> Unit,
    dica: String,
    modifier: Modifier = Modifier,
    umaLinha: Boolean = false
) {
    BasicTextField(
        value = valor,
        onValueChange = aoMudar,
        singleLine = umaLinha,
        textStyle = TextStyle(color = Color.White, fontSize = 17.sp),
        cursorBrush = SolidColor(Color.White),
        modifier = modifier,
        decorationBox = { campo ->
            Box {
                if (valor.isEmpty()) {
                    Text(dica, color = TextoFracoEdicao, fontSize = 17.sp)
                }
                campo()
            }
        }
    )
}

// ---------------------------------------------------------------- BIO

@Composable
fun TelaEditarBio(navController: NavController) {
    var texto by remember { mutableStateOf(PerfilDados.bio) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoEdicao)
            .statusBarsPadding()
    ) {
        BarraSuperiorEdicao(
            titulo = "Adicionar bio",
            aoFechar = { navController.popBackStack() },
            aoConfirmar = {
                PerfilDados.bio = texto.trim()
                navController.popBackStack()
            }
        )
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CartaoEdicao)
                    .padding(18.dp)
            ) {
                CampoTexto(
                    valor = texto,
                    aoMudar = { if (it.length <= PerfilDados.LIMITE_BIO) texto = it },
                    dica = "Escreva um pouco sobre você",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                )
                Text(
                    "${texto.length}/${PerfilDados.LIMITE_BIO}",
                    color = TextoFracoEdicao,
                    fontSize = 13.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CartaoEdicao)
                    .padding(18.dp)
            ) {
                Text("Dica para bio", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "As melhores bios são curtas e objetivas. Compartilhe seus interesses, valores e o que você procura.",
                    color = TextoFracoEdicao,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                "Excluir",
                color = TextoFracoEdicao,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { texto = "" }
                    .padding(12.dp)
            )
        }
    }
}

// ---------------------------------------------------------------- PERGUNTAS

@Composable
fun TelaEditarPergunta(navController: NavController) {
    var escolhida by remember { mutableStateOf(PerfilDados.perguntaEscolhida) }
    var resposta by remember { mutableStateOf(PerfilDados.respostaPergunta) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoEdicao)
            .statusBarsPadding()
    ) {
        if (escolhida == null) {
            BarraSuperiorEdicao(
                titulo = "Selecione uma pergunta",
                aoFechar = { navController.popBackStack() },
                aoConfirmar = null
            )
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(PerfilDados.perguntas) { pergunta ->
                    Text(
                        pergunta,
                        color = Color.White,
                        fontSize = 17.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { escolhida = pergunta }
                            .padding(horizontal = 20.dp, vertical = 18.dp)
                    )
                }
            }
        } else {
            BarraSuperiorEdicao(
                titulo = "Sua resposta",
                aoFechar = { escolhida = null },
                aoConfirmar = {
                    PerfilDados.perguntaEscolhida = escolhida
                    PerfilDados.respostaPergunta = resposta.trim()
                    navController.popBackStack()
                }
            )
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(CartaoEdicao)
                        .padding(18.dp)
                ) {
                    Text(escolhida ?: "", color = TextoFracoEdicao, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    CampoTexto(
                        valor = resposta,
                        aoMudar = { if (it.length <= 200) resposta = it },
                        dica = "Escreva sua resposta",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )
                    Text(
                        "${resposta.length}/200",
                        color = TextoFracoEdicao,
                        fontSize = 13.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    "Trocar pergunta",
                    color = TextoFracoEdicao,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { escolhida = null }
                        .padding(12.dp)
                )
            }
        }
    }
}

// ---------------------------------------------------------------- PRIMEIRO ENCONTRO

@Composable
fun TelaEditarEncontro(navController: NavController) {
    var resposta by remember { mutableStateOf(PerfilDados.respostaEncontro) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoEdicao)
            .statusBarsPadding()
    ) {
        BarraSuperiorEdicao(
            titulo = "Sua resposta",
            aoFechar = { navController.popBackStack() },
            aoConfirmar = {
                PerfilDados.respostaEncontro = resposta.trim()
                navController.popBackStack()
            }
        )
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CartaoEdicao)
                    .padding(18.dp)
            ) {
                Text(PerfilDados.PERGUNTA_ENCONTRO, color = TextoFracoEdicao, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))
                CampoTexto(
                    valor = resposta,
                    aoMudar = { if (it.length <= 200) resposta = it },
                    dica = "Escreva sua resposta",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
                Text(
                    "${resposta.length}/200",
                    color = TextoFracoEdicao,
                    fontSize = 13.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Excluir",
                color = TextoFracoEdicao,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { resposta = "" }
                    .padding(12.dp)
            )
        }
    }
}

// ---------------------------------------------------------------- SELEÇÃO DE ITENS

@Composable
fun TelaSelecaoItens(navController: NavController, tipo: TipoSelecao) {
    val selecionados = remember {
        mutableStateListOf<String>().apply { addAll(PerfilDados.lista(tipo)) }
    }
    var busca by remember { mutableStateOf("") }

    fun alternar(item: String) {
        if (selecionados.contains(item)) selecionados.remove(item)
        else if (selecionados.size < tipo.maximo) selecionados.add(item)
    }

    fun salvar() {
        val destino = PerfilDados.lista(tipo)
        destino.clear()
        destino.addAll(selecionados)
        navController.popBackStack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoEdicao)
            .statusBarsPadding()
    ) {
        BarraSuperiorEdicao(
            titulo = tipo.titulo,
            subtitulo = "${selecionados.size} de ${tipo.maximo}",
            aoFechar = { navController.popBackStack() },
            aoConfirmar = { salvar() }
        )

        if (selecionados.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selecionados.toList().forEach { item ->
                    val capa = PerfilDados.capa(tipo, item)
                    if (capa != null) {
                        MiniCapa(item, capa, tipo == TipoSelecao.TIMES) { alternar(item) }
                    } else {
                        ChipSelecionado(item, selecionado = true) { alternar(item) }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .clip(RoundedCornerShape(50))
                .background(CartaoEdicao)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Search, contentDescription = null, tint = TextoFracoEdicao)
            Spacer(modifier = Modifier.width(10.dp))
            CampoTexto(
                valor = busca,
                aoMudar = { busca = it },
                dica = tipo.placeholderBusca,
                umaLinha = true,
                modifier = Modifier.weight(1f)
            )
        }

        val catalogo = PerfilDados.catalogo(tipo)
            .filter { busca.isBlank() || it.contains(busca.trim(), ignoreCase = true) }

        when (tipo) {
            TipoSelecao.HOBBIES -> ListaHobbies(busca, selecionados) { alternar(it) }
            TipoSelecao.MUSICAS -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(catalogo) { musica ->
                    val marcado = selecionados.contains(musica)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { alternar(musica) }
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(VerdeSpotify)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            musica,
                            color = Color.White,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        if (marcado) {
                            Icon(Icons.Filled.Check, contentDescription = "Selecionada", tint = Color.White)
                        }
                    }
                }
            }
            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(catalogo) { item ->
                    TileItem(item, selecionados.contains(item), PerfilDados.capa(tipo, item), tipo == TipoSelecao.TIMES) { alternar(item) }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ListaHobbies(
    busca: String,
    selecionados: List<String>,
    aoClicar: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        PerfilDados.categoriasHobbies.forEach { categoria ->
            val itens = categoria.itens.filter {
                busca.isBlank() || it.contains(busca.trim(), ignoreCase = true)
            }
            if (itens.isNotEmpty()) {
                Text(
                    categoria.nome,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itens.forEach { hobby ->
                        ChipSelecionado(hobby, selecionado = selecionados.contains(hobby)) { aoClicar(hobby) }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun ChipSelecionado(texto: String, selecionado: Boolean, aoClicar: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (selecionado) Color(0xFFEDEDED) else CartaoEdicao)
            .clickable(onClick = aoClicar)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            texto,
            color = if (selecionado) Color.Black else Color.White,
            fontSize = 15.sp
        )
        if (selecionado) {
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.Filled.Close, contentDescription = "Remover", tint = Color.Black, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun TileItem(nome: String, selecionado: Boolean, capa: Int?, escudo: Boolean, aoClicar: () -> Unit) {
    val matiz = (nome.hashCode().toLong().let { if (it < 0) -it else it } % 360).toFloat()
    val icone: ImageVector = if (selecionado) Icons.Filled.Remove else Icons.Filled.Add
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = aoClicar)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(if (escudo) 1f else 0.72f)
                .clip(RoundedCornerShape(14.dp))
                .background(if (escudo) Color.White else Color.hsv(matiz, 0.45f, if (selecionado) 0.35f else 0.55f))
        ) {
            if (capa != null) {
                Image(
                    painter = painterResource(capa),
                    contentDescription = nome,
                    contentScale = if (escudo) ContentScale.Fit else ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(if (escudo) 12.dp else 0.dp)
                        .alpha(if (selecionado) 0.4f else 1f)
                )
            } else {
                Text(
                    nome.take(1).uppercase(),
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(icone, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            nome,
            color = if (selecionado) TextoFracoEdicao else Color.White,
            fontSize = 12.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}
@Composable
private fun MiniCapa(nome: String, capa: Int, escudo: Boolean, aoRemover: () -> Unit) {
    Box(
        modifier = Modifier
            .size(width = if (escudo) 56.dp else 48.dp, height = if (escudo) 56.dp else 68.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (escudo) Color.White else Color.Transparent)
            .clickable(onClick = aoRemover)
    ) {
        Image(
            painter = painterResource(capa),
            contentDescription = nome,
            contentScale = if (escudo) ContentScale.Fit else ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .padding(if (escudo) 6.dp else 0.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(2.dp)
                .size(16.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Close, contentDescription = "Remover", tint = Color.Black, modifier = Modifier.size(12.dp))
        }
    }
}