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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.componentes.BarraRolagem
import com.example.myapplication.dados.banco.LojaRepositorio
import com.example.myapplication.dados.banco.TipoCompra
import com.example.myapplication.navegacao.Rotas
import java.util.Locale
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.math.roundToLong

private const val NOME_APP = "TINDER"
private val FundoAssinatura = Color(0xFF111111)

internal fun formatarReais(valor: Double): String =
    "R$ " + String.format(Locale("pt", "BR"), "%.2f", valor)

private class OpcaoPlano(val nome: String, val semanas: Int, val precoSemana: Double)

private data class DadosPlano(
    val planoSalvo: String,
    val nomeSelo: String,
    val corSelo: Color,
    val corTextoSelo: Color,
    val destaque: Color,
    val titulo: String,
    val opcoes: List<OpcaoPlano>,
    val beneficios: List<String>
)

private fun dadosDoPlano(chave: String): DadosPlano {
    val plus = DadosPlano(
        planoSalvo = "PLUS", nomeSelo = "PLUS",
        corSelo = CfgVermelho, corTextoSelo = Color.White, destaque = CfgVermelho,
        titulo = "Curtidas ilimitadas. \"Voltar\" ilimitado. Modo Passaporte ilimitado. Sem anúncios.",
        opcoes = listOf(
            OpcaoPlano("1 semana", 1, 11.50),
            OpcaoPlano("1 mês", 4, 5.75),
            OpcaoPlano("6 meses", 24, 3.90)
        ),
        beneficios = listOf(
            "Curtidas ilimitadas",
            "Use o Voltar quantas vezes quiser",
            "Modo Passaporte ilimitado",
            "Sem anúncios"
        )
    )
    return when (chave) {
        "PLATINUM" -> DadosPlano(
            planoSalvo = "PLATINUM", nomeSelo = "PLATINUM",
            corSelo = CfgPlatina, corTextoSelo = Color.Black, destaque = Color(0xFFE0E0E0),
            titulo = "Dê um upgrade nas suas curtidas e Super Likes com o Platinum.",
            opcoes = listOf(
                OpcaoPlano("1 semana", 1, 46.99),
                OpcaoPlano("1 mês", 4, 23.22),
                OpcaoPlano("6 meses", 24, 14.99)
            ),
            beneficios = listOf(
                "Curtidas ilimitadas",
                "Veja quem curtiu você",
                "Curtidas prioritárias",
                "Mensagem antes do match",
                "Modo Passaporte ilimitado"
            )
        )
        "GOLD" -> DadosPlano(
            planoSalvo = "GOLD", nomeSelo = "GOLD",
            corSelo = CfgDourado, corTextoSelo = Color.Black, destaque = CfgDourado,
            titulo = "Use o Gold™, veja quem curtiu você e dê match instantaneamente.",
            opcoes = listOf(
                OpcaoPlano("1 semana", 1, 18.00),
                OpcaoPlano("1 mês", 4, 9.00),
                OpcaoPlano("6 meses", 24, 6.50)
            ),
            beneficios = listOf(
                "Curtidas ilimitadas",
                "Veja quem curtiu você",
                "1 Boost grátis por mês",
                "Use o Voltar quantas vezes quiser"
            )
        )
        "ANONIMO" -> plus.copy(titulo = "Mostre seu perfil somente pra pessoas que você curtiu.")
        else -> plus
    }
}


@Composable
fun TelaAssinatura(navController: NavController, plano: String) {
    val dados = remember(plano) { dadosDoPlano(plano) }
    var escolhida by remember { mutableIntStateOf(0) }
    var criandoCompra by remember { mutableStateOf(false) }
    val contexto = LocalContext.current
    val escopo = rememberCoroutineScope()
    val estadoRolagem = rememberScrollState()

    val opcao = dados.opcoes[escolhida]
    val total = opcao.semanas * opcao.precoSemana

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoAssinatura)
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2A2A2A))
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Fechar", tint = Color.White)
            }
            Row(
                modifier = Modifier.align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(NOME_APP, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Selo(dados.nomeSelo, dados.corSelo, dados.corTextoSelo)
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(estadoRolagem)
            ) {
                Text(
                    text = dados.titulo,
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 32.sp,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                )
                Text(
                    text = "Escolha um plano",
                    color = Color.White,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(start = 20.dp, top = 8.dp, bottom = 12.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(dados.opcoes) { indice, op ->
                        CartaoOpcaoPlano(
                            opcao = op,
                            rotulo = rotuloDaOpcao(indice, op, dados.opcoes.first()),
                            selecionada = indice == escolhida,
                            destaque = dados.destaque,
                            aoClicar = { escolhida = indice }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    dados.opcoes.indices.forEach { indice ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (indice == escolhida) Color.White else Color(0xFF555555))
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, CfgDivisor, RoundedCornerShape(20.dp))
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Incluído com $NOME_APP ${dados.nomeSelo.lowercase().replaceFirstChar { it.uppercase() }}",
                        color = CfgTextoFraco,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    dados.beneficios.forEach { beneficio ->
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(18.dp))
                            Text(beneficio, color = Color.White, fontSize = 16.sp)
                        }
                    }
                }
            }
            BarraRolagem(estado = estadoRolagem, modifier = Modifier.align(Alignment.CenterEnd))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0C0C0C))
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    append(
                        "Cobrança recorrente. Cancele a qualquer hora.\n" +
                                "Ao tocar em Continuar, o pagamento será cobrado na sua conta da loja de aplicativos, " +
                                "e sua assinatura será renovada automaticamente, durante o período selecionado " +
                                "para o pacote e pelo mesmo valor, até que você efetue o cancelamento nas " +
                                "configurações da loja e concorde com nossos "
                    )
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) { append("Termos") }
                    append(".")
                },
                color = CfgTextoFraco,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    if (!criandoCompra) {
                        criandoCompra = true
                        escopo.launch {
                            val nomePlano = dados.nomeSelo.lowercase().replaceFirstChar { it.uppercase() }
                            val compraId = LojaRepositorio.obter(contexto).criarCompra(
                                tipo = TipoCompra.PLANO,
                                item = "Plano $nomePlano (${opcao.nome})",
                                quantidade = 0,
                                plano = dados.planoSalvo,
                                valorCentavos = (total * 100).roundToLong()
                            )
                            criandoCompra = false
                            navController.navigate(Rotas.pagamento(compraId))
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDEDED), contentColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Continuar - ${formatarReais(total)} total", fontSize = 17.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun rotuloDaOpcao(indice: Int, opcao: OpcaoPlano, primeira: OpcaoPlano): String {
    if (indice == 0) return "Popular"
    val economia = ((1 - opcao.precoSemana / primeira.precoSemana) * 100).roundToInt()
    return if (indice == 2) "Melhor preço" else "Economize $economia%"
}

@Composable
private fun CartaoOpcaoPlano(
    opcao: OpcaoPlano,
    rotulo: String,
    selecionada: Boolean,
    destaque: Color,
    aoClicar: () -> Unit
) {
    val forma = RoundedCornerShape(20.dp)
    Column(
        modifier = Modifier
            .width(230.dp)
            .height(190.dp)
            .clip(forma)
            .border(if (selecionada) 2.dp else 1.dp, if (selecionada) destaque else CfgDivisor, forma)
            .clickable(onClick = aoClicar)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(rotulo, color = if (selecionada) destaque else CfgTextoFraco, fontSize = 14.sp)
            if (selecionada) {
                Icon(Icons.Filled.Check, contentDescription = "Selecionado", tint = destaque)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(opcao.nome, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.weight(1f))
        Text("${formatarReais(opcao.precoSemana)}/semana", color = Color.White, fontSize = 16.sp)
    }
}