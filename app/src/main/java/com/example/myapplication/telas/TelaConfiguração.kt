@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.myapplication.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SmokingRooms
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.math.roundToInt

// ---------------------------------------------------------------------
// Cores e textos fixos da tela
// ---------------------------------------------------------------------
private val CfgFundo = Color.Black
private val CfgCartao = Color(0xFF1A1A1A)
private val CfgDivisor = Color(0xFF2A2A2A)
private val CfgTextoFraco = Color(0xFFA8A8A8)
private val CfgVermelho = Color(0xFFFF3B4E)
private val CfgDourado = Color(0xFFF2B600)
private val CfgPlatina = Color(0xFF9E9E9E)
private const val NOME_APP = "TINDER"

// =====================================================================
// TELA PRINCIPAL
// =====================================================================

@Composable
fun TelaConfiguracoes(navController: NavController) {
    // ---- estados da tela (tudo mexe de verdade) ----
    var minimoFotos by remember { mutableStateOf(1f) }
    var comBio by remember { mutableStateOf(false) }

    var distanciaMaxima by remember { mutableStateOf(35f) }
    var mostrarMaisLonge by remember { mutableStateOf(false) }
    var interesseEm by remember { mutableStateOf("Mulheres") }
    var faixaEtaria by remember { mutableStateOf(18f..28f) }
    var mostrarForaDaFaixa by remember { mutableStateOf(true) }

    var visibilidade by remember { mutableStateOf("Padrão") }
    var descobertaAtiva by remember { mutableStateOf(true) }
    var chatVerificacao by remember { mutableStateOf(false) }
    var modoEscuro by remember { mutableStateOf("Usar configuração do sistema") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CfgFundo)
            .statusBarsPadding()
    ) {
        BarraTopoConfig(aoConfirmar = { navController.popBackStack() })

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            SecaoPlanos()
            SecaoConta()
            SecaoDescobertaPremium(
                minimoFotos = minimoFotos,
                aoMudarMinimoFotos = { minimoFotos = it },
                comBio = comBio,
                aoMudarComBio = { comBio = it }
            )
            SecaoDescoberta(
                distanciaMaxima = distanciaMaxima,
                aoMudarDistancia = { distanciaMaxima = it },
                mostrarMaisLonge = mostrarMaisLonge,
                aoMudarMostrarMaisLonge = { mostrarMaisLonge = it },
                interesseEm = interesseEm,
                aoTrocarInteresse = {
                    val opcoes = listOf("Mulheres", "Homens", "Todos")
                    interesseEm = opcoes[(opcoes.indexOf(interesseEm) + 1) % opcoes.size]
                },
                faixaEtaria = faixaEtaria,
                aoMudarFaixa = { faixaEtaria = it },
                mostrarForaDaFaixa = mostrarForaDaFaixa,
                aoMudarForaDaFaixa = { mostrarForaDaFaixa = it }
            )
            SecaoVisibilidade(
                visibilidade = visibilidade,
                aoEscolher = { visibilidade = it },
                descobertaAtiva = descobertaAtiva,
                aoMudarDescoberta = { descobertaAtiva = it }
            )
            SecaoMensagens(
                chatVerificacao = chatVerificacao,
                aoMudarChat = { chatVerificacao = it }
            )
            SecaoGeral()
            SecaoNotificacoes()
            SecaoModoEscuro(selecionado = modoEscuro, aoEscolher = { modoEscuro = it })
            SecaoRodape()
        }
    }
}

// =====================================================================
// BARRA DO TOPO
// =====================================================================

/** Título centralizado e botão de confirmar (✓) no canto direito. */
@Composable
private fun BarraTopoConfig(aoConfirmar: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = "Configurações",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable(onClick = aoConfirmar),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Check, contentDescription = "Concluir", tint = Color.Black)
        }
    }
}

// =====================================================================
// SEÇÕES
// =====================================================================

@Composable
private fun SecaoPlanos() {
    Spacer(modifier = Modifier.height(8.dp))
    CartaoPlano("PLATINUM", CfgPlatina, Color.Black, "Use as curtidas prioritárias, veja quem curtiu você e muito mais!")
    Spacer(modifier = Modifier.height(10.dp))
    CartaoPlano("GOLD", CfgDourado, Color.Black, "Veja quem curtiu você e mais!")
    Spacer(modifier = Modifier.height(10.dp))
    CartaoPlano("PLUS", CfgVermelho, Color.White, "Curtidas ilimitadas e mais!")
    Spacer(modifier = Modifier.height(10.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        CartaoAtalho(Icons.Filled.Star, Color(0xFF6C8CFF), "Quero Super Likes", Modifier.weight(1f))
        CartaoAtalho(Icons.Filled.Bolt, Color(0xFFD38CF0), "Quero Boosts", Modifier.weight(1f))
    }
    Spacer(modifier = Modifier.height(10.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        CartaoAtalho(Icons.Filled.VisibilityOff, Color.White, "Usar modo anônimo", Modifier.weight(1f))
        CartaoAtalho(Icons.Filled.Flight, CfgVermelho, "Usar o Modo Passaporte", Modifier.weight(1f))
    }
}

@Composable
private fun SecaoConta() {
    TituloSecao("Configurações de conta")
    GrupoCartao {
        // dados fictícios (placeholder) — nunca dados reais de ninguém
        LinhaNavegacao(rotulo = "Número de telefone", valor = "+55 41 9xxxx-xxxx")
        DivisorFino()
        LinhaNavegacao(rotulo = "Contas conectadas")
        DivisorFino()
        LinhaNavegacao(rotulo = "E-mail", valor = "seuemail@exemplo.com", corValor = CfgVermelho)
    }
    TextoAjuda("Verifique um e-mail para ajudar a proteger a sua conta.")
}

@Composable
private fun SecaoDescobertaPremium(
    minimoFotos: Float,
    aoMudarMinimoFotos: (Float) -> Unit,
    comBio: Boolean,
    aoMudarComBio: (Boolean) -> Unit
) {
    TituloSecao("Descoberta Premium", selo = "GOLD", corSelo = CfgDourado, corTextoSelo = Color.Black)
    TextoAjuda(
        "As preferências mostram mais pessoas que curtem a mesma vibe que você, mas não limitam " +
                "quem você vê. Você ainda poderá dar matches com qualquer pessoa."
    )
    Spacer(modifier = Modifier.height(8.dp))
    GrupoCartao {
        LinhaSlider(
            titulo = "Número mínimo de fotos",
            valorTexto = minimoFotos.roundToInt().toString(),
            valor = minimoFotos,
            intervalo = 1f..6f,
            passos = 4,
            aoMudar = aoMudarMinimoFotos
        )
        DivisorFino()
        LinhaSwitch("Com bio", comBio, aoMudarComBio)
        DivisorFino()
        LinhaNavegacao(rotulo = "Interesses", valor = "Selecionar")
        DivisorFino()
        LinhaNavegacao(rotulo = "Tô procurando", valor = "Selecionar", icone = Icons.Filled.Visibility)
        DivisorFino()
        LinhaNavegacao(rotulo = "Me interesso por...", valor = "Selecionar", icone = Icons.Filled.FavoriteBorder)
        DivisorFino()
        LinhaNavegacao(rotulo = "Formação", valor = "Selecionar", icone = Icons.Filled.School)
        DivisorFino()
        LinhaNavegacao(rotulo = "Família", valor = "Selecionar", icone = Icons.Filled.FamilyRestroom)
        DivisorFino()
        LinhaNavegacao(rotulo = "Estilo de comunicação", valor = "Selecionar", icone = Icons.Filled.ChatBubbleOutline)
        DivisorFino()
        LinhaNavegacao(rotulo = "Linguagem do amor", valor = "Selecionar", icone = Icons.Filled.Chat)
        DivisorFino()
        LinhaNavegacao(rotulo = "Pets", valor = "Selecionar", icone = Icons.Filled.Pets)
        DivisorFino()
        LinhaNavegacao(rotulo = "Bebida", valor = "Selecionar", icone = Icons.Filled.LocalBar)
        DivisorFino()
        LinhaNavegacao(rotulo = "Você fuma?", valor = "Selecionar", icone = Icons.Filled.SmokingRooms)
        DivisorFino()
        LinhaNavegacao(rotulo = "Atividade física", valor = "Selecionar", icone = Icons.Filled.FitnessCenter)
        DivisorFino()
        LinhaNavegacao(rotulo = "Redes sociais", valor = "Selecionar", icone = Icons.Filled.AlternateEmail)
    }
}

@Composable
private fun SecaoDescoberta(
    distanciaMaxima: Float,
    aoMudarDistancia: (Float) -> Unit,
    mostrarMaisLonge: Boolean,
    aoMudarMostrarMaisLonge: (Boolean) -> Unit,
    interesseEm: String,
    aoTrocarInteresse: () -> Unit,
    faixaEtaria: ClosedFloatingPointRange<Float>,
    aoMudarFaixa: (ClosedFloatingPointRange<Float>) -> Unit,
    mostrarForaDaFaixa: Boolean,
    aoMudarForaDaFaixa: (Boolean) -> Unit
) {
    TituloSecao("Descoberta")
    GrupoCartao {
        LinhaNavegacao(
            rotulo = "Localização",
            valor = "Curitiba, PR",
            ajuda = "Mude a localização pra dar matches em qualquer lugar."
        )
        DivisorFino()
        LinhaSlider(
            titulo = "Distância máxima",
            valorTexto = "${distanciaMaxima.roundToInt()} mi",
            valor = distanciaMaxima,
            intervalo = 1f..100f,
            passos = 0,
            aoMudar = aoMudarDistancia
        )
        LinhaSwitch(
            texto = "Mostrar pessoas mais longe de mim se eu ficar sem perfis pra ver.",
            marcado = mostrarMaisLonge,
            aoMudar = aoMudarMostrarMaisLonge,
            textoPequeno = true
        )
        DivisorFino()
        LinhaNavegacao(rotulo = "Tenho interesse em", valor = interesseEm, aoClicar = aoTrocarInteresse)
        DivisorFino()
        LinhaFaixaEtaria(faixa = faixaEtaria, aoMudar = aoMudarFaixa)
        LinhaSwitch(
            texto = "Mostrar pessoas um pouco fora da minha faixa de preferência se eu ficar sem perfis pra ver.",
            marcado = mostrarForaDaFaixa,
            aoMudar = aoMudarForaDaFaixa,
            textoPequeno = true
        )
    }
}

@Composable
private fun SecaoVisibilidade(
    visibilidade: String,
    aoEscolher: (String) -> Unit,
    descobertaAtiva: Boolean,
    aoMudarDescoberta: (Boolean) -> Unit
) {
    TituloSecao("Controlar visibilidade")
    GrupoCartao {
        LinhaOpcao(
            titulo = "Padrão",
            subtitulo = "Você aparecerá na pilha de perfis.",
            selecionado = visibilidade == "Padrão",
            aoClicar = { aoEscolher("Padrão") }
        )
        DivisorFino()
        LinhaOpcao(
            titulo = "Modo anônimo",
            subtitulo = "Você aparecerá apenas para as pessoas que curtir.",
            selecionado = visibilidade == "Modo anônimo",
            aoClicar = { aoEscolher("Modo anônimo") },
            selo = "PLUS"
        )
    }

    TituloSecao("Ativar Descoberta")
    GrupoCartao {
        LinhaSwitch("Ativar Descoberta", descobertaAtiva, aoMudarDescoberta)
    }
    TextoAjuda(
        "Ao desligar, seu perfil ficará oculto da pilha de perfis e a Descoberta será desativada. " +
                "As pessoas que você curtiu ainda poderão visualizar seu perfil e dar matches com você."
    )
}

@Composable
private fun SecaoMensagens(chatVerificacao: Boolean, aoMudarChat: (Boolean) -> Unit) {
    TituloSecao("Controle quem pode mandar mensagem pra você")
    GrupoCartao {
        LinhaSwitch(
            texto = "Chat com verificação",
            marcado = chatVerificacao,
            aoMudar = aoMudarChat,
            ajuda = "Membros com perfis verificados podem ativar esse recurso para receber mensagens " +
                    "apenas de outros perfis verificados."
        )
    }
}

@Composable
private fun SecaoGeral() {
    TituloSecao("Conexões")
    GrupoCartao { LinhaNavegacao(rotulo = "Bloquear contatos") }
    TextoAjuda("Selecione as pessoas da lista de contatos do seu celular que você não quer ver no app.")

    TituloSecao("Modos")
    GrupoCartao {
        LinhaNavegacao(rotulo = "Double Date")
        DivisorFino()
        LinhaNavegacao(rotulo = "Modo Música")
        DivisorFino()
        LinhaNavegacao(rotulo = "Modo Astrologia")
    }

    TituloSecao("Uso de dados")
    GrupoCartao { LinhaNavegacao(rotulo = "Reproduzir vídeos automaticamente") }

    TituloSecao("Status Online")
    GrupoCartao { LinhaNavegacao(rotulo = "Status Online") }

    TituloSecao("Perfil online")
    GrupoCartao { LinhaNavegacao(rotulo = "Nome de usuário", valor = "Garanta o seu") }
}

@Composable
private fun SecaoNotificacoes() {
    TituloSecao("Notificações")
    GrupoCartao {
        LinhaNavegacao(rotulo = "E-mail")
        DivisorFino()
        LinhaNavegacao(rotulo = "Notificações Push")
        DivisorFino()
        LinhaNavegacao(rotulo = "SMS")
        DivisorFino()
        LinhaNavegacao(rotulo = "Equipe do App")
    }
    TextoAjuda("Escolha quais notificações deseja ver enquanto estiver no aplicativo.")
}

@Composable
private fun SecaoModoEscuro(selecionado: String, aoEscolher: (String) -> Unit) {
    TituloSecao("Modo escuro")
    GrupoCartao {
        listOf("Usar configuração do sistema", "Modo claro", "Modo escuro").forEachIndexed { i, opcao ->
            if (i > 0) DivisorFino()
            LinhaOpcao(
                titulo = opcao,
                selecionado = selecionado == opcao,
                aoClicar = { aoEscolher(opcao) }
            )
        }
    }
}

@Composable
private fun SecaoRodape() {
    Spacer(modifier = Modifier.height(20.dp))
    BotaoLargo("Restaurar compras")
    Spacer(modifier = Modifier.height(12.dp))
    BotaoLargo("Compartilhar o app")

    TituloSecao("Fale com a gente")
    GrupoCartao { LinhaNavegacao(rotulo = "Ajuda e Suporte") }

    TituloSecao("Comunidade")
    GrupoCartao {
        LinhaNavegacao(rotulo = "Regras da comunidade")
        DivisorFino()
        LinhaNavegacao(rotulo = "Dicas de segurança")
        DivisorFino()
        LinhaNavegacao(rotulo = "Central de Segurança")
    }

    TituloSecao("Privacidade")
    GrupoCartao {
        LinhaNavegacao(rotulo = "Política de Cookies")
        DivisorFino()
        LinhaNavegacao(rotulo = "Política de Privacidade")
        DivisorFino()
        LinhaNavegacao(rotulo = "Preferências de privacidade")
    }

    TituloSecao("Jurídico")
    GrupoCartao {
        LinhaNavegacao(rotulo = "Licenças")
        DivisorFino()
        LinhaNavegacao(rotulo = "Termos de Serviço")
    }

    Spacer(modifier = Modifier.height(24.dp))
    BotaoLargo("Sair")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Filled.Whatshot, contentDescription = null, tint = CfgVermelho, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.height(6.dp))
        Text("Versão 1.0.0", color = CfgTextoFraco, fontSize = 13.sp)
    }

    BotaoLargo("Deletar conta")
    Spacer(modifier = Modifier.navigationBarsPadding().height(32.dp))
}

// =====================================================================
// COMPONENTES REUTILIZÁVEIS
// =====================================================================

@Composable
private fun TituloSecao(
    texto: String,
    selo: String? = null,
    corSelo: Color = CfgVermelho,
    corTextoSelo: Color = Color.White
) {
    Row(
        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(texto, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
        if (selo != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Selo(selo, corSelo, corTextoSelo)
        }
    }
}

@Composable
private fun TextoAjuda(texto: String) {
    Text(
        text = texto,
        color = CfgTextoFraco,
        fontSize = 13.sp,
        modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 8.dp)
    )
}

@Composable
private fun Selo(texto: String, cor: Color, corTexto: Color) {
    Text(
        text = texto,
        color = corTexto,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(cor)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

@Composable
private fun GrupoCartao(conteudo: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CfgCartao),
        content = conteudo
    )
}

@Composable
private fun DivisorFino() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .background(CfgDivisor)
    )
}

/** Cartão de plano: logo do app + selo colorido + descrição. */
@Composable
private fun CartaoPlano(selo: String, corSelo: Color, corTextoSelo: Color, descricao: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CfgCartao)
            .clickable { }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(NOME_APP, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.width(8.dp))
            Selo(selo, corSelo, corTextoSelo)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(descricao, color = Color.White, fontSize = 13.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun CartaoAtalho(icone: ImageVector, corIcone: Color, texto: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(CfgCartao)
            .clickable { }
            .padding(vertical = 18.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icone, contentDescription = null, tint = corIcone, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(texto, color = Color.White, fontSize = 13.sp, textAlign = TextAlign.Center)
    }
}

/** Linha com rótulo à esquerda, valor à direita e seta; opcionalmente ícone e texto de ajuda. */
@Composable
private fun LinhaNavegacao(
    rotulo: String,
    valor: String = "",
    icone: ImageVector? = null,
    corValor: Color = CfgTextoFraco,
    ajuda: String? = null,
    aoClicar: () -> Unit = { }
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = aoClicar)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icone != null) {
                Icon(icone, contentDescription = null, tint = CfgTextoFraco, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(
                text = rotulo,
                color = Color.White,
                fontSize = 15.sp,
                modifier = if (valor.isBlank()) Modifier.weight(1f) else Modifier
            )
            if (valor.isNotBlank()) {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = valor,
                    color = corValor,
                    fontSize = 15.sp,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.White)
        }
        if (ajuda != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(ajuda, color = CfgTextoFraco, fontSize = 12.sp)
        }
    }
}

@Composable
private fun LinhaSwitch(
    texto: String,
    marcado: Boolean,
    aoMudar: (Boolean) -> Unit,
    ajuda: String? = null,
    textoPequeno: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = texto,
                color = if (textoPequeno) CfgTextoFraco else Color.White,
                fontSize = if (textoPequeno) 13.sp else 15.sp
            )
            if (ajuda != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(ajuda, color = CfgTextoFraco, fontSize = 12.sp)
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Switch(
            checked = marcado,
            onCheckedChange = aoMudar,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = CfgVermelho,
                checkedBorderColor = Color.Transparent,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFF4A4A4A),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun LinhaSlider(
    titulo: String,
    valorTexto: String,
    valor: Float,
    intervalo: ClosedFloatingPointRange<Float>,
    passos: Int,
    aoMudar: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(titulo, color = Color.White, fontSize = 15.sp)
            Text(valorTexto, color = CfgTextoFraco, fontSize = 15.sp)
        }
        Slider(
            value = valor,
            onValueChange = aoMudar,
            valueRange = intervalo,
            steps = passos,
            colors = coresSlider()
        )
    }
}

@Composable
private fun LinhaFaixaEtaria(
    faixa: ClosedFloatingPointRange<Float>,
    aoMudar: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Faixa etária", color = Color.White, fontSize = 15.sp)
            Text(
                "${faixa.start.roundToInt()}-${faixa.endInclusive.roundToInt()}",
                color = CfgTextoFraco,
                fontSize = 15.sp
            )
        }
        RangeSlider(
            value = faixa,
            onValueChange = aoMudar,
            valueRange = 18f..100f,
            colors = coresSlider()
        )
    }
}

@Composable
private fun coresSlider() = SliderDefaults.colors(
    thumbColor = Color.White,
    activeTrackColor = CfgVermelho,
    inactiveTrackColor = Color(0xFF4A4A4A),
    activeTickColor = Color.Transparent,
    inactiveTickColor = Color.Transparent
)

/** Linha de escolha única: mostra um ✓ vermelho quando está selecionada. */
@Composable
private fun LinhaOpcao(
    titulo: String,
    selecionado: Boolean,
    aoClicar: () -> Unit,
    subtitulo: String? = null,
    selo: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = aoClicar)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(titulo, color = Color.White, fontSize = 15.sp)
                if (selo != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Selo(selo, CfgVermelho, Color.White)
                }
            }
            if (subtitulo != null) {
                Text(subtitulo, color = CfgTextoFraco, fontSize = 13.sp)
            }
        }
        if (selecionado) {
            Icon(Icons.Filled.Check, contentDescription = "Selecionado", tint = CfgVermelho)
        }
    }
}

@Composable
private fun BotaoLargo(texto: String, aoClicar: () -> Unit = { }) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(CfgCartao)
            .clickable(onClick = aoClicar)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(texto, color = Color.White, fontSize = 15.sp)
    }
}