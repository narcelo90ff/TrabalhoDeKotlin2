package com.example.myapplication.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Straighten
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.dados.AppDados
import com.example.myapplication.navegacao.Rotas

private val CorCartaoPerfil = Color(0xFF1E1E1E)
private val CorTextoFracoPerfil = Color(0xFFB5B5B5)

@Composable
fun TelaPerfil(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // ---- cabeçalho: foto, nome e botões ----
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
            BotaoRedondoPerfil(Icons.Filled.Group, "Amigos") { }
            Spacer(modifier = Modifier.width(8.dp))
            // abre a tela de Configurações de verdade
            BotaoRedondoPerfil(Icons.Filled.Settings, "Configurações") {
                navController.navigate(Rotas.CONFIGURACOES)
            }
        }

        // ---- chips de informações (rolam para o lado) ----
        Row(
            modifier = Modifier
                .padding(top = 24.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ChipPerfil(Icons.Filled.Home, "São Paulo")
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

        // ---- minhas fotos ----
        Text(
            text = "Minhas fotos",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 8.dp, top = 28.dp, bottom = 12.dp)
        )
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
                repeat(6) { posicao ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(88.dp)
                            .background(if (posicao % 2 == 0) Color(0xFF151515) else Color(0xFF1B1B1B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Adicionar foto", tint = Color.White)
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
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                )
            }
        }

        // ---- minhas perguntas ----
        Text(
            text = "Minhas perguntas",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 8.dp, top = 28.dp, bottom = 12.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CartaoPergunta(
                pergunta = "Sobre mim",
                resposta = "Olá! Me chamo Você, tenho 23 anos. Atualmente estudo e curto conhecer gente nova.",
                modifier = Modifier.weight(1f)
            )
            CartaoPergunta(
                pergunta = "O melhor primeiro encontro dessa cidade é...",
                resposta = "O melhor seria num lugar calmo, com café e boa conversa.",
                modifier = Modifier.weight(1f)
            )
        }

        // ---- banner Gold (leva para as Curtidas) ----
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(40.dp))
                .background(Color(0xFF8F6A00))
                .clickable { navController.navigate(Rotas.CURTIDAS) { launchSingleTop = true } }
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("APP", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black)
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
                    fontSize = 15.sp
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(34.dp)
            )
        }

        // ---- super like / boost / assinaturas ----
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .background(Color(0xFF1A1A1A))
                .border(1.dp, Color(0xFF2E2E2E), RoundedCornerShape(50))
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemBeneficio(Icons.Filled.Star, Color(0xFF8FB0F0), "0 Super Like")
            ItemBeneficio(Icons.Filled.Bolt, Color(0xFFD38CF0), "0 Boost")
            ItemBeneficio(Icons.Filled.Whatshot, Color(0xFFD32F2F), "Assinaturas")
        }

        // dado real, vindo da mesma fonte de dados usada nas outras telas
        Text(
            text = "Você tem ${AppDados.curtidas.size} curtidas e " +
                    "${AppDados.conversas.size} conversas.",
            color = CorTextoFracoPerfil,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp, top = 20.dp, bottom = 24.dp)
        )
    }
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
private fun CartaoPergunta(pergunta: String, resposta: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .height(210.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(CorCartaoPerfil)
            .padding(20.dp)
    ) {
        Text(
            text = pergunta,
            color = CorTextoFracoPerfil,
            fontSize = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = resposta,
            color = Color.White,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            lineHeight = 30.sp,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ItemBeneficio(icone: ImageVector, cor: Color, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icone, contentDescription = null, tint = cor, modifier = Modifier.size(26.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(texto, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium, maxLines = 1)
    }
}