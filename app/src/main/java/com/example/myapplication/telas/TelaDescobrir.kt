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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FormatQuote
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.dados.AppDados
import com.example.myapplication.modelo.Perfil
import com.example.myapplication.navegacao.Rotas

private val CorCartao = Color(0xFF1A1A1A)
private val CorChip = Color(0xFF0B0B0B)
private val CorChipDestaque = Color(0xFFF4A6E0)
private val CorDivisor = Color(0xFF333333)
private val CorTextoFraco = Color(0xFFB5B5B5)
private val CorAzul = Color(0xFF8FB0F0)
private val CorBotao = Color(0xFF1E1E1E)

@Composable
fun TelaDescobrir(navController: NavController) {
    // qual perfil está na tela e se a tela de detalhes (seta pra cima) está aberta
    var indice by rememberSaveable { mutableIntStateOf(0) }
    var mostrandoDetalhe by rememberSaveable { mutableStateOf(false) }
    val perfil = AppDados.perfis.getOrNull(indice)

    if (perfil == null) {
        TelaSemPerfis(onRecomecar = { indice = 0 })
        return
    }

    fun proximo() {
        mostrandoDetalhe = false
        indice++
    }

    // interação de verdade: curtir adiciona a pessoa na lista de Curtidas
    val aoPassar: () -> Unit = { proximo() }
    val aoCurtir: () -> Unit = {
        AppDados.curtir(perfil)
        proximo()
    }
    val aoFavoritar: () -> Unit = {
        AppDados.curtir(perfil, favorito = true)
        proximo()
    }
    val aoVoltar: () -> Unit = {
        if (indice > 0) {
            mostrandoDetalhe = false
            indice--
        }
    }
    // curtir + abrir a conversa com a pessoa
    val aoResponder: () -> Unit = {
        val pessoa = AppDados.curtir(perfil)
        val conversa = AppDados.iniciarConversaComPessoa(pessoa)
        navController.navigate(Rotas.detalheConversa(conversa.id))
    }

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
            aoAbrirDetalhe = { mostrandoDetalhe = true },
            aoDoubleDate = { navController.navigate(Rotas.DOUBLE_DATE) },
            aoVoltar = aoVoltar,
            aoPassar = aoPassar,
            aoFavoritar = aoFavoritar,
            aoCurtir = aoCurtir,
            aoResponder = aoResponder
        )
    }
}

// ---------------------------------------------------------------------------
// Tela 1: foto cheia
// ---------------------------------------------------------------------------

@Composable
private fun FotoPerfil(
    perfil: Perfil,
    aoAbrirDetalhe: () -> Unit,
    aoDoubleDate: () -> Unit,
    aoVoltar: () -> Unit,
    aoPassar: () -> Unit,
    aoFavoritar: () -> Unit,
    aoCurtir: () -> Unit,
    aoResponder: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(28.dp))
    ) {
        Image(
            painter = painterResource(id = perfil.foto),
            contentDescription = "Foto de ${perfil.nome}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // sombra em cima (abas) e embaixo (nome e botões) para o texto ler bem
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
                .height(320.dp)
                .align(Alignment.BottomCenter)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xE6000000))))
        )

        // abas do topo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(Icons.Filled.Tune, contentDescription = "Filtros", tint = Color.White, modifier = Modifier.size(28.dp))
            Text(
                text = "Pra você",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0x66000000))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            )
            Text(
                text = "Double Date",
                color = Color.White,
                fontSize = 15.sp,
                modifier = Modifier.clickable { aoDoubleDate() }
            )
            Text(text = "Astrologia", color = Color.White, fontSize = 15.sp)
            Icon(Icons.Filled.Bolt, contentDescription = null, tint = Color(0xFFD38CF0), modifier = Modifier.size(26.dp))
        }

        // nome, distância, seta pra cima e botões
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(perfil.nome, color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(perfil.idade.toString(), color = Color.White, fontSize = 30.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(perfil.distancia, color = Color.White, fontSize = 16.sp)
                    }
                }
                // a setinha pra cima: abre as informações do perfil
                BotaoCirculo(
                    icone = Icons.Filled.ArrowUpward,
                    tamanho = 52.dp,
                    fundo = Color.White,
                    corIcone = Color.Black,
                    descricao = "Ver perfil completo",
                    aoClicar = aoAbrirDetalhe
                )
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

// ---------------------------------------------------------------------------
// Tela 2: informações do perfil (abre com a setinha pra cima)
// ---------------------------------------------------------------------------

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
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF111111))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 12.dp)
        ) {
            // cabeçalho com nome, idade e a setinha pra baixo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
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

            // Tô procurando
            Cartao {
                CabecalhoSecao(Icons.Filled.Search, perfil.procurando, comMenu = false)
                Spacer(modifier = Modifier.height(12.dp))
                Text(perfil.tipoRelacionamento, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(14.dp))
                Divisor()
                Spacer(modifier = Modifier.height(14.dp))
                Chip("Monogamia")
            }

            // Sobre mim
            Cartao {
                CabecalhoSecao(Icons.Filled.FormatQuote, "Sobre mim", comMenu = true)
                Spacer(modifier = Modifier.height(12.dp))
                Text(perfil.sobreMim, color = Color.White, fontSize = 20.sp)
                BotaoResponderLinha(aoResponder)
            }

            // Informações básicas
            Cartao {
                CabecalhoSecao(Icons.Filled.Person, "Informações básicas", comMenu = true)
                Spacer(modifier = Modifier.height(12.dp))
                LinhaInfo(Icons.Filled.LocationOn, perfil.distancia)
                LinhaInfo(Icons.Filled.Straighten, perfil.altura)
                LinhaInfo(Icons.Filled.School, perfil.escolaridade)
                LinhaInfo(Icons.Filled.Person, perfil.genero, comDivisor = false)
                BotaoResponderLinha(aoResponder)
            }

            // Pergunta e resposta
            Cartao {
                CabecalhoSecao(Icons.Filled.FormatQuote, perfil.pergunta, comMenu = true)
                Spacer(modifier = Modifier.height(12.dp))
                Text(perfil.resposta, color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold, lineHeight = 36.sp)
                BotaoResponderLinha(aoResponder)
            }

            // Estilo de vida
            Cartao {
                CabecalhoSecao(Icons.Filled.LocalOffer, "Estilo de vida", comMenu = false)
                Spacer(modifier = Modifier.height(12.dp))
                CampoComRotulo("Bebida", Icons.Filled.LocalBar, perfil.bebida)
                CampoComRotulo("Você fuma?", Icons.Filled.SmokingRooms, perfil.fuma)
                CampoComRotulo("Atividade física", Icons.Filled.FitnessCenter, perfil.atividadeFisica)
                CampoComRotulo("Pets", Icons.Filled.Pets, perfil.pets, comDivisor = false)
                BotaoResponderLinha(aoResponder)
            }

            // Mais informações
            Cartao {
                CabecalhoSecao(Icons.Filled.LocalOffer, "Mais informações", comMenu = false)
                Spacer(modifier = Modifier.height(12.dp))
                CampoComRotulo("Estilo de comunicação", Icons.Filled.Call, perfil.comunicacao)
                CampoComRotulo("Signo", Icons.Filled.NightsStay, perfil.signo, comDivisor = false)
                BotaoResponderLinha(aoResponder)
            }

            // Interesses (os 2 primeiros em rosa = em comum com você)
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

        // botões fixos embaixo (não curtir, favoritar, curtir)
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

// ---------------------------------------------------------------------------
// Tela quando acabam os perfis
// ---------------------------------------------------------------------------

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

// ---------------------------------------------------------------------------
// Pedacinhos reutilizáveis
// ---------------------------------------------------------------------------

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