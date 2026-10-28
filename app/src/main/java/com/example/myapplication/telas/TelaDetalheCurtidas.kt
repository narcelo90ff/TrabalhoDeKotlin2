@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.myapplication.telas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.data.repository.SocialRepositorio
import com.example.myapplication.modelo.Pessoa
import com.example.myapplication.navegacao.Rotas
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun TelaDetalheCurtida(navController: NavController, pessoaId: Int) {
    val contexto = LocalContext.current
    val repositorio = remember { SocialRepositorio.obter(contexto) }
    val escopo = rememberCoroutineScope()

    var pessoaCarregada by remember { mutableStateOf<Pessoa?>(null) }
    var carregando by remember { mutableStateOf(true) }
    LaunchedEffect(pessoaId) {
        pessoaCarregada = repositorio.buscarPessoa(pessoaId)
        carregando = false
    }
    val pessoa = pessoaCarregada

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pessoa?.nome ?: "Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        if (pessoa == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                if (!carregando) Text("Essa pessoa não existe mais.")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(text = "${pessoa.nome}, ${pessoa.idade} anos", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = pessoa.bio)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Compatibilidade estimada: ${calcularCompatibilidade(pessoa.idade)}%")

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                escopo.launch {
                    repositorio.iniciarConversaComPessoa(pessoa)?.let { conversa ->
                        navController.navigate(Rotas.detalheConversa(conversa.id))
                    }
                }
            }) {
                Text("Iniciar conversa")
            }
        }
    }
}

private fun calcularCompatibilidade(idade: Int): Int {
    val base = 100 - abs(idade - 25) * 2
    return base.coerceIn(40, 99)
}