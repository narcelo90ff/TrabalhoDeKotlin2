package com.example.myapplication.componentes

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Barra de rolagem que dá pra SEGURAR e ARRASTAR com o mouse (ou o dedo),
 * ou clicar no trilho para pular até aquele ponto. Só aparece quando a
 * tela realmente rola.
 *
 * Como usar: coloque dentro do MESMO Box da lista, alinhada à direita:
 *
 *   Box {
 *       Column(Modifier.verticalScroll(estado)) { ... }
 *       BarraRolagem(estado, Modifier.align(Alignment.CenterEnd))
 *   }
 *
 * Existem 3 versões: para verticalScroll (ScrollState), LazyColumn
 * (LazyListState) e LazyVerticalGrid (LazyGridState).
 */

/** Descreve a rolagem de um jeito que o desenho da barra entende, não importa o tipo de lista. */
private class AdaptadorRolagem(
    val rolavel: () -> Boolean,
    /** altura do "polegar" (a parte que se segura), dada a altura do trilho em pixels */
    val alturaPolegar: (trilhoPx: Float) -> Float,
    /** onde a rolagem está agora: 0 = começo, 1 = fim */
    val posicao: () -> Float,
    /** rola até uma posição: 0 = começo, 1 = fim */
    val moverPara: (fracao: Float) -> Unit
)

// ---------------------------------------------------------------------------
// Versão 1: Column com verticalScroll
// ---------------------------------------------------------------------------

@Composable
fun BarraRolagem(estado: ScrollState, modifier: Modifier = Modifier) {
    val adaptador = remember(estado) {
        AdaptadorRolagem(
            rolavel = { estado.maxValue > 0 && estado.maxValue != Int.MAX_VALUE },
            alturaPolegar = { trilho -> trilho * trilho / (trilho + estado.maxValue) },
            posicao = {
                if (estado.maxValue > 0) estado.value.toFloat() / estado.maxValue else 0f
            },
            moverPara = { fracao ->
                // dispatchRawDelta rola na hora, sem precisar de coroutine
                estado.dispatchRawDelta(fracao * estado.maxValue - estado.value)
            }
        )
    }
    TrilhoBarra(adaptador, modifier)
}

// ---------------------------------------------------------------------------
// Versão 2: LazyColumn
// ---------------------------------------------------------------------------

@Composable
fun BarraRolagem(estado: LazyListState, modifier: Modifier = Modifier) {
    val escopo = rememberCoroutineScope()
    val adaptador = remember(estado) {
        AdaptadorRolagem(
            rolavel = { estado.canScrollForward || estado.canScrollBackward },
            alturaPolegar = { trilho ->
                val info = estado.layoutInfo
                val total = info.totalItemsCount.coerceAtLeast(1)
                val visiveis = info.visibleItemsInfo.size.coerceAtLeast(1)
                trilho * visiveis / total
            },
            posicao = {
                val info = estado.layoutInfo
                val primeiro = info.visibleItemsInfo.firstOrNull()
                val parte = if (primeiro != null && primeiro.size > 0) {
                    (-primeiro.offset).coerceAtLeast(0).toFloat() / primeiro.size
                } else 0f
                posicaoPorItens(
                    total = info.totalItemsCount,
                    visiveis = info.visibleItemsInfo.size,
                    primeiroIndice = estado.firstVisibleItemIndex,
                    parteDoPrimeiro = parte
                )
            },
            moverPara = { fracao ->
                val info = estado.layoutInfo
                val faltam = (info.totalItemsCount - info.visibleItemsInfo.size).coerceAtLeast(0)
                val alvo = fracao * faltam
                val indice = alvo.toInt()
                val tamanhoItem = info.visibleItemsInfo.firstOrNull()?.size ?: 0
                val deslocamento = ((alvo - indice) * tamanhoItem).toInt()
                escopo.launch { estado.scrollToItem(indice, deslocamento) }
            }
        )
    }
    TrilhoBarra(adaptador, modifier)
}

// ---------------------------------------------------------------------------
// Versão 3: LazyVerticalGrid
// ---------------------------------------------------------------------------

@Composable
fun BarraRolagem(estado: LazyGridState, modifier: Modifier = Modifier) {
    val escopo = rememberCoroutineScope()
    val adaptador = remember(estado) {
        AdaptadorRolagem(
            rolavel = { estado.canScrollForward || estado.canScrollBackward },
            alturaPolegar = { trilho ->
                val info = estado.layoutInfo
                val total = info.totalItemsCount.coerceAtLeast(1)
                val visiveis = info.visibleItemsInfo.size.coerceAtLeast(1)
                trilho * visiveis / total
            },
            posicao = {
                val info = estado.layoutInfo
                val primeiro = info.visibleItemsInfo.firstOrNull()
                val parte = if (primeiro != null && primeiro.size.height > 0) {
                    (-primeiro.offset.y).coerceAtLeast(0).toFloat() / primeiro.size.height
                } else 0f
                posicaoPorItens(
                    total = info.totalItemsCount,
                    visiveis = info.visibleItemsInfo.size,
                    primeiroIndice = estado.firstVisibleItemIndex,
                    parteDoPrimeiro = parte
                )
            },
            moverPara = { fracao ->
                val info = estado.layoutInfo
                val faltam = (info.totalItemsCount - info.visibleItemsInfo.size).coerceAtLeast(0)
                val alvo = fracao * faltam
                val indice = alvo.toInt()
                val alturaItem = info.visibleItemsInfo.firstOrNull()?.size?.height ?: 0
                val deslocamento = ((alvo - indice) * alturaItem).toInt()
                escopo.launch { estado.scrollToItem(indice, deslocamento) }
            }
        )
    }
    TrilhoBarra(adaptador, modifier)
}

/** Posição (0 a 1) calculada pela quantidade de itens: primeiro item visível / itens que "sobram". */
private fun posicaoPorItens(
    total: Int,
    visiveis: Int,
    primeiroIndice: Int,
    parteDoPrimeiro: Float
): Float {
    val faltam = total - visiveis
    if (faltam <= 0) return 0f
    return ((primeiroIndice + parteDoPrimeiro) / faltam).coerceIn(0f, 1f)
}

// ---------------------------------------------------------------------------
// O desenho da barra e o tratamento do mouse/dedo
// ---------------------------------------------------------------------------

@Composable
private fun TrilhoBarra(adaptador: AdaptadorRolagem, modifier: Modifier) {
    var alturaTrilho by remember { mutableFloatStateOf(0f) }
    var segurando by remember { mutableStateOf(false) }
    val densidade = LocalDensity.current
    val minimoPx = with(densidade) { 56.dp.toPx() }

    // Sempre usa os valores mais recentes, mesmo no meio de um arrasto
    val irPara by rememberUpdatedState<(Float) -> Unit> { yPx ->
        val polegar = adaptador.alturaPolegar(alturaTrilho).coerceIn(minimoPx, alturaTrilho)
        val espaco = (alturaTrilho - polegar).coerceAtLeast(1f)
        // o ponteiro fica no MEIO do polegar, por isso o "- polegar / 2"
        adaptador.moverPara(((yPx - polegar / 2f) / espaco).coerceIn(0f, 1f))
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(14.dp)
            .onSizeChanged { alturaTrilho = it.height.toFloat() }
    ) {
        if (adaptador.rolavel() && alturaTrilho > 0f) {
            val polegar = adaptador.alturaPolegar(alturaTrilho).coerceIn(minimoPx, alturaTrilho)
            val topo = adaptador.posicao() * (alturaTrilho - polegar)

            // faixa inteira: recebe o clique/toque do mouse e do dedo
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        // clicar no trilho pula até ali
                        detectTapGestures(
                            onPress = { posicao ->
                                irPara(posicao.y)
                                segurando = true
                                tryAwaitRelease()
                                segurando = false
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        // segurar e arrastar
                        detectDragGestures(
                            onDragStart = { posicao ->
                                segurando = true
                                irPara(posicao.y)
                            },
                            onDragEnd = { segurando = false },
                            onDragCancel = { segurando = false },
                            onDrag = { mudanca, _ ->
                                mudanca.consume()
                                irPara(mudanca.position.y)
                            }
                        )
                    }
            ) {
                // trilho fininho, só pra mostrar por onde dá pra arrastar
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxHeight()
                        .width(4.dp)
                        .background(Color(0x1AFFFFFF), RoundedCornerShape(50))
                )
                // o polegar (a parte que se segura)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset { IntOffset(0, topo.roundToInt()) }
                        .width(if (segurando) 10.dp else 6.dp)
                        .height(with(densidade) { polegar.toDp() })
                        .background(
                            if (segurando) Color(0xE6FFFFFF) else Color(0x99FFFFFF),
                            RoundedCornerShape(50)
                        )
                )
            }
        }
    }
}