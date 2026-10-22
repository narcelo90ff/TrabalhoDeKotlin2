package com.example.myapplication.telas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmokingRooms
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.dados.AppDados
import com.example.myapplication.dados.banco.SocialRepositorio
import kotlinx.coroutines.launch
import com.example.myapplication.dados.AbaDescobrir
import com.example.myapplication.dados.Astrologia
import com.example.myapplication.dados.DescobrirEstado
import com.example.myapplication.modelo.Perfil
import com.example.myapplication.navegacao.Rotas
import com.example.myapplication.componentes.BarraRolagem

private val CorCartao = Color(0xFF1A1A1A)
private val CorChip = Color(0xFF0B0B0B)
private val CorChipDestaque = Color(0xFFF4A6E0)
private val CorDivisor = Color(0xFF333333)
private val CorTextoFraco = Color(0xFFB5B5B5)
private val CorAzul = Color(0xFF8FB0F0)
private val CorBotao = Color(0xFF1E1E1E)

@Composable
fun TelaDescobrir(navController: NavController) {
    DeslizarPerfis(perfis = AppDados.perfis, navController = navController)
}


@Composable
fun DeslizarPerfis(
    perfis: List<Perfil>,
    navController: NavController,
    mostrarAbas: Boolean = true,
    mostrarInteresses: Boolean = false
) {
    val contexto = LocalContext.current
    val repositorio = remember { SocialRepositorio.obter(contexto) }
    val escopo = rememberCoroutineScope()

    var indice by rememberSaveable { mutableIntStateOf(0) }
    var mostrandoDetalhe by rememberSaveable { mutableStateOf(false) }
    var matchAtual by remember { mutableStateOf<DadosMatch?>(null) }

    if (perfis.isEmpty()) {
        TelaSemPerfis(onRecomecar = { indice = 0 })
        return
    }

    val aba = if (mostrarAbas) DescobrirEstado.aba else AbaDescobrir.PRA_VOCE
    val posicao = indice.mod(perfis.size)
    val perfil = perfis[posicao]
    val parceiro: Perfil? =
        if (aba == AbaDescobrir.DOUBLE_DATE && perfis.size > 1) perfis[(posicao + 1) % perfis.size] else null

    fun proximo() {
        mostrandoDetalhe = false
        indice = (indice.mod(perfis.size) + 1) % perfis.size
    }

    val aoPassar: () -> Unit = { proximo() }
    suspend fun registrarCurtida(favorito: Boolean) {
        val pessoa = repositorio.curtir(perfil, favorito = favorito)
        val pessoaParceiro = parceiro?.let { repositorio.curtir(it) }
        val alvo = listOfNotNull(perfil, parceiro).firstOrNull { it.id == ID_PESSOA_MATCH }
        if (alvo != null) {
            matchAtual = DadosMatch(alvo, if (alvo.id == perfil.id) pessoa else pessoaParceiro)
        }
    }

    val aoCurtir: () -> Unit = {
        escopo.launch { registrarCurtida(favorito = false) }
        proximo()
    }
    val aoFavoritar: () -> Unit = {
        escopo.launch { registrarCurtida(favorito = true) }
        proximo()
    }
    val aoVoltar: () -> Unit = {
        mostrandoDetalhe = false
        indice = (indice.mod(perfis.size) - 1 + perfis.size) % perfis.size
    }
    val aoResponder: () -> Unit = {
        escopo.launch {
            repositorio.curtir(perfil)
                ?.let { pessoa -> repositorio.iniciarConversaComPessoa(pessoa) }
                ?.let { conversa -> navController.navigate(Rotas.detalheConversa(conversa.id)) }
        }
    }
    fun mudarAba(nova: AbaDescobrir) {
        mostrandoDetalhe = false
        when (nova) {
            AbaDescobrir.PRA_VOCE -> {
                DescobrirEstado.aba = nova
            }
            AbaDescobrir.DOUBLE_DATE -> {
                DescobrirEstado.aba = nova
                // na primeira vez abre a tela de convidar amigos
                if (!DescobrirEstado.conviteDoubleDateVisto) {
                    DescobrirEstado.conviteDoubleDateVisto = true
                    navController.navigate(Rotas.DOUBLE_DATE)
                }
            }
            AbaDescobrir.ASTROLOGIA -> {
                if (Astrologia.ativo) {
                    DescobrirEstado.aba = nova
                } else {
                    navController.navigate(Rotas.ASTROLOGIA)
                }
            }
        }
    }
    val aoMudarAba: (AbaDescobrir) -> Unit = { mudarAba(it) }

    if (mostrandoDetalhe) {
        DetalhePerfil(
            perfil = perfil,
            aoFechar = { mostrandoDetalhe = false },
            aoPassar = aoPassar,
            aoFavoritar = aoFavoritar,
            aoCurtir = aoCurtir,
            aoResponder = aoResponder
        )
    } else {
        FotoPerfil(
            perfil = perfil,
            parceiro = parceiro,
            aba = aba,
            aoAbrirDetalhe = { mostrandoDetalhe = true },
            aoMudarAba = aoMudarAba,
            aoEditarAstrologia = { navController.navigate(Rotas.ASTROLOGIA) },
            aoTestarDoubleDate = { navController.navigate(Rotas.DOUBLE_DATE) },
            aoFiltros = { navController.navigate(Rotas.FILTROS) },
            aoVoltar = aoVoltar,
            aoPassar = aoPassar,
            aoFavoritar = aoFavoritar,
            aoCurtir = aoCurtir,
            aoResponder = aoResponder,
            mostrarAbas = mostrarAbas,
            mostrarInteresses = mostrarInteresses
        )
    }

    matchAtual?.let { match ->
        TelaMatch(
            perfil = match.perfil,
            aoFechar = { matchAtual = null },
            aoEnviar = { texto ->
                matchAtual = null
                escopo.launch {
                    match.pessoa
                        ?.let { pessoa -> repositorio.iniciarConversaComPessoa(pessoa) }
                        ?.let { conversa ->
                            repositorio.enviarMensagem(conversa.id, texto)
                            navController.navigate(Rotas.detalheConversa(conversa.id))
                        }
                }
            }
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FotoPerfil(
    perfil: Perfil,
    parceiro: Perfil?,
    aba: AbaDescobrir,
    aoAbrirDetalhe: () -> Unit,
    aoMudarAba: (AbaDescobrir) -> Unit,
    aoEditarAstrologia: () -> Unit,
    aoTestarDoubleDate: () -> Unit,
    aoFiltros: () -> Unit,
    aoVoltar: () -> Unit,
    aoPassar: () -> Unit,
    aoFavoritar: () -> Unit,
    aoCurtir: () -> Unit,
    aoResponder: () -> Unit,
    mostrarAbas: Boolean,
    mostrarInteresses: Boolean
) {
    val noAstrologia = aba == AbaDescobrir.ASTROLOGIA && !mostrarInteresses
    val textoCompat = if (noAstrologia) Astrologia.compatibilidade(perfil.signo) else null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(28.dp))
    ) {
        if (parceiro != null) {
            Row(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = perfil.foto),
                    contentDescription = "Foto de ${perfil.nome}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                Image(
                    painter = painterResource(id = parceiro.foto),
                    contentDescription = "Foto de ${parceiro.nome}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }
        } else {
            Image(
                painter = painterResource(id = perfil.foto),
                contentDescription = "Foto de ${perfil.nome}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .align(Alignment.TopCenter)
                .background(Brush.verticalGradient(listOf(Color(0xAA000000), Color.Transparent)))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (mostrarInteresses) 400.dp else 340.dp)
                .align(Alignment.BottomCenter)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xE6000000))))
        )

        if (mostrarAbas) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.Filled.Tune,
                    contentDescription = "Filtros",
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable(onClick = aoFiltros)
                )
                ItemAba("Pra você", aba == AbaDescobrir.PRA_VOCE, { aoMudarAba(AbaDescobrir.PRA_VOCE) })
                ItemAba("Double Date", aba == AbaDescobrir.DOUBLE_DATE, { aoMudarAba(AbaDescobrir.DOUBLE_DATE) })
                ItemAba(
                    texto = "Astrologia",
                    selecionada = aba == AbaDescobrir.ASTROLOGIA,
                    aoClicar = { aoMudarAba(AbaDescobrir.ASTROLOGIA) },
                    aoEditar = if (aba == AbaDescobrir.ASTROLOGIA && Astrologia.ativo) aoEditarAstrologia else null
                )
                Icon(Icons.Filled.Bolt, contentDescription = null, tint = Color(0xFFD38CF0), modifier = Modifier.size(26.dp))
            }

        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            if (parceiro == null) {
                Text(
                    text = "Perto de mim",
                    color = Color.Black,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color.White)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (parceiro != null) {
                        Text(
                            text = "${perfil.nome} ${perfil.idade} e ${parceiro.nome} ${parceiro.idade}",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(perfil.nome, color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(perfil.idade.toString(), color = Color.White, fontSize = 30.sp)
                            if (mostrarInteresses) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.Filled.Verified, contentDescription = "Verificado", tint = Color.White, modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                    if (!mostrarInteresses && aba == AbaDescobrir.PRA_VOCE) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(perfil.distancia, color = Color.White, fontSize = 16.sp)
                        }
                    }
                    if (noAstrologia) {
                        val indiceSigno = Astrologia.SIGNOS.indexOf(perfil.signo)
                        val simbolo = if (indiceSigno >= 0) Astrologia.SIMBOLOS[indiceSigno] else ""
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$simbolo ${perfil.signo}",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color(0x66000000))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                BotaoCirculo(
                    icone = Icons.Filled.ArrowUpward,
                    tamanho = 52.dp,
                    fundo = Color.White,
                    corIcone = Color.Black,
                    descricao = "Ver perfil completo",
                    aoClicar = aoAbrirDetalhe
                )
            }
            if (textoCompat != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(Astrologia.tituloCompatibilidade(perfil.signo) + " • ")
                        }
                        append(textoCompat)
                    },
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Ver mais >",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { aoAbrirDetalhe() }.padding(top = 4.dp)
                )
            }
            if (mostrarInteresses) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Apps, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Interesses", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.height(10.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    perfil.interesses.take(6).forEach { interesse ->
                        Text(
                            text = interesse,
                            color = Color.White,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .border(1.dp, Color(0x99FFFFFF), RoundedCornerShape(50))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            if (parceiro != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xE61A1A1A))
                        .clickable { aoTestarDoubleDate() }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Link, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Testar Double Date", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Dê match e converse com outras duplas.", color = CorTextoFraco, fontSize = 13.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BotaoCirculo(Icons.Filled.Replay, 48.dp, CorBotao, Color.White, "Voltar", aoVoltar)
                BotaoCirculo(Icons.Filled.Close, 68.dp, CorBotao, Color.White, "Não curti", aoPassar)
                BotaoCirculo(Icons.Filled.Star, 48.dp, CorBotao, CorAzul, "Favoritar", aoFavoritar)
                BotaoCirculo(Icons.Filled.Favorite, 68.dp, CorBotao, Color(0xFFEA1D25), "Curtir", aoCurtir)
                BotaoCirculo(Icons.AutoMirrored.Filled.Send, 48.dp, CorBotao, CorAzul, "Mandar mensagem", aoResponder)
            }
        }
    }
}

@Composable
private fun ItemAba(
    texto: String,
    selecionada: Boolean,
    aoClicar: () -> Unit,
    aoEditar: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (selecionada) Color(0x66000000) else Color.Transparent)
            .clickable { aoClicar() }
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = texto,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = if (selecionada) FontWeight.Bold else FontWeight.Normal
        )
        if (aoEditar != null) {
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                Icons.Filled.Edit,
                contentDescription = "Editar dados de astrologia",
                tint = Color.White,
                modifier = Modifier.size(16.dp).clickable { aoEditar() }
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetalhePerfil(
    perfil: Perfil,
    aoFechar: () -> Unit,
    aoPassar: () -> Unit,
    aoFavoritar: () -> Unit,
    aoCurtir: () -> Unit,
    aoResponder: () -> Unit
) {
    val estadoRolagem = rememberScrollState()
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF111111))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(perfil.nome, color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(perfil.idade.toString(), color = Color.White, fontSize = 30.sp)
                }
                BotaoCirculo(
                    icone = Icons.Filled.ArrowDownward,
                    tamanho = 48.dp,
                    fundo = Color.White,
                    corIcone = Color.Black,
                    descricao = "Voltar para a foto",
                    aoClicar = aoFechar
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(estadoRolagem)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(430.dp)
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(24.dp))
                ) {
                    Image(
                        painter = painterResource(id = perfil.foto),
                        contentDescription = "Foto de ${perfil.nome}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(14.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.White)
                            .clickable { aoResponder() }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = CorAzul, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Responder", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Cartao {
                    CabecalhoSecao(Icons.Filled.Search, perfil.procurando, comMenu = false)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(perfil.tipoRelacionamento, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(14.dp))
                    Divisor()
                    Spacer(modifier = Modifier.height(14.dp))
                    Chip("Monogamia")
                }

                Cartao {
                    CabecalhoSecao(Icons.Filled.FormatQuote, "Sobre mim", comMenu = true)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(perfil.sobreMim, color = Color.White, fontSize = 20.sp)
                    BotaoResponderLinha(aoResponder)
                }

                Cartao {
                    CabecalhoSecao(Icons.Filled.Person, "Informações básicas", comMenu = true)
                    Spacer(modifier = Modifier.height(12.dp))
                    LinhaInfo(Icons.Filled.LocationOn, perfil.distancia)
                    LinhaInfo(Icons.Filled.Straighten, perfil.altura)
                    LinhaInfo(Icons.Filled.School, perfil.escolaridade)
                    LinhaInfo(Icons.Filled.Person, perfil.genero, comDivisor = false)
                    BotaoResponderLinha(aoResponder)
                }

                Cartao {
                    CabecalhoSecao(Icons.Filled.FormatQuote, perfil.pergunta, comMenu = true)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(perfil.resposta, color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold, lineHeight = 36.sp)
                    BotaoResponderLinha(aoResponder)
                }

                Cartao {
                    CabecalhoSecao(Icons.Filled.LocalOffer, "Estilo de vida", comMenu = false)
                    Spacer(modifier = Modifier.height(12.dp))
                    CampoComRotulo("Bebida", Icons.Filled.LocalBar, perfil.bebida)
                    CampoComRotulo("Você fuma?", Icons.Filled.SmokingRooms, perfil.fuma)
                    CampoComRotulo("Atividade física", Icons.Filled.FitnessCenter, perfil.atividadeFisica)
                    CampoComRotulo("Pets", Icons.Filled.Pets, perfil.pets, comDivisor = false)
                    BotaoResponderLinha(aoResponder)
                }

                Cartao {
                    CabecalhoSecao(Icons.Filled.LocalOffer, "Mais informações", comMenu = false)
                    Spacer(modifier = Modifier.height(12.dp))
                    CampoComRotulo("Estilo de comunicação", Icons.Filled.Call, perfil.comunicacao)
                    CampoComRotulo("Signo", Icons.Filled.NightsStay, perfil.signo, comDivisor = false)
                    BotaoResponderLinha(aoResponder)
                }

                Cartao {
                    CabecalhoSecao(Icons.Filled.Favorite, "Interesses", comMenu = false)
                    Spacer(modifier = Modifier.height(16.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        perfil.interesses.forEachIndexed { posicao, interesse ->
                            Chip(interesse, destaque = posicao < 2)
                        }
                    }
                    BotaoResponderLinha(aoResponder)
                }

                BotaoAcao("Compartilhar ${perfil.nome}", Color.White, onClick = {})
                BotaoAcao("Bloquear ${perfil.nome}", Color.White, onClick = aoPassar)
                BotaoAcao("Denunciar ${perfil.nome}", Color(0xFFF07070), onClick = aoPassar)

                Spacer(modifier = Modifier.height(130.dp))
            }
        }

        BarraRolagem(
            estado = estadoRolagem,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .statusBarsPadding()
                .padding(top = 80.dp, bottom = 120.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xE6111111))))
                .padding(top = 24.dp, bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally)
            ) {
                BotaoCirculo(Icons.Filled.Close, 68.dp, CorBotao, Color.White, "Não curti", aoPassar)
                BotaoCirculo(Icons.Filled.Star, 48.dp, CorBotao, CorAzul, "Favoritar", aoFavoritar)
                BotaoCirculo(Icons.Filled.Favorite, 68.dp, CorBotao, Color(0xFFEA1D25), "Curtir", aoCurtir)
            }
        }
    }
}


@Composable
private fun TelaSemPerfis(onRecomecar: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Você já viu todo mundo por perto",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Volte mais tarde ou veja os perfis de novo.",
            color = CorTextoFraco,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onRecomecar) {
            Text("Ver de novo")
        }
    }
}


@Composable
private fun BotaoCirculo(
    icone: ImageVector,
    tamanho: androidx.compose.ui.unit.Dp,
    fundo: Color,
    corIcone: Color,
    descricao: String,
    aoClicar: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(tamanho)
            .clip(CircleShape)
            .background(fundo)
            .clickable { aoClicar() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icone,
            contentDescription = descricao,
            tint = corIcone,
            modifier = Modifier.size(tamanho * 0.5f)
        )
    }
}

@Composable
private fun Cartao(conteudo: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(CorCartao)
            .padding(20.dp),
        content = conteudo
    )
}

@Composable
private fun CabecalhoSecao(icone: ImageVector, titulo: String, comMenu: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icone, contentDescription = null, tint = CorTextoFraco, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = titulo,
            color = CorTextoFraco,
            fontSize = 17.sp,
            modifier = Modifier.weight(1f)
        )
        if (comMenu) {
            Icon(Icons.Filled.MoreHoriz, contentDescription = null, tint = CorTextoFraco)
        }
    }
}

@Composable
private fun Divisor() {
    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CorDivisor))
}

@Composable
private fun Chip(texto: String, destaque: Boolean = false) {
    Text(
        text = texto,
        color = if (destaque) Color.Black else Color.White,
        fontSize = 17.sp,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (destaque) CorChipDestaque else CorChip)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    )
}

@Composable
private fun LinhaInfo(icone: ImageVector, texto: String, comDivisor: Boolean = true) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icone, contentDescription = null, tint = CorTextoFraco, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Text(texto, color = Color.White, fontSize = 20.sp)
    }
    if (comDivisor) Divisor()
}

@Composable
private fun CampoComRotulo(
    rotulo: String,
    icone: ImageVector,
    valor: String,
    comDivisor: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
        Text(rotulo, color = CorTextoFraco, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icone, contentDescription = null, tint = CorTextoFraco, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(14.dp))
            Text(valor, color = Color.White, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (comDivisor) Divisor()
    }
}

@Composable
private fun BotaoResponderLinha(aoClicar: () -> Unit) {
    Spacer(modifier = Modifier.height(16.dp))
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .border(1.dp, Color(0xFF3A3A3A), RoundedCornerShape(50))
                .clickable { aoClicar() }
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = null,
                tint = CorAzul,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Responder", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun BotaoAcao(texto: String, cor: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(CorCartao)
            .clickable { onClick() }
            .padding(vertical = 26.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(texto, color = cor, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
    }
}