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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.componentes.BarraRolagem
import com.example.myapplication.dados.Preferencias

/**
 * Localização (Modo Passaporte): escolhe de qual cidade você quer dar matches
 * ou adiciona um local novo. A escolha aparece nas Configurações e no Perfil.
 */
@Composable
fun TelaLocalizacao(navController: NavController) {
    var mostrarDialogo by remember { mutableStateOf(false) }
    val estadoLista = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E0E0E))
            .statusBarsPadding()
    ) {
        // ---- topo: voltar + título ----
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E1E1E))
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
            }
            Text(
                text = "Localização",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Text(
            text = "Localização atual",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 20.dp, top = 12.dp, bottom = 8.dp)
        )

        // ---- lista de locais (toque escolhe; o X remove) ----
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                state = estadoLista,
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(Preferencias.locais, key = { it }) { local ->
                    val atual = local == Preferencias.localAtual
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF1A1A1A))
                            .clickable { Preferencias.localAtual = local }
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = CfgVermelho, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(local, color = Color.White, fontSize = 16.sp, modifier = Modifier.weight(1f))
                        if (atual) {
                            Icon(Icons.Filled.Check, contentDescription = "Local atual", tint = CfgVermelho)
                        } else {
                            // só dá pra remover quem não é o local atual
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Remover local",
                                tint = CfgTextoFraco,
                                modifier = Modifier.clickable { Preferencias.locais.remove(local) }
                            )
                        }
                    }
                }
            }
            BarraRolagem(estado = estadoLista, modifier = Modifier.align(Alignment.CenterEnd))
        }

        // ---- botão fixo embaixo ----
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Button(
                onClick = { mostrarDialogo = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F1F1), contentColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Adicionar local novo", fontSize = 17.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (mostrarDialogo) {
        DialogNovoLocal(
            aoFechar = { mostrarDialogo = false },
            aoAdicionar = { nome ->
                val limpo = nome.trim()
                if (limpo !in Preferencias.locais) Preferencias.locais.add(limpo)
                Preferencias.localAtual = limpo
                mostrarDialogo = false
            }
        )
    }
}

@Composable
private fun DialogNovoLocal(aoFechar: () -> Unit, aoAdicionar: (String) -> Unit) {
    var nome by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = aoFechar,
        title = { Text("Adicionar local novo") },
        text = {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Cidade, UF") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { if (nome.isNotBlank()) aoAdicionar(nome) }) { Text("Adicionar") }
        },
        dismissButton = {
            TextButton(onClick = aoFechar) { Text("Cancelar") }
        }
    )
}