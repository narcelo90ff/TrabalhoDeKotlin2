package com.example.myapplication.telas

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.componentes.BarraRolagem
import com.example.myapplication.ui.local.LocalViewModel
import com.example.myapplication.ui.local.LocalViewModelFactory
import com.example.myapplication.data.local.LocalEntity
import com.example.myapplication.data.repository.LocalRepositorio

// ╔══════════════════════════════════════════════════════════╗
// ║  TelaLocalizacao — a View (MVVM)                         ║
// ║  Antes: 5 variáveis remember + CoroutineScope + acesso   ║
// ║  direto ao Repository dentro da tela.                    ║
// ║  Agora: só lê o uiState e avisa o ViewModel.             ║
// ╚══════════════════════════════════════════════════════════╝
@Composable
fun TelaLocalizacao(
    navController: NavController,
    // Monta o ViewModel injetando o Repository pela Factory (igual à aula)
    viewModel: LocalViewModel = viewModel(
        factory = LocalViewModelFactory(
            LocalRepositorio.obter(LocalContext.current)
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val estadoLista = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E0E0E))
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

        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                state = estadoLista,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.locais, key = { it.id }) { local ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF1A1A1A))
                            .clickable { viewModel.onEscolher(local) }
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = CfgVermelho, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(local.nome, color = Color.White, fontSize = 16.sp, modifier = Modifier.weight(1f))

                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Editar local",
                            tint = CfgTextoFraco,
                            modifier = Modifier
                                .size(22.dp)
                                .clickable { viewModel.onEditar(local) }
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        if (local.atual) {
                            Icon(Icons.Filled.Check, contentDescription = "Local atual", tint = CfgVermelho)
                        } else {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Remover local",
                                tint = CfgTextoFraco,
                                modifier = Modifier.clickable { viewModel.onRemover(local) }
                            )
                        }
                    }
                }
            }
            BarraRolagem(estado = estadoLista, modifier = Modifier.align(Alignment.CenterEnd))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Button(
                onClick = { viewModel.onAbrirNovo() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F1F1), contentColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Adicionar local novo", fontSize = 17.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (uiState.mostrarDialogoNovo) {
        DialogLocal(
            titulo = "Adicionar local novo",
            textoInicial = "",
            botao = "Adicionar",
            erro = null,
            aoFechar = { viewModel.onFecharNovo() },
            aoConfirmar = { nome -> viewModel.onAdicionar(nome) }
        )
    }

    uiState.localEmEdicao?.let { local ->
        DialogLocal(
            titulo = "Editar local",
            textoInicial = local.nome,
            botao = "Salvar",
            erro = uiState.erroEdicao,
            aoFechar = { viewModel.onCancelarEdicao() },
            aoConfirmar = { nome -> viewModel.onSalvarEdicao(nome) }
        )
    }
}

@Composable
private fun DialogLocal(
    titulo: String,
    textoInicial: String,
    botao: String,
    erro: String?,
    aoFechar: () -> Unit,
    aoConfirmar: (String) -> Unit
) {
    var nome by remember { mutableStateOf(textoInicial) }

    AlertDialog(
        onDismissRequest = aoFechar,
        title = { Text(titulo) },
        text = {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Cidade, UF") },
                singleLine = true,
                isError = erro != null,
                supportingText = { if (erro != null) Text(erro) }
            )
        },
        confirmButton = {
            TextButton(onClick = { if (nome.isNotBlank()) aoConfirmar(nome) }) { Text(botao) }
        },
        dismissButton = {
            TextButton(onClick = aoFechar) { Text("Cancelar") }
        }
    )
}