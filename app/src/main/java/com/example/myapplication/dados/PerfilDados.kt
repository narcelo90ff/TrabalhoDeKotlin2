package com.example.myapplication.dados

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myapplication.R

/** Tipos de lista que o usuário pode editar no perfil (telas do vídeo). */
enum class TipoSelecao(
    val titulo: String,
    val maximo: Int,
    val placeholderBusca: String
) {
    HOBBIES("Meus hobbies", 10, "Pesquisar hobbies"),
    SERIES("Séries que estou assistindo", 6, "Pesquisar programas/séries"),
    FILMES("Filmes que eu curto", 6, "Pesquisar filmes"),
    TIMES("Torcendo por", 6, "Pesquisar times"),
    JOGOS("Atualmente jogando", 6, "Pesquisar jogos"),
    MUSICAS("Escolher música", 6, "Buscar")
}

class CategoriaHobby(val nome: String, val itens: List<String>)

object PerfilDados {
    const val LIMITE_BIO = 500

    var bio by mutableStateOf("")
    var perguntaEscolhida by mutableStateOf<String?>(null)
    var respostaPergunta by mutableStateOf("")

    const val PERGUNTA_ENCONTRO = "O melhor primeiro encontro dessa cidade é..."
    var respostaEncontro by mutableStateOf("O melhor seria num lugar calmo, com café e boa conversa.")

    val hobbies = mutableStateListOf<String>()
    val series = mutableStateListOf<String>()
    val filmes = mutableStateListOf<String>()
    val times = mutableStateListOf<String>()
    val jogos = mutableStateListOf<String>()
    val musicas = mutableStateListOf<String>()

    private val capasSeries = mapOf(
        "Bridgerton" to R.drawable.bridgerton,
        "Stranger Things" to R.drawable.stranger_things,
        "Wandinha" to R.drawable.wandinha,
        "Me Conte Mentiras" to R.drawable.me_conta_mentiras,
        "The Pitt" to R.drawable.the_pitt,
        "Emily in Paris" to R.drawable.emily_in_paris,
        "The Big Bang Theory" to R.drawable.the_big_bang_theory,
        "The Office" to R.drawable.the_office,
        "Grey's Anatomy" to R.drawable.grey_s_anatomy,
        "Breaking Bad" to R.drawable.breaking_bad,
        "Sex and the City" to R.drawable.sex_and_the_city,
        "Round 6" to R.drawable.round_6,
        "La Casa de Papel" to R.drawable.la_casa_de_papel,
        "Friends" to R.drawable.friends,
        "Game of Thrones" to R.drawable.game_of_thrones,
        "The Last of Us" to R.drawable.the_last_of_us
    )

    private val capasFilmes = mapOf(
        "Ainda Estou Aqui" to R.drawable.ainda_estou_aqui,
        "O Agente Secreto" to R.drawable.o_agente_secreto,
        "As Branquelas" to R.drawable.as_branquelas,
        "O Senhor dos Anéis: O Retorno do Rei" to R.drawable.o_senhor_de_aneis,
        "Divertida Mente" to R.drawable.divertamente,
        "Deadpool & Wolverine" to R.drawable.deadpool_e_wolverine,
        "Barbie" to R.drawable.barbie,
        "Oppenheimer" to R.drawable.oppenheimer,
        "F1" to R.drawable.f1_filme,
        "Velozes e Furiosos" to R.drawable.velozes_e_furiosos,
        "Avatar: Fogo e Cinzas" to R.drawable.avatar_fogo_e_cinzas,
        "Wicked" to R.drawable.wicked,
        "Toy Story 4" to R.drawable.toy_story,
        "Se Beber, Não Case" to R.drawable.se_beber_nao_case,
        "Marley & Eu" to R.drawable.marley_e_eu,
        "Interestelar" to R.drawable.interestelar
    )

    private val escudosTimes = mapOf(
        "Flamengo" to R.drawable.flamengo,
        "Corinthians" to R.drawable.corinthians,
        "São Paulo" to R.drawable.sao_paulo,
        "Vasco da Gama" to R.drawable.vasco,
        "Grêmio" to R.drawable.gremio,
        "Palmeiras" to R.drawable.palmeiras,
        "Internacional" to R.drawable.internacional,
        "Cruzeiro" to R.drawable.cruzeiro,
        "Santos" to R.drawable.santos,
        "Atlético Mineiro" to R.drawable.atletico_mineiro,
        "Real Madrid" to R.drawable.real_madrid,
        "Barcelona" to R.drawable.barcelona,
        "Manchester United" to R.drawable.manchester_united,
        "PSG" to R.drawable.psg,
        "Liverpool" to R.drawable.liverpool,
        "Botafogo" to R.drawable.botafogo,
        "Fluminense" to R.drawable.fluminense,
        "Bahia" to R.drawable.bahia
    )

    private val capasJogos = mapOf(
        "FIFA" to R.drawable.fifa,
        "Minecraft" to R.drawable.minecraft,
        "Clash Royale" to R.drawable.clash_royale,
        "Grand Theft Auto V" to R.drawable.grand_theft_auto_v,
        "Subway Surfers" to R.drawable.subway_surfers,
        "Call of Duty: Warzone" to R.drawable.call_of_duty_warzone,
        "Counter-Strike 2" to R.drawable.counter_strike_2,
        "League of Legends" to R.drawable.league_of_legends,
        "Red Dead Redemption II" to R.drawable.red_dead_redemption_2,
        "Pokémon Scarlet e Violet" to R.drawable.pokemon_scarlet_e_violet,
        "The Sims" to R.drawable.the_sims_4,
        "Fortnite" to R.drawable.fortnite,
        "Free Fire" to R.drawable.free_fire,
        "Valorant" to R.drawable.valorant
    )

    /** Capa (drawable) do item, ou null quando não há imagem. */
    fun capa(tipo: TipoSelecao, nome: String): Int? = when (tipo) {
        TipoSelecao.SERIES -> capasSeries[nome]
        TipoSelecao.FILMES -> capasFilmes[nome]
        TipoSelecao.TIMES -> escudosTimes[nome]
        TipoSelecao.JOGOS -> capasJogos[nome]
        else -> null
    }

    fun lista(tipo: TipoSelecao) = when (tipo) {
        TipoSelecao.HOBBIES -> hobbies
        TipoSelecao.SERIES -> series
        TipoSelecao.FILMES -> filmes
        TipoSelecao.TIMES -> times
        TipoSelecao.JOGOS -> jogos
        TipoSelecao.MUSICAS -> musicas
    }

    fun catalogo(tipo: TipoSelecao): List<String> = when (tipo) {
        TipoSelecao.HOBBIES -> categoriasHobbies.flatMap { it.itens }
        TipoSelecao.SERIES -> catalogoSeries
        TipoSelecao.FILMES -> catalogoFilmes
        TipoSelecao.TIMES -> catalogoTimes
        TipoSelecao.JOGOS -> catalogoJogos
        TipoSelecao.MUSICAS -> catalogoMusicas
    }

    /** Texto resumo usado nos cartões do perfil: "Primeiro, +N". */
    fun resumo(lista: List<String>, vazio: String): String = when {
        lista.isEmpty() -> vazio
        lista.size == 1 -> lista[0]
        else -> "${lista[0]}, +${lista.size - 1}"
    }

    val perguntas = listOf(
        "A minha opinião mais polêmica é...",
        "Tenho a mesma energia que...",
        "Meu fim de semana ideal inclui...",
        "Você concorda ou discorda que...",
        "Meu momento efeito borboleta...",
        "Meus amigos diriam que eu sou...",
        "Eu me sinto mais à vontade quando...",
        "Um livro que reflete a minha vida...",
        "Na minha opinião, a maior green flag é...",
        "Posso ganhar de você em um jogo de...",
        "Vamos debater...",
        "Onde eu vou para me sentir à vontade...",
        "Pode confiar em mim...",
        "Quando a gente se encontrar, vou te contar sobre aquela vez em que eu...",
        "Somos esquisitos do mesmo jeito se você...",
        "Uma coisa surpreendente sobre mim é...",
        "A história do começo da minha vilania...",
        "Meu hábito diário mais caótico é...",
        "Eu me empolgo quando...",
        "Vantagens de sair comigo...",
        "Você deve saber que eu...",
        "Meu hiperfoco atual é...",
        "Quero aprender mais sobre...",
        "Não tô de férias e sim...",
        "A vibe que quero trazer para o nosso primeiro encontro é..."
    )

    val categoriasHobbies = listOf(
        CategoriaHobby("Bem-estar e estilo de vida", listOf("Amor próprio", "Experimentar coisas novas", "Spa", "Meditação", "Skincare")),
        CategoriaHobby("Comida e bebida", listOf("Foodie", "Tours gastronômicos", "Comida de rua", "Churrasco", "Sorvete", "Café", "Cerveja artesanal")),
        CategoriaHobby("Criatividade", listOf("Trabalho freelance", "Tatuagens", "Desenho", "Fotografia", "Escrever")),
        CategoriaHobby("Games", listOf("E-sports", "PlayStation", "Fortnite", "Among Us", "Atari", "Xbox")),
        CategoriaHobby("Música", listOf("Pisadinha", "Pagode", "Brega Funk", "MPB", "Música gospel", "Bandas", "Rock", "Sertanejo")),
        CategoriaHobby("Redes sociais e conteúdo", listOf("Podcasts", "Instagram", "X", "YouTube", "TikTok")),
        CategoriaHobby("Sair e se divertir", listOf("Sextou", "Rolês", "Escape rooms", "Bares", "Brechós", "Museus")),
        CategoriaHobby("TV e cinema", listOf("Big Brother Brasil", "Filmes de ação", "Filmes de animação", "Séries de crime")),
        CategoriaHobby("Valores e causas", listOf("Igualdade", "Desenvolvimento social", "Direitos humanos", "Direitos LGBTQIA+"))
    )

    val catalogoSeries = listOf(
        "Bridgerton", "Stranger Things", "Wandinha", "Me Conte Mentiras", "The Pitt", "Emily in Paris",
        "The Big Bang Theory", "The Office", "Grey's Anatomy", "Breaking Bad", "Sex and the City",
        "Round 6", "La Casa de Papel", "Friends", "Game of Thrones", "The Last of Us"
    )

    val catalogoFilmes = listOf(
        "Ainda Estou Aqui", "O Agente Secreto", "As Branquelas", "O Senhor dos Anéis: O Retorno do Rei",
        "Divertida Mente", "Deadpool & Wolverine", "Barbie", "Oppenheimer", "F1", "Velozes e Furiosos",
        "Avatar: Fogo e Cinzas", "Wicked", "Toy Story 4", "Se Beber, Não Case", "Marley & Eu", "Interestelar"
    )

    val catalogoTimes = listOf(
        "Flamengo", "Corinthians", "São Paulo", "Vasco da Gama", "Grêmio", "Palmeiras", "Internacional",
        "Cruzeiro", "Santos", "Atlético Mineiro", "Real Madrid", "Barcelona", "Manchester United",
        "PSG", "Liverpool", "Botafogo", "Fluminense", "Bahia"
    )

    val catalogoJogos = listOf(
        "FIFA", "Minecraft", "Clash Royale", "Grand Theft Auto V", "Subway Surfers", "Call of Duty: Warzone",
        "Counter-Strike 2", "League of Legends", "Red Dead Redemption II", "Pokémon Scarlet e Violet", "The Sims",
        "Fortnite", "Free Fire", "Valorant"
    )

    val catalogoMusicas = listOf(
        "Stop The Wedding! — Ashe", "the cure — Olivia Rodrigo", "Broken Window Serenade — Whiskey Myers",
        "Mr. Brightside — The Killers", "Ain't In LA — ADÉLA", "Patient Zero — Taylor Swift",
        "Sweater Weather — The Neighbourhood", "Man I Need — Olivia Dean", "Fade Into You — Mazzy Star",
        "Power Trip — J. Cole, Miguel", "Willing and Able — Noah Kahan", "Linger — The Cranberries",
        "Everlong — Foo Fighters"
    )
}