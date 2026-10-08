package com.example.myapplication.dados

import androidx.compose.runtime.mutableStateListOf
import com.example.myapplication.R
import com.example.myapplication.modelo.Conversa
import com.example.myapplication.modelo.Perfil
import com.example.myapplication.modelo.Pessoa


object AppDados {

    val curtidas = mutableStateListOf(
        Pessoa(id = 1, nome = "Beatriz", idade = 24, bio = "Curtiu você de volta!"),
        Pessoa(id = 2, nome = "Camila", idade = 27, bio = "Match perfeito!")
    )

    val conversas = mutableStateListOf(
        Conversa(
            id = 1,
            pessoaId = 1,
            nome = "Beatriz",
            mensagens = mutableStateListOf("Oii! Tudo bem?")
        ),
        Conversa(
            id = 2,
            pessoaId = 2,
            nome = "Camila",
            mensagens = mutableStateListOf("Combinado então")
        )
    )

    val perfis = listOf(
        Perfil(
            id = 1, nome = "Davi", idade = 19, foto = R.drawable.pessoa_1,
            distancia = "a 2 km de distância",
            procurando = "Tô procurando", tipoRelacionamento = "Algo casual",
            sobreMim = "Estudante de Educação Física, fã de academia e de macarrão no almoço de domingo.",
            altura = "1,75 m", escolaridade = "Graduando em Educação Física", genero = "Homem",
            pergunta = "Meu maior talento inútil é...",
            resposta = "decorar placa de carro depois de ver uma vez",
            bebida = "Socialmente", fuma = "Não fumo", atividadeFisica = "Todo dia", pets = "Cachorro",
            comunicacao = "Respondo rápido no WhatsApp", signo = "Áries",
            interesses = listOf("Academia", "Futebol", "Churrasco", "Videogame", "Séries", "Viagens", "Podcasts")
        ),
        Perfil(
            id = 2, nome = "Rafael", idade = 24, foto = R.drawable.pessoa_2,
            distancia = "a 5 km de distância",
            procurando = "Tô procurando", tipoRelacionamento = "Relacionamento sério",
            sobreMim = "Maquiador nas horas vagas, tatuado e apaixonado por moda e por café forte.",
            altura = "1,80 m", escolaridade = "Cursando Design de Moda", genero = "Homem",
            pergunta = "Uma opinião impopular minha é...",
            resposta = "pizza com ketchup é boa sim",
            bebida = "Gosto de vinho", fuma = "Fumo às vezes", atividadeFisica = "Às vezes", pets = "Gato",
            comunicacao = "Prefiro chamada de vídeo", signo = "Escorpião",
            interesses = listOf("Maquiagem", "Moda", "Tatuagem", "De cafeteria em cafeteria", "Cinema", "Fotografia", "Brechós")
        ),
        Perfil(
            id = 3, nome = "Kauã", idade = 21, foto = R.drawable.pessoa_3,
            distancia = "a 1 km de distância",
            procurando = "Tô procurando", tipoRelacionamento = "Ainda estou decidindo",
            sobreMim = "Sou de boa, gosto de pagode, de resenha com os amigos e de dormir tarde.",
            altura = "1,72 m", escolaridade = "Ensino médio completo", genero = "Homem",
            pergunta = "Se eu não estiver em casa, você pode me encontrar...",
            resposta = "na quadra jogando bola ou na casa de algum amigo",
            bebida = "Socialmente", fuma = "Não fumo", atividadeFisica = "Frequentemente", pets = "Papagaio",
            comunicacao = "Mando muito áudio", signo = "Leão",
            interesses = listOf("Pagode", "Futebol", "Dança", "Resenha", "Moto", "Cozinhar")
        ),
        Perfil(
            id = 4, nome = "Antônio", idade = 58, foto = R.drawable.pessoa_4,
            distancia = "a 8 km de distância",
            procurando = "Tô procurando", tipoRelacionamento = "Relacionamento sério",
            sobreMim = "Aposentado, curto assistir futebol, jogar dominó na praça e contar história de pescaria.",
            altura = "1,68 m", escolaridade = "Ensino médio completo", genero = "Homem",
            pergunta = "Meu fim de semana ideal é...",
            resposta = "pescaria de manhã e jogo do time à tarde",
            bebida = "Cerveja de vez em quando", fuma = "Não fumo", atividadeFisica = "Caminhada", pets = "Cachorro",
            comunicacao = "Gosto de falar no telefone", signo = "Capricórnio",
            interesses = listOf("Pescaria", "Futebol", "Dominó", "Churrasco", "Música sertaneja", "Jardinagem")
        ),
        Perfil(
            id = 5, nome = "Lucas", idade = 20, foto = R.drawable.pessoa_5,
            distancia = "a 3 km de distância",
            procurando = "Tô procurando", tipoRelacionamento = "Relacionamento sério",
            sobreMim = "Calmo, caseiro e viciado em série. Se tiver lanche, já ganhou meu coração.",
            altura = "1,70 m", escolaridade = "Cursando Análise de Sistemas", genero = "Homem",
            pergunta = "Se eu não estiver em casa, você pode me encontrar...",
            resposta = "no mercado comprando mais lanchinho",
            bebida = "Não curto", fuma = "Não fumo", atividadeFisica = "Sedentário", pets = "Gato",
            comunicacao = "Gosto de mandar mensagem", signo = "Libra",
            interesses = listOf("Séries", "Videogame", "Anime", "Programação", "Filmes de terror", "Lanchinhos", "Música")
        )
    )


    fun curtir(perfil: Perfil, favorito: Boolean = false): Pessoa {
        val existente = perfil.pessoaId?.let { buscarPessoa(it) }
        val pessoa = existente ?: adicionarPessoa(
            nome = perfil.nome,
            idade = perfil.idade,
            bio = "Deu match com você agora!",
            foto = perfil.foto,
            // "a 2 km de distância" -> 2
            distanciaKm = perfil.distancia.filter { it.isDigit() }.toIntOrNull()
        ).also { perfil.pessoaId = it.id }
        if (favorito) pessoa.favorito = true
        return pessoa
    }

    private var proximoIdPessoa = 3
    private var proximoIdConversa = 3

    fun adicionarPessoa(
        nome: String,
        idade: Int,
        bio: String,
        foto: Int? = null,
        distanciaKm: Int? = null
    ): Pessoa {
        val nova = Pessoa(
            id = proximoIdPessoa, nome = nome, idade = idade, bio = bio,
            foto = foto, distanciaKm = distanciaKm
        )
        proximoIdPessoa++
        curtidas.add(nova)
        return nova
    }

    fun removerPessoa(pessoa: Pessoa) {
        curtidas.remove(pessoa)
    }

    fun buscarPessoa(id: Int): Pessoa? = curtidas.find { it.id == id }

    fun adicionarConversa(nome: String, pessoaId: Int? = null): Conversa {
        val nova = Conversa(
            id = proximoIdConversa,
            pessoaId = pessoaId,
            nome = nome,
            mensagens = mutableStateListOf()
        )
        proximoIdConversa++
        conversas.add(nova)
        return nova
    }

    fun iniciarConversaComPessoa(pessoa: Pessoa): Conversa {
        val existente = conversas.find { it.pessoaId == pessoa.id }
        if (existente != null) return existente
        return adicionarConversa(nome = pessoa.nome, pessoaId = pessoa.id)
    }

    fun removerConversa(conversa: Conversa) {
        conversas.remove(conversa)
    }

    fun buscarConversa(id: Int): Conversa? = conversas.find { it.id == id }
}