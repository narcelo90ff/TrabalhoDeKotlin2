package com.example.myapplication.telas

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.dados.Preferencias


private val FundoSheet = Color(0xFF1C1C1E)
private val FundoCartao = Color(0xFF1F1F1F)
private val BordaSeg = Color(0xFF3A3A3C)
private val CinzaTexto = Color(0xFFB5B5B5)
private val VermelhoSeg = Color(0xFFE5484D)
private val AzulSeg = Color(0xFF3B82F6)
private val RosaBotao = Color(0xFFFFC2CC)

enum class FolhaSeguranca { NENHUMA, KIT, DENUNCIA }

private fun abrirLink(contexto: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        contexto.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(contexto, "Não foi possível abrir o link", Toast.LENGTH_SHORT).show()
    }
}

@Composable
private fun CabecalhoFolha(titulo: String, aoFechar: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = aoFechar, modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(Icons.Filled.Close, contentDescription = "Fechar", tint = Color.White)
        }
        Text(titulo, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KitSegurancaSheet(
    aoFechar: () -> Unit,
    aoDenunciar: () -> Unit,
    aoConfigMensagens: () -> Unit,
    aoCentralSeguranca: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = aoFechar,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = FundoSheet,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            CabecalhoFolha("Kit de ferramentas de segurança", aoFechar)
            Spacer(Modifier.height(12.dp))

            ItemKit(
                icone = Icons.Filled.Flag,
                corIcone = VermelhoSeg,
                titulo = "DENUNCIAR",
                descricao = "Denuncie alguém de quem você não é mais um match ou qualquer conteúdo ilegal na UE.",
                aoClicar = aoDenunciar
            )
            ItemKit(
                icone = Icons.Filled.Settings,
                corIcone = CinzaTexto,
                titulo = "ATUALIZAR CONFIGURAÇÕES DE MENSAGENS",
                descricao = "Veja quem pode enviar mensagens pra você.",
                aoClicar = aoConfigMensagens
            )
            ItemKit(
                icone = Icons.Filled.Shield,
                corIcone = AzulSeg,
                titulo = "ACESSE A CENTRAL DE SEGURANÇA",
                descricao = "Seu bem-estar é importante. Descubra recursos e ferramentas de segurança aqui.",
                aoClicar = aoCentralSeguranca
            )
        }
    }
}

@Composable
private fun ItemKit(
    icone: ImageVector,
    corIcone: Color,
    titulo: String,
    descricao: String,
    aoClicar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = aoClicar)
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(icone, contentDescription = null, tint = corIcone, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(16.dp))
        Column {
            Text(titulo, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(descricao, color = CinzaTexto, fontSize = 14.sp, lineHeight = 19.sp)
        }
    }
}


private data class OpcaoDenuncia(val texto: String, val url: String)

private val opcoesDenuncia = listOf(
    OpcaoDenuncia(
        "Danos físicos pessoais causados por alguém que não é mais seu match",
        "https://www.gov.br/mulheres/pt-br/ligue180"
    ),
    OpcaoDenuncia(
        "Assédio ou outros danos causados por alguém que não é mais seu match",
        "https://new.safernet.org.br/helpline"
    ),
    OpcaoDenuncia(
        "Conteúdo ilegal no Espaço Econômico Europeu",
        "https://denuncia.safernet.org.br"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DenunciaSheet(aoFechar: () -> Unit) {
    val contexto = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = aoFechar,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = FundoSheet,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            CabecalhoFolha("Denúncia", aoFechar)
            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFDCE9FA))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Shield,
                        contentDescription = null,
                        tint = Color(0xFF1B1B1B),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Dicas para fazer denúncias no app",
                        color = Color(0xFF1B1B1B),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    "Denuncie alguém no app tocando nos três pontos no perfil da pessoa ou no escudo de " +
                            "segurança nas suas mensagens com essa pessoa. Você também pode tocar e segurar " +
                            "em uma mensagem específica para denunciar.",
                    color = Color(0xFF1B1B1B),
                    fontSize = 14.sp,
                    lineHeight = 19.sp
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                "Para denunciar incidentes através da Central de Ajuda, selecione uma opção abaixo:",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(Modifier.height(8.dp))

            opcoesDenuncia.forEach { opcao ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { abrirLink(contexto, opcao.url) }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        opcao.texto,
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(12.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = "Abrir no navegador",
                        tint = CinzaTexto,
                        modifier = Modifier.size(20.dp)
                    )
                }
                HorizontalDivider(color = BordaSeg, modifier = Modifier.padding(horizontal = 24.dp))
            }
        }
    }
}


@Composable
fun TelaConfigMensagens(navController: NavController) {
    val contexto = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Configurações de mensagens",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Check, contentDescription = "Concluir", tint = Color.Black)
            }
        }

        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(FundoCartao)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    Icons.Filled.CheckBox,
                    contentDescription = null,
                    tint = AzulSeg,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "Chat com usuários verificados",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Membros com perfis verificados podem ativar esse recurso para receber " +
                                "mensagens apenas de outros perfis verificados.",
                        color = CinzaTexto,
                        fontSize = 14.sp,
                        lineHeight = 19.sp
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    Preferencias.perfilVerificado = true
                    Toast.makeText(contexto, "Perfil verificado!", Toast.LENGTH_SHORT).show()
                },
                enabled = !Preferencias.perfilVerificado,
                colors = ButtonDefaults.buttonColors(
                    containerColor = RosaBotao,
                    contentColor = Color(0xFF3A1F26),
                    disabledContainerColor = Color(0xFF3A3A3C),
                    disabledContentColor = CinzaTexto
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text(
                    if (Preferencias.perfilVerificado) "Perfil verificado" else "Verificar o meu perfil",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            "CONTROLE QUEM PODE MANDAR MENSAGEM PRA VOCÊ",
            color = CinzaTexto,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(FundoCartao)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Chat com usuários verificados",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Membros com perfis verificados podem ativar esse recurso para receber " +
                            "mensagens apenas de outros perfis verificados.",
                    color = CinzaTexto,
                    fontSize = 14.sp,
                    lineHeight = 19.sp
                )
            }
            Spacer(Modifier.width(12.dp))
            Switch(
                checked = Preferencias.chatSomenteVerificados,
                onCheckedChange = { ligar ->
                    if (ligar && !Preferencias.perfilVerificado) {
                        Toast.makeText(
                            contexto,
                            "Verifique o seu perfil para ativar esse recurso",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Preferencias.chatSomenteVerificados = ligar
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = AzulSeg,
                    uncheckedTrackColor = Color(0xFF8E8E93),
                    uncheckedThumbColor = Color.White,
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }
    }
}


private enum class AbaSeguranca(val rotulo: String) {
    GUIA("Guia"), FERRAMENTAS("Ferramentas"), RECURSOS("Recursos")
}

private data class Artigo(
    val titulo: String,
    val resumo: String? = null,
    val corpo: String,
    val quiz: Boolean = false
)

private data class SecaoGuia(val titulo: String, val artigos: List<Artigo>)

private val secoesGuia = listOf(
    SecaoGuia(
        "Guia para Encontros Seguros",
        listOf(
            Artigo(
                "Sofri Violência", "O Que Fazer?",
                "Se você está em perigo imediato, ligue para 190 (Polícia) ou 192 (SAMU). " +
                        "Procure um lugar seguro e avise alguém de confiança. Depois, denuncie a pessoa " +
                        "no app e registre um boletim de ocorrência. Em casos de violência contra a mulher, " +
                        "o Ligue 180 funciona 24 horas."
            )
        )
    ),
    SecaoGuia(
        "Assédio",
        listOf(
            Artigo(
                "Você sabe o que é transfobia?", "FONATRANS e ABGLT compartilham como",
                "Transfobia é a discriminação, o preconceito ou a violência contra pessoas trans e " +
                        "travestis. Respeitar o nome e os pronomes de cada pessoa é o primeiro passo para " +
                        "uma conversa respeitosa."
            ),
            Artigo(
                "Como lidar", null,
                "Você não precisa responder a mensagens que te deixem desconfortável. Bloqueie, " +
                        "desfaça o match e denuncie. Guarde capturas de tela caso precise registrar a ocorrência."
            ),
            Artigo(
                "7 vezes em que é perfeitamente aceitável dar um perdido em alguém", null,
                "Quando a pessoa desrespeita seus limites, pressiona por informações pessoais, pede " +
                        "dinheiro, muda de assunto para algo sexual sem consentimento, insiste depois de um " +
                        "\"não\", te deixa insegura ou simplesmente quando você perdeu o interesse."
            )
        )
    ),
    SecaoGuia(
        "Segurança",
        listOf(
            Artigo(
                "O Básico", "O que você precisa saber pra ter mais",
                "Não compartilhe endereço, rotina ou dados financeiros. Marque o primeiro encontro em " +
                        "local público, avise alguém de confiança e use transporte próprio. Desconfie de " +
                        "perfis que pedem dinheiro ou levam a conversa para outros aplicativos muito rápido."
            ),
            Artigo(
                "Quiz sobre Diretrizes da comunidade do app", null,
                "Teste o que você sabe sobre as regras de convivência: respeito, consentimento, " +
                        "proibição de perfis falsos e de conteúdo ofensivo.",
                quiz = true
            )
        )
    ),
    SecaoGuia(
        "Na vida real",
        listOf(
            Artigo(
                "Seu guia da vida real", "Dicas pra ter mais segurança na vida real",
                "Combine o encontro em um lugar movimentado, compartilhe sua localização com uma " +
                        "pessoa de confiança, mantenha o celular carregado e confie no seu instinto: " +
                        "se algo parecer estranho, vá embora."
            ),
            Artigo(
                "Quiz básico de segurança na vida real", null,
                "Responda perguntas rápidas sobre como se proteger antes, durante e depois de um encontro.",
                quiz = true
            )
        )
    ),
    SecaoGuia(
        "Denúncia",
        listOf(
            Artigo(
                "O que denunciar", "Quando você deve ou não denunciar alguém",
                "Denuncie assédio, ameaças, golpes, perfis falsos, conteúdo sexual não solicitado, " +
                        "discurso de ódio e menores de idade. Diferenças de opinião ou falta de interesse " +
                        "não são motivo de denúncia — nesse caso, desfaça o match."
            ),
            Artigo(
                "Como denunciar", null,
                "Toque nos três pontos no perfil ou no escudo dentro da conversa, escolha o motivo e " +
                        "envie. Você também pode tocar e segurar uma mensagem específica."
            ),
            Artigo(
                "O que acontece depois que", null,
                "Nossa equipe analisa a denúncia. Dependendo da gravidade, a conta pode ser advertida, " +
                        "suspensa ou banida. A pessoa denunciada não sabe quem fez a denúncia."
            )
        )
    ),
    SecaoGuia(
        "Consentimento",
        listOf(
            Artigo(
                "O básico sobre consentimento", "O consentimento faz parte de qualquer conexão",
                "Consentimento deve ser claro, livre e pode ser retirado a qualquer momento. " +
                        "Silêncio ou falta de resistência não significam um \"sim\"."
            )
        )
    ),
    SecaoGuia(
        "Viagens",
        listOf(
            Artigo(
                "O que fazer e o que não fazer", "Para fazer a viagem dos sonhos, você precisa",
                "Pesquise o destino e as leis locais, avise amigos e familiares do roteiro, evite " +
                        "mostrar objetos de valor e prefira marcar encontros em locais públicos."
            )
        )
    )
)

private val ferramentasSeguranca = listOf(
    Artigo(
        "Como denunciar",
        "Denunciar é uma forma segura de nos dizer que alguém está agindo de forma inapropriada.",
        "Toque nos três pontos no perfil ou no escudo dentro da conversa, escolha o motivo e envie."
    ),
    Artigo(
        "Configurações de privacidade",
        "Customize sua experiência com esses recursos e configurações de privacidade.",
        "Controle quem vê o seu perfil, quem pode te mandar mensagem e se o seu cartão fica oculto."
    ),
    Artigo(
        "Como desfazer um Match",
        "Quando perceber que não tem interesse ou quando seu Match agir de forma inapropriada, " +
                "você sempre pode desfazer o Match.",
        "Abra a conversa, toque no escudo e escolha \"Desfazer match\". A conversa deixa de aparecer " +
                "para os dois."
    ),
    Artigo(
        "Como verificar sua foto",
        "A melhor forma de saber que alguém não é bom demais pra ser verdade.",
        "Tire uma selfie imitando a pose mostrada no app. Ao ser aprovada, seu perfil ganha o " +
                "selo azul de verificado."
    )
)

private data class RecursoApoio(
    val nome: String,
    val telefone: String?,
    val url: String
)

private val recursosApoio = listOf(
    RecursoApoio("CVV - Suporte à saúde mental", "188", "https://www.cvv.org.br"),
    RecursoApoio(
        "Mapa do Acolhimento - Suporte para casos de abuso / violência contra a mulher",
        null,
        "https://mapadoacolhimento.org"
    ),
    RecursoApoio("SaferNet - Segurança digital & privacidade", null, "https://new.safernet.org.br")
)

@Composable
fun TelaCentralSeguranca(navController: NavController) {
    var aba by rememberSaveable { mutableStateOf(AbaSeguranca.GUIA) }
    var artigoAberto by remember { mutableStateOf<Artigo?>(null) }
    val contexto = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2C2C2E))
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Fechar", tint = Color.White)
            }
            Text(
                "Central de Segurança",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Abas
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFF2C2C2E))
                .padding(3.dp)
        ) {
            AbaSeguranca.entries.forEach { item ->
                val selecionada = item == aba
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (selecionada) Color.White else Color.Transparent)
                        .clickable { aba = item }
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        item.rotulo,
                        color = if (selecionada) Color.Black else Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            when (aba) {
                AbaSeguranca.GUIA -> secoesGuia.forEach { secao ->
                    Text(
                        secao.titulo,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
                    )
                    secao.artigos.forEach { artigo ->
                        LinhaArtigo(artigo) { artigoAberto = artigo }
                        Spacer(Modifier.height(10.dp))
                    }
                }

                AbaSeguranca.FERRAMENTAS -> {
                    Spacer(Modifier.height(8.dp))
                    ferramentasSeguranca.forEach { ferramenta ->
                        CartaoFerramenta(ferramenta) { artigoAberto = ferramenta }
                        Spacer(Modifier.height(14.dp))
                    }
                }

                AbaSeguranca.RECURSOS -> {
                    Spacer(Modifier.height(8.dp))
                    recursosApoio.forEach { recurso ->
                        ItemRecurso(recurso) { abrirLink(contexto, recurso.url) }
                        HorizontalDivider(
                            color = BordaSeg,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
            }
        }
    }

    artigoAberto?.let { artigo ->
        AlertDialog(
            onDismissRequest = { artigoAberto = null },
            title = { Text(artigo.titulo) },
            text = { Text(artigo.corpo) },
            confirmButton = {
                TextButton(onClick = { artigoAberto = null }) { Text("Fechar") }
            }
        )
    }
}

@Composable
private fun LinhaArtigo(artigo: Artigo, aoClicar: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(FundoCartao)
            .clickable(onClick = aoClicar)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (artigo.quiz) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("Quiz", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(16.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                artigo.titulo,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            artigo.resumo?.let {
                Spacer(Modifier.height(2.dp))
                Text(it, color = CinzaTexto, fontSize = 13.sp, maxLines = 1)
            }
        }
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = CinzaTexto
        )
    }
}

@Composable
private fun CartaoFerramenta(artigo: Artigo, aoClicar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(FundoCartao)
            .clickable(onClick = aoClicar)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color(0xFFEAF2FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.VerifiedUser,
                contentDescription = null,
                tint = AzulSeg,
                modifier = Modifier.size(72.dp)
            )
        }
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    artigo.titulo,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(artigo.resumo ?: "", color = CinzaTexto, fontSize = 14.sp, lineHeight = 19.sp)
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = CinzaTexto
            )
        }
    }
}

@Composable
private fun ItemRecurso(recurso: RecursoApoio, aoAcessar: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            recurso.nome,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
        recurso.telefone?.let {
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Phone,
                    contentDescription = null,
                    tint = CinzaTexto,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(it, color = CinzaTexto, fontSize = 14.sp)
            }
        }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(
            onClick = aoAcessar,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, BordaSeg),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text("Acesse o site", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}