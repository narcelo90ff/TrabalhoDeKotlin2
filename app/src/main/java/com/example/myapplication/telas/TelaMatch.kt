package com.example.myapplication.telas

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myapplication.modelo.Perfil
import com.example.myapplication.modelo.Pessoa
import kotlinx.coroutines.launch

const val ID_PESSOA_MATCH = 3

data class DadosMatch(
    val perfil: Perfil,
    val pessoa: Pessoa?
)

private val VerdeMatch = Color(0xFF2EF2B3)
private val AzulEnviar = Color(0xFF21AEE8)

@Composable
fun TelaMatch(
    perfil: Perfil,
    aoFechar: () -> Unit,
    aoEnviar: (String) -> Unit
) {
    Dialog(
        onDismissRequest = aoFechar,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        ConteudoMatch(perfil, aoFechar, aoEnviar)
    }
}

@Composable
private fun ConteudoMatch(
    perfil: Perfil,
    aoFechar: () -> Unit,
    aoEnviar: (String) -> Unit
) {
    var texto by remember { mutableStateOf("") }

    val escala = remember { Animatable(0.3f) }
    val transparencia = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        launch { transparencia.animateTo(1f, tween(350)) }
        escala.animateTo(
            1f,
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
        )
    }

    val legenda = "${perfil.nome} curtiu você também!"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = perfil.foto),
            contentDescription = "Foto de ${perfil.nome}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .align(Alignment.TopCenter)
                .background(Brush.verticalGradient(listOf(Color(0x99000000), Color.Transparent)))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .align(Alignment.BottomCenter)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xE6000000))))
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(12.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0x66000000))
                .clickable(onClick = aoFechar),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Close, contentDescription = "Continuar deslizando", tint = Color.White)
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    scaleX = escala.value
                    scaleY = escala.value
                    alpha = transparencia.value
                },
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "DEU",
                color = VerdeMatch,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(end = 12.dp)
            )
            Box(modifier = Modifier.padding(bottom = 44.dp)) {
                Text(
                    text = "MATCH!",
                    style = TextStyle(
                        color = VerdeMatch,
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        letterSpacing = (-2).sp,
                        drawStyle = Stroke(width = 3f)
                    ),
                    modifier = Modifier
                        .offset(y = 46.dp)
                        .alpha(0.6f)
                )
                Text(
                    text = "MATCH!",
                    color = VerdeMatch,
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = (-2).sp
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Filled.Favorite,
                contentDescription = null,
                tint = VerdeMatch,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = legenda,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(18.dp))

            val podeEnviar = texto.isNotBlank()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BasicTextField(
                    value = texto,
                    onValueChange = { texto = it },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                    cursorBrush = SolidColor(Color.Black),
                    modifier = Modifier.weight(1f),
                    decorationBox = { campo ->
                        Box {
                            if (texto.isEmpty()) {
                                Text("Diga algo legal", color = Color(0xFF9A9A9A), fontSize = 16.sp)
                            }
                            campo()
                        }
                    }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "ENVIAR",
                    color = AzulEnviar,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .alpha(if (podeEnviar) 1f else 0.4f)
                        .clickable(enabled = podeEnviar) { aoEnviar(texto.trim()) }
                )
            }
        }
    }
}