package com.example.myapplication.telas

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.dados.AbaDescobrir
import com.example.myapplication.dados.Astrologia
import com.example.myapplication.dados.DescobrirEstado
import kotlinx.coroutines.delay

private val FundoEscuro = Color(0xFF121212)
private val FundoCartao = Color(0xFF1E1E1E)
private val FundoEstrelas = Color(0xFF0C1322)
private val TextoFraco = Color(0xFFB5B5B5)

private enum class Etapa { INTRO, FORM, LOCAL, HORA, CONECTANDO, SOBRE }


@Composable
fun TelaAstrologia(navController: NavController) {
    var etapa by rememberSaveable {
        mutableStateOf(if (Astrologia.ativo) Etapa.FORM else Etapa.INTRO)
    }
    var ponto by remember { mutableIntStateOf(0) }

    val fechar: () -> Unit = { navController.popBackStack() }

    BackHandler(enabled = etapa == Etapa.LOCAL || etapa == Etapa.HORA) { etapa = Etapa.FORM }

    LaunchedEffect(etapa) {
        if (etapa == Etapa.CONECTANDO) {
            repeat(5) {
                ponto = it % 2
                delay(500)
            }
            etapa = Etapa.SOBRE
        }
    }

    when (etapa) {
        Etapa.INTRO -> EtapaIntro(
            aoContinuar = { etapa = Etapa.FORM },
            aoFechar = fechar
        )
        Etapa.FORM -> EtapaForm(
            aoEditarLocal = { etapa = Etapa.LOCAL },
            aoEditarHora = { etapa = Etapa.HORA },
            aoContinuar = { etapa = Etapa.CONECTANDO },
            aoFechar = fechar
        )
        Etapa.LOCAL -> EtapaEntradaTexto(
            titulo = "Adicione um local de nascimento",
            dica = "Digite uma cidade",
            inicial = Astrologia.local,
            teclado = KeyboardType.Text,
            aoSalvar = {
                Astrologia.local = it
                etapa = Etapa.FORM
            },
            aoFechar = { etapa = Etapa.FORM }
        )
        Etapa.HORA -> EtapaEntradaTexto(
            titulo = "Adicione uma hora de nascimento",
            dica = "Ex.: 14:30",
            inicial = Astrologia.hora,
            teclado = KeyboardType.Text,
            aoSalvar = {
                Astrologia.hora = it
                etapa = Etapa.FORM
            },
            aoFechar = { etapa = Etapa.FORM }
        )
        Etapa.CONECTANDO -> EtapaConectando(ponto = ponto, aoFechar = { etapa = Etapa.FORM })
        Etapa.SOBRE -> EtapaSobreVoce(
            aoEntrar = {
                Astrologia.ativo = true
                DescobrirEstado.aba = AbaDescobrir.ASTROLOGIA
                navController.popBackStack()
            },
            aoEditarDados = { etapa = Etapa.FORM },
            aoFechar = fechar
        )
    }
}

@Composable
private fun BotaoRedondo(icone: ImageVector, descricao: String, aoClicar: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0x33FFFFFF))
            .clickable { aoClicar() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icone, contentDescription = descricao, tint = Color.White, modifier = Modifier.size(22.dp))
    }
}

@Composable
private fun BotaoBranco(texto: String, aoClicar: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(27.dp))
            .background(Color.White)
            .clickable { aoClicar() },
        contentAlignment = Alignment.Center
    ) {
        Text(texto, color = Color.Black, fontSize = 17.sp, fontWeight = FontWeight.Bold)
    }
}


@Composable
private fun EtapaIntro(aoContinuar: () -> Unit, aoFechar: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(FundoEscuro)) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(Color(0xFF1A1A1A))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                BotaoRedondo(Icons.Filled.Close, "Fechar", aoFechar)
            }
            Text("🔮", fontSize = 96.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Experimente o\nModo Astrologia",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "O Modo Astrologia oferece informações rápidas e divertidas sobre como você e seus possíveis matches se conectam astrologicamente.",
                color = TextoFraco,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            BotaoBranco("Continuar", aoContinuar)
            Text(
                text = "Talvez mais tarde",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { aoFechar() }
                    .padding(vertical = 20.dp)
            )
        }
    }
}


@Composable
private fun EtapaForm(
    aoEditarLocal: () -> Unit,
    aoEditarHora: () -> Unit,
    aoContinuar: () -> Unit,
    aoFechar: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(FundoEscuro).padding(20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BotaoRedondo(Icons.Filled.Close, "Fechar", aoFechar)
            BotaoRedondo(Icons.Filled.Info, "Informações") {}
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Adicione seu mix de signos do zodíaco",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Seus signos do zodíaco revelarão as combinações e conflitos entre você e um match.",
            color = TextoFraco,
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            CampoAstro(
                rotulo = "Data",
                valor = Astrologia.DATA_NASCIMENTO,
                dica = "Revela seu signo, que descreve a essência da sua personalidade.",
                editavel = false,
                aoClicar = {}
            )
            CampoAstro(
                rotulo = "Lugar",
                valor = Astrologia.local.ifBlank { "Adicionar" },
                dica = "Revela sua Lua, que descreve seu lado emocional.",
                editavel = true,
                aoClicar = aoEditarLocal
            )
            CampoAstro(
                rotulo = "Hora",
                valor = Astrologia.hora.ifBlank { "Adicionar" },
                dica = "Revela seu ascendente, que descreve como os outros veem você.",
                editavel = true,
                aoClicar = aoEditarHora
            )
        }
        BotaoBranco("Continuar", aoContinuar)
    }
}

@Composable
private fun CampoAstro(
    rotulo: String,
    valor: String,
    dica: String,
    editavel: Boolean,
    aoClicar: () -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 14.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(FundoCartao)
                .then(if (editavel) Modifier.clickable { aoClicar() } else Modifier)
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                rotulo,
                color = if (editavel) Color.White else TextoFraco,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Text(valor, color = if (editavel) Color.White else TextoFraco, fontSize = 16.sp)
            if (editavel) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Text(
            text = dica,
            color = TextoFraco,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 6.dp, top = 8.dp)
        )
    }
}


@Composable
private fun EtapaEntradaTexto(
    titulo: String,
    dica: String,
    inicial: String,
    teclado: KeyboardType,
    aoSalvar: (String) -> Unit,
    aoFechar: () -> Unit
) {
    var texto by remember { mutableStateOf(inicial) }
    val foco = remember { FocusRequester() }
    LaunchedEffect(Unit) { foco.requestFocus() }

    Column(modifier = Modifier.fillMaxSize().background(FundoEscuro).padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BotaoRedondo(Icons.Filled.Close, "Fechar", aoFechar)
            Spacer(modifier = Modifier.width(14.dp))
            Text(titulo, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(28.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(FundoCartao)
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            if (texto.isEmpty()) {
                Text(dica, color = TextoFraco, fontSize = 18.sp)
            }
            BasicTextField(
                value = texto,
                onValueChange = { texto = it },
                singleLine = true,
                textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
                cursorBrush = SolidColor(Color.White),
                keyboardOptions = KeyboardOptions(keyboardType = teclado, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { aoSalvar(texto.trim()) }),
                modifier = Modifier.fillMaxWidth().focusRequester(foco)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        BotaoBranco("Salvar") { aoSalvar(texto.trim()) }
    }
}


@Composable
private fun EtapaConectando(ponto: Int, aoFechar: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(FundoEstrelas)) {
        Box(modifier = Modifier.padding(20.dp)) {
            BotaoRedondo(Icons.Filled.Close, "Fechar", aoFechar)
        }
        Text("✦", color = Color(0xFF8FB0F0), fontSize = 22.sp, modifier = Modifier.align(Alignment.TopCenter).padding(top = 140.dp, end = 120.dp))
        Text("✦", color = Color(0xFF8FB0F0), fontSize = 30.sp, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 140.dp, start = 140.dp))
        Text("✦", color = Color(0xFF8FB0F0), fontSize = 16.sp, modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 90.dp, start = 40.dp))
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Conectando\nsuas estrelas",
                color = Color(0xFFDCE6FF),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(2) { i ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (i == ponto) Color.White else Color(0x55FFFFFF))
                    )
                }
            }
        }
    }
}


@Composable
private fun EtapaSobreVoce(
    aoEntrar: () -> Unit,
    aoEditarDados: () -> Unit,
    aoFechar: () -> Unit
) {
    val sol = Astrologia.indiceSol
    val lua = Astrologia.indiceLua
    val asc = Astrologia.indiceAscendente

    Column(modifier = Modifier.fillMaxSize().background(FundoEstrelas).padding(20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BotaoRedondo(Icons.Filled.Close, "Fechar", aoFechar)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .background(Color(0xFF2A3450)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Person, contentDescription = null, tint = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Astrologia", color = TextoFraco, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "Sobre você",
            color = Color(0xFFDCE6FF),
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            ItemSigno(
                icone = Icons.Filled.WbSunny,
                corIcone = Color(0xFFFF6B4A),
                rotulo = "Sol em ${Astrologia.SIGNOS[sol]} ${Astrologia.SIMBOLOS[sol]}",
                titulo = "Sua personalidade essencial",
                texto = Astrologia.DESCRICAO_SOL[sol]
            )
            ItemSigno(
                icone = if (lua != null) Icons.Filled.NightsStay else Icons.Filled.Lock,
                corIcone = if (lua != null) Color(0xFFB9C6FF) else Color(0xFF9A8FD0),
                rotulo = if (lua != null) "Lua em ${Astrologia.SIGNOS[lua]} ${Astrologia.SIMBOLOS[lua]}" else "Signo Lua",
                titulo = "Seu lado emocional",
                texto = if (lua != null) {
                    "Elemento ${Astrologia.ELEMENTOS[lua % 4]}: é assim que você sente e reage por dentro."
                } else {
                    "Adicione seu local de nascimento para revelar sua Lua."
                }
            )
            ItemSigno(
                icone = if (asc != null) Icons.Filled.NightsStay else Icons.Filled.Lock,
                corIcone = if (asc != null) Color(0xFFB9C6FF) else Color(0xFF9A8FD0),
                rotulo = if (asc != null) "Ascendente em ${Astrologia.SIGNOS[asc]} ${Astrologia.SIMBOLOS[asc]}" else "Signo Ascendente",
                titulo = "Como os outros te veem",
                texto = if (asc != null) {
                    "Elemento ${Astrologia.ELEMENTOS[asc % 4]}: é a primeira impressão que as pessoas têm de você."
                } else {
                    "Adicione seu local e hora de nascimento para descobrir seu ascendente."
                }
            )
        }

        BotaoBranco("Entrar no Modo Astrologia", aoEntrar)
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = if (Astrologia.temLocal && Astrologia.temHora) {
                    "Editar local e hora de nascimento"
                } else {
                    "Adicionar local e hora\nde nascimento"
                },
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable { aoEditarDados() }
                    .padding(vertical = 18.dp)
            )
        }
    }
}

@Composable
private fun ItemSigno(
    icone: ImageVector,
    corIcone: Color,
    rotulo: String,
    titulo: String,
    texto: String
) {
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 22.dp)) {
        Icon(icone, contentDescription = null, tint = corIcone, modifier = Modifier.size(40.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(rotulo, color = TextoFraco, fontSize = 13.sp)
            Text(titulo, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(texto, color = Color(0xFFDCE6FF), fontSize = 15.sp)
        }
    }
}