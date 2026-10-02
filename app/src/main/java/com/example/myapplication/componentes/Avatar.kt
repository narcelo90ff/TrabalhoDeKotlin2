package com.example.myapplication.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Mostra a foto da pessoa; se ela não tiver foto, mostra um fundo colorido
 * com a inicial do nome.
 */
@Composable
fun FotoOuInicial(
    foto: Int?,
    nome: String,
    modifier: Modifier = Modifier,
    forma: Shape = RectangleShape,
    tamanhoLetra: TextUnit = 28.sp
) {
    val base = modifier.clip(forma)
    if (foto != null) {
        Image(
            painter = painterResource(id = foto),
            contentDescription = nome,
            contentScale = ContentScale.Crop,
            modifier = base
        )
    } else {
        Box(
            modifier = base.background(
                Brush.verticalGradient(listOf(Color(0xFFFA8393), Color(0xFFFF5A36)))
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = nome.take(1).uppercase(),
                color = Color.White,
                fontSize = tamanhoLetra,
                fontWeight = FontWeight.Bold
            )
        }
    }
}