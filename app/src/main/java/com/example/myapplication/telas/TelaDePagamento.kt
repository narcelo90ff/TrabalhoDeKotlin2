package com.example.myapplication.telas

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.dados.banco.CompraEntity
import com.example.myapplication.dados.banco.LojaRepositorio
import com.example.myapplication.dados.banco.StatusCompra
import com.example.myapplication.navegacao.Rotas
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.floor

private val FundoPagamento = Color(0xFF121212)
private val CartaoPagamento = Color(0xFF1E1E1E)
private val VerdePix = Color(0xFF32BCAD)
private val TextoFracoPagamento = Color(0xFFB0B0B0)

@Composable
fun TelaPagamento(navController: NavController, compraId: Long) {
    val contexto = LocalContext.current
    val repositorio = remember { LojaRepositorio.obter(contexto) }
    val escopo = rememberCoroutineScope()

    var compra by remember { mutableStateOf<CompraEntity?>(null) }
    var carregou by remember { mutableStateOf(false) }
    var processando by remember { mutableStateOf(false) }
    var copiado by remember { mutableStateOf(false) }

    LaunchedEffect(compraId) {
        compra = repositorio.buscarCompra(compraId)
        carregou = true
    }
    LaunchedEffect(copiado) {
        if (copiado) {
            delay(2000)
            copiado = false
        }
    }

    val pago = compra?.status == StatusCompra.PAGO

    fun fechar() {
        if (pago) {
            voltarParaAntesDaCompra(navController)
        } else {
            escopo.launch {
                repositorio.cancelar(compraId)
                voltarParaAntesDaCompra(navController)
            }
        }
    }

    BackHandler { fechar() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoPagamento)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2A2A2A))
                    .clickable { fechar() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Fechar", tint = Color.White)
            }
        }

        val atual = compra
        when {
            !carregou -> Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = VerdePix)
            }

            atual == null -> CompraIndisponivel(Modifier.weight(1f))

            atual.status == StatusCompra.CANCELADO -> CompraIndisponivel(Modifier.weight(1f))

            pago -> PagamentoAprovado(atual, Modifier.weight(1f))

            else -> PagamentoPendente(
                compra = atual,
                copiado = copiado,
                aoCopiar = {
                    val gerenciador = contexto.getSystemService(ClipboardManager::class.java)
                    gerenciador?.setPrimaryClip(ClipData.newPlainText("Pix copia e cola", atual.codigoPix))
                    copiado = true
                },
                modifier = Modifier.weight(1f)
            )
        }

        if (carregou) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                val textoBotao = when {
                    pago || atual == null || atual.status == StatusCompra.CANCELADO -> "Concluir"
                    else -> "Já paguei (simular pagamento)"
                }
                Button(
                    onClick = {
                        if (pago || atual == null || atual.status == StatusCompra.CANCELADO) {
                            voltarParaAntesDaCompra(navController)
                        } else if (!processando) {
                            processando = true
                            escopo.launch {
                                if (repositorio.confirmarPagamento(compraId)) {
                                    compra = repositorio.buscarCompra(compraId)
                                }
                                processando = false
                            }
                        }
                    },
                    enabled = !processando,
                    colors = ButtonDefaults.buttonColors(containerColor = VerdePix, contentColor = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    if (processando) {
                        CircularProgressIndicator(color = Color.Black, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                    } else {
                        Text(textoBotao, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun CompraIndisponivel(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
        Text(
            "Esta compra não está mais disponível.",
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PagamentoPendente(
    compra: CompraEntity,
    copiado: Boolean,
    aoCopiar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pague com Pix", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text(compra.item, color = TextoFracoPagamento, fontSize = 15.sp)
        Text(
            text = formatarReais(compra.valorCentavos / 100.0),
            color = VerdePix,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(14.dp)
        ) {
            QrCode(texto = compra.codigoPix, modifier = Modifier.size(220.dp))
        }
        Spacer(Modifier.height(10.dp))
        Text(
            "Abra o app do seu banco e escaneie o QR Code",
            color = TextoFracoPagamento,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(22.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CartaoPagamento)
                .padding(16.dp)
        ) {
            Text("Pix copia e cola", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(
                text = compra.codigoPix,
                color = TextoFracoPagamento,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF2A2A2A))
                    .clickable(onClick = aoCopiar)
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (copiado) Icons.Filled.Check else Icons.Filled.ContentCopy,
                    contentDescription = null,
                    tint = VerdePix,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (copiado) "Código copiado!" else "Copiar código",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(Modifier.height(14.dp))
        Text(
            "Pagamento de demonstração: a chave Pix é fictícia e nenhum valor real é cobrado.",
            color = Color(0xFF808080),
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun PagamentoAprovado(compra: CompraEntity, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(VerdePix),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(56.dp))
        }
        Spacer(Modifier.height(20.dp))
        Text("Pagamento aprovado!", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "${compra.item} · ${formatarReais(compra.valorCentavos / 100.0)}",
            color = TextoFracoPagamento,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun QrCode(texto: String, modifier: Modifier = Modifier) {
    val matriz = remember(texto) {
        val dicas = mapOf<EncodeHintType, Any>(
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M,
            EncodeHintType.MARGIN to 0,
            EncodeHintType.CHARACTER_SET to "UTF-8"
        )
        QRCodeWriter().encode(texto, BarcodeFormat.QR_CODE, 0, 0, dicas)
    }
    Canvas(modifier = modifier) {
        val modulos = matriz.width
        val passo = floor(size.minDimension / modulos)
        val sobraX = (size.width - passo * modulos) / 2f
        val sobraY = (size.height - passo * modulos) / 2f
        for (y in 0 until modulos) {
            for (x in 0 until modulos) {
                if (matriz.get(x, y)) {
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(sobraX + x * passo, sobraY + y * passo),
                        size = Size(passo, passo)
                    )
                }
            }
        }
    }
}

private fun voltarParaAntesDaCompra(navController: NavController) {
    while (true) {
        val rota = navController.currentBackStackEntry?.destination?.route ?: break
        val ehTelaDeCompra = rota == Rotas.PAGAMENTO || rota == Rotas.COMPRA || rota == Rotas.ASSINATURA
        if (!ehTelaDeCompra || !navController.popBackStack()) break
    }
}