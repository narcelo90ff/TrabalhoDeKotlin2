package com.example.myapplication.navegacao

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.componentes.BottomNavBar
import com.example.myapplication.componentes.rotaTemBottomBar
import com.example.myapplication.telas.TelaConfiguracoes
import com.example.myapplication.telas.TelaDescobrir
import com.example.myapplication.telas.TelaDetalheConversa
import com.example.myapplication.telas.TelaDetalheCurtida
import com.example.myapplication.telas.TelaDoubleDate
import com.example.myapplication.telas.TelaExplorar
import com.example.myapplication.telas.TelaExplorarDetalhe
import com.example.myapplication.telas.TelaFiltros
import com.example.myapplication.telas.TelaListaConversas
import com.example.myapplication.telas.TelaListaCurtidas
import com.example.myapplication.telas.TelaPerfil

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rotaAtual = backStackEntry?.destination?.route

    Scaffold(
        containerColor = Color(0xFF121212),
        bottomBar = {
            // a barra inferior só aparece nas telas principais, não nas telas de detalhe
            if (rotaTemBottomBar(rotaAtual)) {
                BottomNavBar(navController = navController)
            }
        }
    ) { paddingExterno ->
        NavHost(
            navController = navController,
            startDestination = Rotas.DESCOBRIR,
            modifier = Modifier.padding(paddingExterno)
        ) {
            composable(Rotas.DESCOBRIR) {
                TelaDescobrir(navController = navController)
            }
            composable(Rotas.EXPLORAR) {
                TelaExplorar(navController = navController)
            }
            composable(
                route = Rotas.EXPLORAR_DETALHE,
                arguments = listOf(navArgument("categoria") { type = NavType.StringType })
            ) { entrada ->
                val categoria = entrada.arguments?.getString("categoria") ?: "Explorar"
                TelaExplorarDetalhe(navController = navController, categoria = categoria)
            }
            composable(Rotas.CURTIDAS) {
                TelaListaCurtidas(navController = navController)
            }
            composable(
                route = Rotas.DETALHE_CURTIDA,
                arguments = listOf(navArgument("pessoaId") { type = NavType.IntType })
            ) { entrada ->
                val pessoaId = entrada.arguments?.getInt("pessoaId") ?: -1
                TelaDetalheCurtida(navController = navController, pessoaId = pessoaId)
            }
            composable(Rotas.CONVERSAS) {
                TelaListaConversas(navController = navController)
            }
            composable(
                route = Rotas.DETALHE_CONVERSA,
                arguments = listOf(navArgument("conversaId") { type = NavType.IntType })
            ) { entrada ->
                val conversaId = entrada.arguments?.getInt("conversaId") ?: -1
                TelaDetalheConversa(navController = navController, conversaId = conversaId)
            }
            composable(Rotas.PERFIL) {
                TelaPerfil(navController = navController)
            }
            composable(Rotas.DOUBLE_DATE) {
                TelaDoubleDate(navController = navController)
            }
            composable(Rotas.CONFIGURACOES) {
                TelaConfiguracoes(navController = navController)
            }
            composable(Rotas.FILTROS) {
                TelaFiltros(navController = navController)
            }
        }
    }
}