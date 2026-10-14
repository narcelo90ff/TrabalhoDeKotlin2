package com.example.myapplication.telas

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.componentes.BarraRolagem
import com.example.myapplication.dados.Carteira
import com.example.myapplication.navegacao.Rotas
import kotlin.math.roundToInt

/** Uma linha de preço: "5 Boosts - R$ 21,98/cada". */
private class OpcaoCompra(
    val quantidade: Int,
    val precoCada: Double,
    val rotulo: String? = null,
    val economia: String? = null
)

/** As cores de cada versão da tela (rosa para Boost, azul para Super Likes). */
private class TemaCompra(
    val fundoTopo: Color,
    val fundoBase: Color,
    val cartao: Color,
    val destaque: Color,
    val botao: Color
)

private val TemaBoost = TemaCompra(
    fundoTopo = Color(0xFF3A1D3F), fundoBase = Color(0xFF1E1021),
    cartao = Color(0xFF2B1D2E), destaque = Color(0xFFF2A8F2), botao = Color(0xFFEDEDED)
)
private val TemaSuperLike = TemaCompra(
    fundoTopo = Color(0xFF26304D), fundoBase = Color(0xFF151A2B),
    cartao = Color(0xFF212A42), destaque = Color(0xFF9DB4F0), botao = Color(0xFFDDE5F8)
)

private val opcoesBoost = listOf(
    OpcaoCompra(1, 43.90),
    OpcaoCompra(5, 21.98, "Mais popular", "Economize 50%"),
    OpcaoCompra(10, 16.99, "Melhor preço", "Economize 61%")
)
private val opcoesSuperLike = listOf(
    OpcaoCompra(3, 20.97),
    OpcaoCompra(15, 16.99, "Popular", "Economize 19%"),
    OpcaoCompra(30, 12.66, "Melhor preço", "Economize 40%")
)

private val abasBoost = listOf("Boost", "Primetime Boost", "Super Boost")
private val multiplicadorAba = listOf(1.0, 1.2, 1.5)
private val subtituloAba = listOf(
    "Seja um perfil de destaque na sua área por 30 minutos pra conseguir mais curtidas.",
    "Apareça nos horários de maior movimento pra conseguir mais curtidas.",
    "Seja um perfil de destaque numa área bem maior por 3 horas."
)

/**
 * Tela de compra de Boosts ou Super Likes (tipo = "BOOST" ou "SUPERLIKE").
 * Escolhe a quantidade, vê o total e "continua": a compra é simulada e
 * soma na Carteira, que aparece no Perfil.
 */
@Composable
fun TelaCompra(navController: NavController, tipo: String) {
    val ehBoost = tipo == "BOOST"
    val tema = if (ehBoost) TemaBoost else TemaSuperLike
    val opcoes = if (ehBoost) opcoesBoost else opcoesSuperLike

    var abaSelecionada by remember { mutableIntStateOf(0) }
    var escolhida by remember { mutableIntStateOf(0) }
    var mostrarConfirmacao by remember { mutableStateOf(false) }
    val estadoRolagem = rememberScrollState()

    // no Boost, cada aba (Boost / Primetime / Super Boost) muda o preço
    val multiplicador = if (ehBoost) multiplicadorAba[abaSelecionada] else 1.0
    fun precoDe(opcao: OpcaoCompra): Double = (opcao.precoCada * multiplicador * 100).roundToInt() / 100.0

    val opcao = opcoes[escolhida]
    val total = opcao.quantidade * precoDe(opcao)
    val nomeItem = if (ehBoost) "Boost" else "Super Like"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(tema.fundoTopo, tema.fundoBase)))
            .statusBarsPadding()
    ) {
        // ---- topo: X (e as abas no Boost) ----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF))
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Fechar", tint = Color.White)
            }
            if (ehBoost) {
                Spacer(modifier = Modifier.width(10.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0x33FFFFFF))
                        .padding(4.dp)
                ) {
                    abasBoost.forEachIndexed { indice, nome ->
                        Text(
                            text = nome,
                            color = if (indice == abaSelecionada) Color.Black else Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(if (indice == abaSelecionada) Color.White else Color.Transparent)
                                .clickable { abaSelecionada = indice }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // ---- conteúdo que rola ----
        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(estadoRolagem)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // desenho do topo: avatar com raio (Boost) ou estrela (Super Like)
                if (ehBoost) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(84.dp)
                                .clip(CircleShape)
                                .background(Brush.verticalGradient(listOf(Color(0xFFFA8393), Color(0xFFFF5A36)))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(46.dp))
                        }
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(tema.destaque),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Bolt, contentDescription = null, tint = Color(0xFF3A1D3F), modifier = Modifier.size(20.dp))
                        }
                    }
                } else {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFF6C8CFF), modifier = Modifier.size(92.dp))
                }

                Spacer(modifier = Modifier.height(14.dp))

                // título com a segunda palavra em itálico, como no vídeo
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, color = tema.destaque)) {
                            append(if (ehBoost) "Chame " else "Use ")
                        }
                        withStyle(
                            SpanStyle(
                                fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic, color = tema.destaque
                            )
                        ) {
                            append(if (ehBoost) "atenção" else "Super Likes")
                        }
                    },
                    fontSize = 38.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (ehBoost) subtituloAba[abaSelecionada]
                    else "Chame atenção com um Super Like e triplique as suas chances de dar matches!",
                    color = Color(0xFFD0C6D6),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                opcoes.forEachIndexed { indice, op ->
                    CartaoOpcaoCompra(
                        opcao = op,
                        preco = precoDe(op),
                        nomeItem = nomeItem,
                        selecionada = indice == escolhida,
                        tema = tema,
                        aoClicar = { escolhida = indice }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Text(
                    text = "ou",
                    color = Color(0xFFB8AEBD),
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                // convite para assinar o Gold
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(tema.cartao)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Assine o Gold", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = if (ehBoost) "Inclui 1 Boost grátis por mês" else "Inclui 2 Super Likes gratuitos",
                            color = Color(0xFFB8AEBD),
                            fontSize = 13.sp
                        )
                    }
                    Text(
                        text = "Selecionar",
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFEDEDED))
                            .clickable { navController.navigate(Rotas.assinatura("GOLD")) }
                            .padding(horizontal = 18.dp, vertical = 10.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            BarraRolagem(estado = estadoRolagem, modifier = Modifier.align(Alignment.CenterEnd))
        }

        // ---- botão fixo embaixo ----
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Button(
                onClick = { mostrarConfirmacao = true },
                colors = ButtonDefaults.buttonColors(containerColor = tema.botao, contentColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Continuar - ${formatarReais(total)} total", fontSize = 17.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (mostrarConfirmacao) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacao = false },
            title = { Text("Compra simulada") },
            text = {
                Text(
                    "Você comprou ${opcao.quantidade} ${if (ehBoost) "Boost(s)" else "Super Like(s)"} " +
                            "por ${formatarReais(total)}. É só um exemplo: nenhum pagamento de verdade foi feito."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (ehBoost) Carteira.boosts += opcao.quantidade
                    else Carteira.superLikes += opcao.quantidade
                    mostrarConfirmacao = false
                    navController.popBackStack()
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacao = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun CartaoOpcaoCompra(
    opcao: OpcaoCompra,
    preco: Double,
    nomeItem: String,
    selecionada: Boolean,
    tema: TemaCompra,
    aoClicar: () -> Unit
) {
    val forma = RoundedCornerShape(20.dp)
    val nomeQuantidade = "${opcao.quantidade} $nomeItem" + if (opcao.quantidade > 1) "s" else ""

    if (selecionada) {
        // selecionada: preenchida com a cor de destaque
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(forma)
                .background(tema.destaque)
                .clickable(onClick = aoClicar)
                .padding(horizontal = 18.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(nomeQuantidade, color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("${formatarReais(preco)}/cada", color = Color.Black, fontSize = 15.sp)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(forma)
                .background(tema.cartao)
                .clickable(onClick = aoClicar)
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            if (opcao.rotulo != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(opcao.rotulo, color = Color(0xFFB8AEBD), fontSize = 12.sp)
                    if (opcao.economia != null) {
                        Text(
                            text = opcao.economia,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color(0x33FFFFFF))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0x22FFFFFF))
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(nomeQuantidade, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("${formatarReais(preco)}/cada", color = Color.White, fontSize = 15.sp)
            }
        }
    }
}