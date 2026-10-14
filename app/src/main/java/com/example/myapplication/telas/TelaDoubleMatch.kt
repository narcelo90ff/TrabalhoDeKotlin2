package com.example.myapplication.telas

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.dados.AbaDescobrir
import com.example.myapplication.dados.DescobrirEstado
import com.example.myapplication.navegacao.Rotas

@Composable
fun TelaDoubleDate(navController: NavController) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                // ao fechar, a aba Double Date fica selecionada no Descobrir
                DescobrirEstado.aba = AbaDescobrir.DOUBLE_DATE
                navController.popBackStack()
            }) {
                Icon(Icons.Filled.Close, contentDescription = "Fechar", tint = Color.White)
            }
            Text("Double Date", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { navController.navigate(Rotas.DOUBLE_DATE_CONFIG) }) {
                Icon(Icons.Filled.Settings, contentDescription = "Configurações", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Amigos no Double Date", color = Color.White, fontWeight = FontWeight.Bold)
            Text("0/3", color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1E1E1E))
                .clickable { compartilharConvite(context) }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3A3A3A)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Person, contentDescription = null, tint = Color.Gray)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Convidar um amigo", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.Filled.Add, contentDescription = "Adicionar", tint = Color.Gray)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Você pode formar dupla com até 3 amigos no Double Date.",
            color = Color.Gray,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🂡💕", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Convide amigos pra dar matches no Double Date.",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Você precisa convidar pelo menos um amigo pra dar matches no Double Date.",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { compartilharConvite(context) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Convidar amigos", fontWeight = FontWeight.Bold)
        }
    }
}

/** Abre a folha de compartilhamento do Android com o convite do Double Date. */
private fun compartilharConvite(context: Context) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Double Date")
        putExtra(Intent.EXTRA_TEXT, "Vem dar match comigo no Double Date! https://doubledate.app/convite")
    }
    context.startActivity(Intent.createChooser(intent, "Double Date"))
}