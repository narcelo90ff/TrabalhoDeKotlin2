package com.example.myapplication.navegacao

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.example.myapplication.dados.TipoSelecao
import com.example.myapplication.telas.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val rotaAtual = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        containerColor = Color(0xFF121212),
        bottomBar = {
            if (rotaTemBottomBar(rotaAtual)) BottomNavBar(navController = navController)
        }
    ) { paddingExterno ->
        NavHost(
            navController = navController,
            startDestination = Rotas.DESCOBRIR,
            modifier = Modifier.padding(paddingExterno)
        ) {
            composable(Rotas.DESCOBRIR) { TelaDescobrir(navController) }
            composable(Rotas.EXPLORAR) { TelaExplorar(navController) }
            composable(Rotas.CURTIDAS) { TelaListaCurtidas(navController) }
            composable(Rotas.CONVERSAS) { TelaListaConversas(navController) }
            composable(Rotas.PERFIL) { TelaPerfil(navController) }
            composable(Rotas.EDITAR_BIO) { TelaEditarBio(navController) }
            composable(Rotas.EDITAR_ENCONTRO) { TelaEditarEncontro(navController) }
            composable(Rotas.EDITAR_PERGUNTA) { TelaEditarPergunta(navController) }
            composable(Rotas.DOUBLE_DATE) { TelaDoubleDate(navController) }
            composable(Rotas.DOUBLE_DATE_CONFIG) { TelaDoubleDateConfig(navController) }
            composable(Rotas.CENTRAL_SEGURANCA) { TelaCentralSeguranca(navController) }
            composable(Rotas.CONFIG_MENSAGENS) { TelaConfigMensagens(navController) }
            composable(Rotas.ASTROLOGIA) { TelaAstrologia(navController) }
            composable(Rotas.CONFIGURACOES) { TelaConfiguracoes(navController) }
            composable(Rotas.FILTROS) { TelaFiltros(navController) }
            composable(Rotas.LOCALIZACAO) { TelaLocalizacao(navController) }

            // Telas com argumentos
            composable(
                Rotas.EXPLORAR_DETALHE,
                arguments = listOf(navArgument("categoria") { type = NavType.StringType })
            ) {
                val categoria = it.arguments?.getString("categoria") ?: "Explorar"
                TelaExplorarDetalhe(navController, categoria)
            }

            composable(
                Rotas.DETALHE_CURTIDA,
                arguments = listOf(navArgument("pessoaId") { type = NavType.IntType })
            ) {
                val pessoaId = it.arguments?.getInt("pessoaId") ?: -1
                TelaDetalheCurtida(navController, pessoaId)
            }

            composable(
                Rotas.DETALHE_CONVERSA,
                arguments = listOf(navArgument("conversaId") { type = NavType.IntType })
            ) {
                val conversaId = it.arguments?.getInt("conversaId") ?: -1
                TelaDetalheConversa(navController, conversaId)
            }

            composable(
                Rotas.SELECIONAR,
                arguments = listOf(navArgument("tipo") { type = NavType.StringType })
            ) {
                val nome = it.arguments?.getString("tipo") ?: "HOBBIES"
                val tipo = TipoSelecao.entries.firstOrNull { t -> t.name == nome } ?: TipoSelecao.HOBBIES
                TelaSelecaoItens(navController, tipo)
            }

            composable(
                Rotas.ASSINATURA,
                arguments = listOf(navArgument("plano") { type = NavType.StringType })
            ) {
                val plano = it.arguments?.getString("plano") ?: "PLUS"
                TelaAssinatura(navController, plano)
            }

            composable(
                Rotas.COMPRA,
                arguments = listOf(navArgument("tipo") { type = NavType.StringType })
            ) {
                val tipo = it.arguments?.getString("tipo") ?: "BOOST"
                TelaCompra(navController, tipo)
            }

            composable(
                Rotas.PAGAMENTO,
                arguments = listOf(navArgument("compraId") { type = NavType.LongType })
            ) {
                val compraId = it.arguments?.getLong("compraId") ?: -1L
                TelaPagamento(navController, compraId)
            }
        }
    }
}