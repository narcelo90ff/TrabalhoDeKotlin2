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
import com.example.myapplication.telas.TelaAstrologia
import com.example.myapplication.telas.TelaConfiguracoes
import com.example.myapplication.telas.TelaDoubleDateConfig
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
import com.example.myapplication.telas.TelaAssinatura
import com.example.myapplication.telas.TelaCompra
import com.example.myapplication.telas.TelaLocalizacao
import com.example.myapplication.telas.TelaPagamento

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rotaAtual = backStackEntry?.destination?.route

    Scaffold(
        containerColor = Color(0xFF121212),
        bottomBar = {
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
            composable(Rotas.DOUBLE_DATE_CONFIG) {
                TelaDoubleDateConfig(navController = navController)
            }
            composable(Rotas.ASTROLOGIA) {
                TelaAstrologia(navController = navController)
            }
            composable(Rotas.CONFIGURACOES) {
                TelaConfiguracoes(navController = navController)
            }
            composable(Rotas.FILTROS) {
                TelaFiltros(navController = navController)
            }
            composable(Rotas.LOCALIZACAO) {
                TelaLocalizacao(navController = navController)
            }
            composable(
                route = Rotas.ASSINATURA,
                arguments = listOf(navArgument("plano") { type = NavType.StringType })
            ) { entrada ->
                val plano = entrada.arguments?.getString("plano") ?: "PLUS"
                TelaAssinatura(navController = navController, plano = plano)
            }
            composable(
                route = Rotas.COMPRA,
                arguments = listOf(navArgument("tipo") { type = NavType.StringType })
            ) { entrada ->
                val tipo = entrada.arguments?.getString("tipo") ?: "BOOST"
                TelaCompra(navController = navController, tipo = tipo)
            }
            composable(
                route = Rotas.PAGAMENTO,
                arguments = listOf(navArgument("compraId") { type = NavType.LongType })
            ) { entrada ->
                val compraId = entrada.arguments?.getLong("compraId") ?: -1L
                TelaPagamento(navController = navController, compraId = compraId)
            }
        }
    }
}