package com.example.myapplication.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.componentes.BarraRolagem
import com.example.myapplication.navegacao.Rotas


@Composable
fun TelaFiltros(navController: NavController) {
    var feedback by remember { mutableStateOf<String?>(null) }
    val estadoRolagem = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CfgFundo)
            .statusBarsPadding()
    ) {
        CabecalhoFiltros(aoConfirmar = { navController.popBackStack() })

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(estadoRolagem)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                CartaoFeedback(escolha = feedback, aoEscolher = { feedback = it })

                SecaoDescoberta(aoAbrirLocalizacao = { navController.navigate(Rotas.LOCALIZACAO) })
                SecaoDescobertaPremium()

                Spacer(modifier = Modifier.navigationBarsPadding().height(32.dp))
            }
            BarraRolagem(estado = estadoRolagem, modifier = Modifier.align(Alignment.CenterEnd))
        }
    }
}

@Composable
private fun CabecalhoFiltros(aoConfirmar: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Preferências de descoberta",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable(onClick = aoConfirmar),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Check, contentDescription = "Concluir", tint = Color.Black)
        }
    }
}

@Composable
private fun CartaoFeedback(escolha: String?, aoEscolher: (String) -> Unit) {
    GrupoCartao {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Como andam suas recomendações ultimamente?",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (escolha == null) "Seu feedback ajuda a melhorar seu feed."
                else "Obrigado pelo feedback!",
                color = CfgTextoFraco,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BotaoFeedback(
                    texto = "Nada bom",
                    icone = Icons.Filled.Close,
                    selecionado = escolha == "Nada bom",
                    aoClicar = { aoEscolher("Nada bom") },
                    modifier = Modifier.weight(1f)
                )
                BotaoFeedback(
                    texto = "Bom",
                    icone = Icons.Filled.Check,
                    selecionado = escolha == "Bom",
                    aoClicar = { aoEscolher("Bom") },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BotaoFeedback(
    texto: String,
    icone: ImageVector,
    selecionado: Boolean,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(50)
    Row(
        modifier = modifier
            .clip(forma)
            .background(if (selecionado) Color(0xFF2E2E2E) else Color(0xFF222222))
            .border(1.dp, if (selecionado) Color.White else CfgDivisor, forma)
            .clickable(onClick = aoClicar)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icone, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(texto, color = Color.White, fontSize = 14.sp)
    }
}