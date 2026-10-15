package com.example.myapplication.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.dados.AppDados
import com.example.myapplication.navegacao.Rotas
import com.example.myapplication.componentes.BarraRolagem
import com.example.myapplication.dados.Carteira
import com.example.myapplication.dados.Preferencias

private val CorCartaoPerfil = Color(0xFF1E1E1E)
private val CorTextoFracoPerfil = Color(0xFFB5B5B5)
private val CorVerdeMusica = Color(0xFF3DAA5C)

private enum class AcaoCartao { EDITAR, ADICIONAR }

@Composable
fun TelaPerfil(navController: NavController) {
    val estadoRolagem = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(estadoRolagem)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 92.dp)
                        .rotate(-6f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Brush.verticalGradient(listOf(Color(0xFFFA8393), Color(0xFFFF5A36)))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(44.dp))
                }
                Spacer(modifier = Modifier.width(18.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Você",
                            color = Color.White,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = CorTextoFracoPerfil, modifier = Modifier.size(22.dp))
                    }
                    Text("Visualizar  ›", color = CorTextoFracoPerfil, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(104.dp))
            }

            Row(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ChipPerfil(Icons.Filled.Home, Preferencias.localAtual.substringBefore(","))
                ChipPerfil(Icons.Filled.Straighten, "1,70 m")
                ChipPerfil(Icons.Filled.Work, "Estudante")
                ChipPerfil(Icons.Filled.School, "Faculdade")
            }
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ChipPerfil(Icons.Filled.Info, "Mais informações (4/5)")
                ChipPerfil(Icons.Filled.LocalBar, "Estilo de vida (4/5)")
                ChipPerfil(Icons.Filled.Visibility, "Ainda estou decidindo")
            }

            TituloSecao("Minhas fotos", topo = 28.dp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(CorCartaoPerfil)
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                ) {
                    val coresFotos = listOf(
                        Color(0xFF8A5A44), Color(0xFF445A8A), Color(0xFF5A8A6A), Color(0xFF8A4458)
                    )
                    repeat(6) { posicao ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(88.dp)
                                .background(
                                    if (posicao < coresFotos.size) coresFotos[posicao]
                                    else if (posicao % 2 == 0) Color(0xFF151515) else Color(0xFF1B1B1B)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (posicao < coresFotos.size) {
                                Icon(Icons.Filled.Person, contentDescription = "Foto", tint = Color(0x99FFFFFF))
                            } else {
                                Icon(Icons.Filled.Add, contentDescription = "Adicionar foto", tint = Color.White)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Consiga até 2x mais curtidas com 6 fotos.",
                        color = Color.White,
                        fontSize = 17.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Editar",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.White)
                            .clickable { }
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                    )
                }
            }

            TituloSecao("Minhas perguntas", topo = 28.dp)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CartaoPerfilItem(
                    modifier = Modifier.weight(1f),
                    altura = 210.dp,
                    acao = AcaoCartao.EDITAR
                ) {
                    Text("Sobre mim", color = CorTextoFracoPerfil, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Olá! Me chamo Você, tenho 23 anos. Atualmente estudo e curto conhecer gente nova.",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        lineHeight = 28.sp,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                CartaoPerfilItem(
                    modifier = Modifier.weight(1f),
                    altura = 210.dp,
                    acao = AcaoCartao.EDITAR
                ) {
                    Text(
                        text = "O melhor primeiro encontro dessa cidade é...",
                        color = CorTextoFracoPerfil,
                        fontSize = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "O melhor seria num lugar calmo, com café e boa conversa.",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        lineHeight = 28.sp,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CartaoPerfilItem(
                    modifier = Modifier.weight(1f),
                    altura = 170.dp,
                    acao = AcaoCartao.ADICIONAR,
                    centralizado = true
                ) {
                    Icon(Icons.Filled.FormatQuote, contentDescription = null, tint = CorTextoFracoPerfil, modifier = Modifier.size(30.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Selecionar legenda", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Text("Responder à pergunta", color = CorTextoFracoPerfil, fontSize = 13.sp, textAlign = TextAlign.Center)
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            TituloSecao("Meus interesses", topo = 28.dp)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CartaoInteresse(
                    modifier = Modifier.weight(1f),
                    titulo = "Curtindo", subtitulo = "Churrasco, +4",
                    icone = Icons.Filled.Whatshot, emojis = "🍗💖"
                )
                CartaoInteresse(
                    modifier = Modifier.weight(1f),
                    titulo = "Assistindo", subtitulo = "Adicionar programas/séries",
                    icone = Icons.Filled.Tv, vazio = true
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CartaoInteresse(
                    modifier = Modifier.weight(1f),
                    titulo = "Cineminha", subtitulo = "Interestelar, +2",
                    icone = Icons.Filled.Movie, emojis = "🎬🍿"
                )
                CartaoInteresse(
                    modifier = Modifier.weight(1f),
                    titulo = "Torcendo por", subtitulo = "Meu time, +1",
                    icone = Icons.Filled.SportsSoccer, emojis = "⚽🏆"
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CartaoInteresse(
                    modifier = Modifier.weight(1f),
                    titulo = "Jogando", subtitulo = "Adicionar jogos",
                    icone = Icons.Filled.SportsEsports, vazio = true
                )
                CartaoInteresse(
                    modifier = Modifier.weight(1f),
                    titulo = "Ouvindo", subtitulo = "Escolha músicas",
                    icone = Icons.Filled.MusicNote, vazio = true, pontoVerde = true
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CartaoInteresse(
                    modifier = Modifier.weight(1f),
                    titulo = "Artistas preferidos", subtitulo = "Conectar música",
                    icone = Icons.Filled.LibraryMusic, vazio = true, pontoVerde = true
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF1A1A1A))
                    .clickable { navController.navigate(Rotas.CONFIGURACOES) }
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mais controles de perfil",
                    color = Color.White,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = CorTextoFracoPerfil)
            }

            Text(
                text = "Você tem ${AppDados.totalCurtidas} curtidas e " +
                        "${AppDados.totalConversas} conversas.",
                color = CorTextoFracoPerfil,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 8.dp, top = 16.dp)
            )

            Spacer(modifier = Modifier.height(210.dp))
        }

        // barra de rolagem que dá pra segurar e arrastar (acima do banner Gold)
        BarraRolagem(
            estado = estadoRolagem,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .statusBarsPadding()
                .padding(bottom = 190.dp)
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 43.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BotaoRedondoPerfil(Icons.Filled.Group, "Amigos") { }
            // abre a tela de Configurações de verdade
            BotaoRedondoPerfil(Icons.Filled.Settings, "Configurações") {
                navController.navigate(Rotas.CONFIGURACOES)
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xF2121212))))
                .padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color(0xFF8F6A00))
                    .clickable { navController.navigate(Rotas.CURTIDAS) { launchSingleTop = true } }
                    .padding(horizontal = 24.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("APP", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "GOLD",
                            color = Color(0xFF3A2A00),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFFE0B000))
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }
                    Text(
                        text = "Veja quem curtiu você. Dê match mais rápido.",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF1A1A1A))
                    .border(1.dp, Color(0xFF2E2E2E), RoundedCornerShape(50))
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ItemBeneficio(Icons.Filled.Star, Color(0xFF8FB0F0), "${Carteira.superLikes} Super Like") {
                    navController.navigate(Rotas.compra("SUPERLIKE"))
                }
                ItemBeneficio(Icons.Filled.Bolt, Color(0xFFD38CF0), "${Carteira.boosts} Boost") {
                    navController.navigate(Rotas.compra("BOOST"))
                }
                ItemBeneficio(
                    Icons.Filled.Whatshot,
                    Color(0xFFD32F2F),
                    Carteira.plano?.let { "Plano " + it.lowercase().replaceFirstChar { c -> c.uppercase() } }
                        ?: "Assinaturas"
                ) {
                    navController.navigate(Rotas.CONFIGURACOES)
                }
            }
        }
    }
}

@Composable
private fun TituloSecao(texto: String, topo: Dp) {
    Text(
        text = texto,
        color = Color.White,
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = 8.dp, top = topo, bottom = 12.dp)
    )
}

@Composable
private fun BotaoRedondoPerfil(icone: ImageVector, descricao: String, aoClicar: () -> Unit) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(Color(0xFF1A1A1A))
            .border(1.dp, Color(0xFF2E2E2E), CircleShape)
            .clickable(onClick = aoClicar),
        contentAlignment = Alignment.Center
    ) {
        Icon(icone, contentDescription = descricao, tint = Color.White, modifier = Modifier.size(24.dp))
    }
}

@Composable
private fun ChipPerfil(icone: ImageVector, texto: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFF1E1E1E))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icone, contentDescription = null, tint = CorTextoFracoPerfil, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(texto, color = Color.White, fontSize = 16.sp, maxLines = 1)
    }
}

@Composable
private fun CartaoPerfilItem(
    modifier: Modifier = Modifier,
    altura: Dp,
    acao: AcaoCartao,
    centralizado: Boolean = false,
    conteudo: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = modifier.height(altura)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(26.dp))
                .background(CorCartaoPerfil)
                .padding(18.dp),
            horizontalAlignment = if (centralizado) Alignment.CenterHorizontally else Alignment.Start,
            verticalArrangement = if (centralizado) Arrangement.Center else Arrangement.Top,
            content = conteudo
        )
        if (acao == AcaoCartao.EDITAR) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2E2E2E)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = Color.White, modifier = Modifier.size(16.dp))
            }
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = (-6).dp)
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar", tint = Color.Black, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
private fun CartaoInteresse(
    modifier: Modifier = Modifier,
    titulo: String,
    subtitulo: String,
    icone: ImageVector,
    emojis: String? = null,
    vazio: Boolean = false,
    pontoVerde: Boolean = false
) {
    CartaoPerfilItem(
        modifier = modifier,
        altura = 170.dp,
        acao = if (vazio) AcaoCartao.ADICIONAR else AcaoCartao.EDITAR,
        centralizado = vazio
    ) {
        if (vazio) {
            Icon(icone, contentDescription = null, tint = CorTextoFracoPerfil, modifier = Modifier.size(34.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(titulo, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                if (pontoVerde) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(CorVerdeMusica)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                }
                Text(subtitulo, color = CorTextoFracoPerfil, fontSize = 13.sp, textAlign = TextAlign.Center)
            }
        } else {
            Icon(icone, contentDescription = null, tint = CorTextoFracoPerfil, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(titulo, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Text(subtitulo, color = CorTextoFracoPerfil, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(10.dp))
            if (emojis != null) {
                Text(emojis, fontSize = 34.sp)
            }
        }
    }
}

@Composable
private fun ItemBeneficio(icone: ImageVector, cor: Color, texto: String, aoClicar: () -> Unit = { }) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable(onClick = aoClicar)
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icone, contentDescription = null, tint = cor, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(texto, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium, maxLines = 1)
    }
}