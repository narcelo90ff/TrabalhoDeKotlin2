package com.example.myapplication.dados

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.math.abs

/** Abas do topo da tela Descobrir. */
enum class AbaDescobrir { PRA_VOCE, DOUBLE_DATE, ASTROLOGIA }

/** Guarda qual aba está aberta (sobrevive à navegação entre telas). */
object DescobrirEstado {
    var aba by mutableStateOf(AbaDescobrir.PRA_VOCE)

    /** A tela de convidar amigos só abre sozinha na primeira vez que toca na aba Double Date. */
    var conviteDoubleDateVisto by mutableStateOf(false)
}

object Astrologia {
    val SIGNOS = listOf(
        "Áries", "Touro", "Gêmeos", "Câncer", "Leão", "Virgem",
        "Libra", "Escorpião", "Sagitário", "Capricórnio", "Aquário", "Peixes"
    )
    val SIMBOLOS = listOf("♈", "♉", "♊", "♋", "♌", "♍", "♎", "♏", "♐", "♑", "♒", "♓")

    /** Repete a cada 4 signos: Áries = fogo, Touro = terra, Gêmeos = ar, Câncer = água... */
    val ELEMENTOS = listOf("fogo", "terra", "ar", "água")

    val DESCRICAO_SOL = listOf(
        "Você é pura iniciativa: corajoso, direto e sempre pronto pra começar algo novo.",
        "Você gosta de conforto, de lealdade e de aproveitar as coisas boas da vida sem pressa.",
        "Curioso e falante, você adora conversar, descobrir coisas novas e mudar de assunto rapidinho.",
        "Você é caseiro, protetor e sente tudo intensamente, principalmente quando gosta de alguém.",
        "Você tem brilho próprio, ama ser o centro das atenções e é generoso com quem gosta.",
        "Detalhista e prestativo, você mostra carinho cuidando das pessoas e organizando tudo.",
        "Você adora flerte, diplomacia e, acima de tudo, pessoas charmosas. A indecisão também é uma característica sua.",
        "Intenso e misterioso, você não faz nada pela metade e sabe exatamente o que quer.",
        "Aventureiro e otimista, você vive atrás de liberdade, viagens e boas risadas.",
        "Ambicioso e focado, você leva compromisso a sério e constrói tudo com paciência.",
        "Original e independente, você pensa fora da caixa e valoriza muito a sua liberdade.",
        "Sonhador e sensível, você tem muita imaginação e um coração enorme."
    )

    // Data de nascimento do usuário (a mesma que aparece na tela, sem edição, como no app).
    private const val DIA_NASCIMENTO = 2
    private const val MES_NASCIMENTO = 10
    const val DATA_NASCIMENTO = "2 de outubro de 2007"

    var ativo by mutableStateOf(false)
    var local by mutableStateOf("")
    var hora by mutableStateOf("")

    val temLocal: Boolean get() = local.isNotBlank()
    val temHora: Boolean get() = hora.isNotBlank()

    val indiceSol: Int get() = indiceSigno(DIA_NASCIMENTO, MES_NASCIMENTO)

    // Lua e ascendente dependem de cálculo astronômico de verdade; aqui é só uma simulação
    // determinística a partir do local/hora digitados.
    val indiceLua: Int? get() = if (temLocal) (indiceSol + 4 + local.trim().length) % 12 else null
    val indiceAscendente: Int?
        get() = if (temLocal && temHora) {
            ((hora.filter { it.isDigit() }.toIntOrNull() ?: 0) + local.trim().length) % 12
        } else null

    // dia em que começa o signo que "abre" cada mês (jan..dez) e qual signo é esse
    private val inicioPorMes = intArrayOf(20, 19, 21, 20, 21, 21, 23, 23, 23, 23, 22, 22)
    private val signoQueComecaNoMes = intArrayOf(10, 11, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9)

    fun indiceSigno(dia: Int, mes: Int): Int {
        val m = mes - 1
        return if (dia >= inicioPorMes[m]) signoQueComecaNoMes[m] else signoQueComecaNoMes[(m + 11) % 12]
    }

    fun tituloCompatibilidade(signoDele: String): String = "${SIGNOS[indiceSol]} e $signoDele"

    /** Texto de compatibilidade entre o signo do usuário e o do perfil (null se o signo for desconhecido). */
    fun compatibilidade(signoDele: String): String? {
        val b = SIGNOS.indexOf(signoDele)
        if (b < 0) return null
        val a = indiceSol
        val d = abs(a - b)
        return when (minOf(d, 12 - d)) {
            0 -> "Mesmo signo: vocês se reconhecem um no outro e se entendem quase sem falar."
            1 -> "Como seus signos são adjacentes, os astros indicam que vocês podem ter uma conexão divertida e cheia de sedução."
            2 -> "Os astros indicam uma boa parceria, com conversa fácil e vontade de curtir as mesmas coisas."
            3 -> "Seus signos formam um ângulo de tensão: pode rolar faísca, mas vão precisar de paciência."
            4 -> "Vocês compartilham o elemento ${ELEMENTOS[a % 4]}, o que indica sintonia natural e muita afinidade."
            5 -> "Signos bem diferentes: com curiosidade e jogo de cintura, essa combinação pode surpreender."
            else -> "Signos opostos se atraem: os astros indicam muita química e muito pra aprender um com o outro."
        }
    }
}