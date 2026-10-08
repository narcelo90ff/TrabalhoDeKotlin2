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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.dados.AppDados
import com.example.myapplication.navegacao.Rotas
import kotlin.math.abs

@Composable
fun TelaDetalheCurtida(navController: NavController, pessoaId: Int) {
    val pessoa = AppDados.buscarPessoa(pessoaId)

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
                Text("Essa pessoa não existe mais.")
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
                val conversa = AppDados.iniciarConversaComPessoa(pessoa)
                navController.navigate(Rotas.detalheConversa(conversa.id))
            }) {
                Text("Iniciar conversa")
            }
        }
    }
}

/** Cálculo fictício só paa exemplo didático, não é ciência nenhuma. */
private fun calcularCompatibilidade(idade: Int): Int {
    val base = 100 - abs(idade - 25) * 2
    return base.coerceIn(40, 99)
}