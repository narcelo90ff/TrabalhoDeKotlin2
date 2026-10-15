package com.example.myapplication.dados.banco

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.myapplication.dados.AppDados
import com.example.myapplication.modelo.Conversa
import com.example.myapplication.modelo.Fotos
import com.example.myapplication.modelo.Mensagem
import com.example.myapplication.modelo.Perfil
import com.example.myapplication.modelo.Pessoa
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Uma linha da lista de conversas: a conversa, a última mensagem e a pessoa (se houver). */
data class ResumoConversa(
    val conversa: Conversa,
    val ultimaMensagem: String?,
    val pessoa: Pessoa?
)

/**
 * Único ponto de acesso ao banco para Curtidas (Pessoa) e Conversas/Mensagens.
 * Toda operação roda em Dispatchers.IO e tem try/catch com Log.e, então a interface
 * nunca trava nem quebra por causa de uma falha no banco.
 */
class SocialRepositorio private constructor(
    private val pessoaDao: PessoaDao,
    private val conversaDao: ConversaDao,
    private val mensagemDao: MensagemDao,
    private val prefs: SharedPreferences
) {

    private suspend fun <T> io(padrao: T, acao: suspend () -> T): T =
        withContext(Dispatchers.IO) {
            try {
                acao()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao acessar o banco de dados", e)
                padrao
            }
        }

    // ---------- inicialização ----------

    /** Na primeira abertura do app grava os dados de exemplo (os mesmos do Trabalho 2). */
    suspend fun carregarInicial() {
        io(Unit) {
            if (!prefs.getBoolean(CHAVE_SEED, false)) {
                if (pessoaDao.contar() == 0 && conversaDao.contar() == 0) semear()
                prefs.edit().putBoolean(CHAVE_SEED, true).apply()
            }
        }
        atualizarContagens()
    }

    private suspend fun semear() {
        val beatriz = pessoaDao.inserir(
            Pessoa(nome = "Beatriz", idade = 24, bio = "Curtiu você de volta!")
        ).toInt()
        val camila = pessoaDao.inserir(
            Pessoa(nome = "Camila", idade = 27, bio = "Match perfeito!")
        ).toInt()

        val conversaBeatriz = conversaDao.inserir(Conversa(pessoaId = beatriz, nome = "Beatriz")).toInt()
        mensagemDao.inserir(Mensagem(conversaId = conversaBeatriz, texto = "Oii! Tudo bem?", minha = false))

        val conversaCamila = conversaDao.inserir(Conversa(pessoaId = camila, nome = "Camila")).toInt()
        mensagemDao.inserir(Mensagem(conversaId = conversaCamila, texto = "Combinado então", minha = false))
    }

    /** Atualiza os contadores usados no badge da barra de baixo e no Perfil. */
    suspend fun atualizarContagens() {
        val totais = io<Pair<Int, Int>?>(null) { pessoaDao.contar() to conversaDao.contar() } ?: return
        AppDados.totalCurtidas = totais.first
        AppDados.totalConversas = totais.second
    }

    // ---------- Curtidas (Pessoa) ----------

    suspend fun listarPessoas(): List<Pessoa> = io(emptyList()) { pessoaDao.buscarTodas() }

    suspend fun buscarPessoa(id: Int): Pessoa? = io<Pessoa?>(null) { pessoaDao.buscarPorId(id) }

    suspend fun adicionarPessoa(nome: String, idade: Int, bio: String): Pessoa? {
        val nova = io<Pessoa?>(null) {
            val p = Pessoa(nome = nome.trim(), idade = idade, bio = bio)
            p.copy(id = pessoaDao.inserir(p).toInt())
        }
        atualizarContagens()
        return nova
    }

    suspend fun atualizarPessoa(pessoa: Pessoa) {
        io(Unit) { pessoaDao.atualizar(pessoa) }
    }

    suspend fun alternarFavorito(pessoa: Pessoa) {
        atualizarPessoa(pessoa.copy(favorito = !pessoa.favorito))
    }

    suspend fun removerPessoa(pessoa: Pessoa) {
        io(Unit) { pessoaDao.deletar(pessoa) }
        atualizarContagens()
    }

    /** Curtir um perfil do Descobrir: cria a Pessoa no banco (ou reaproveita a que já existe). */
    suspend fun curtir(perfil: Perfil, favorito: Boolean = false): Pessoa? {
        val pessoa = io<Pessoa?>(null) {
            val existente = pessoaDao.buscarPorNomeEIdade(perfil.nome, perfil.idade)
            when {
                existente == null -> {
                    val nova = Pessoa(
                        nome = perfil.nome,
                        idade = perfil.idade,
                        bio = "Deu match com você agora!",
                        fotoNome = Fotos.nomeDe(perfil.foto),
                        // "a 2 km de distância" -> 2
                        distanciaKm = perfil.distancia.filter { it.isDigit() }.toIntOrNull(),
                        favorito = favorito
                    )
                    nova.copy(id = pessoaDao.inserir(nova).toInt())
                }
                favorito && !existente.favorito -> {
                    val atualizada = existente.copy(favorito = true)
                    pessoaDao.atualizar(atualizada)
                    atualizada
                }
                else -> existente
            }
        }
        atualizarContagens()
        return pessoa
    }

    // ---------- Conversas ----------

    suspend fun listarResumos(): List<ResumoConversa> = io(emptyList()) {
        conversaDao.buscarTodas().map { conversa ->
            ResumoConversa(
                conversa = conversa,
                ultimaMensagem = mensagemDao.ultimaDaConversa(conversa.id)?.texto,
                pessoa = conversa.pessoaId?.let { pessoaDao.buscarPorId(it) }
            )
        }
    }

    suspend fun buscarConversa(id: Int): Conversa? = io<Conversa?>(null) { conversaDao.buscarPorId(id) }

    suspend fun criarConversa(nome: String): Conversa? {
        val nova = io<Conversa?>(null) {
            val c = Conversa(nome = nome.trim())
            c.copy(id = conversaDao.inserir(c).toInt())
        }
        atualizarContagens()
        return nova
    }

    /** Abre a conversa já existente com a pessoa ou cria uma nova. */
    suspend fun iniciarConversaComPessoa(pessoa: Pessoa): Conversa? {
        val conversa = io<Conversa?>(null) {
            conversaDao.buscarPorPessoaId(pessoa.id) ?: run {
                val c = Conversa(pessoaId = pessoa.id, nome = pessoa.nome)
                c.copy(id = conversaDao.inserir(c).toInt())
            }
        }
        atualizarContagens()
        return conversa
    }

    suspend fun renomearConversa(conversa: Conversa, novoNome: String) {
        val limpo = novoNome.trim()
        if (limpo.isEmpty()) return
        io(Unit) { conversaDao.atualizar(conversa.copy(nome = limpo)) }
    }

    suspend fun removerConversa(conversa: Conversa) {
        io(Unit) {
            mensagemDao.excluirDaConversa(conversa.id)
            conversaDao.deletar(conversa)
        }
        atualizarContagens()
    }

    // ---------- Mensagens ----------

    suspend fun listarMensagens(conversaId: Int): List<Mensagem> =
        io(emptyList()) { mensagemDao.buscarPorConversa(conversaId) }

    suspend fun enviarMensagem(conversaId: Int, texto: String) {
        val limpo = texto.trim()
        if (limpo.isEmpty()) return
        io(Unit) { mensagemDao.inserir(Mensagem(conversaId = conversaId, texto = limpo, minha = true)) }
    }

    companion object {
        private const val TAG = "SocialRepositorio"
        private const val CHAVE_SEED = "seed_social_feito"

        fun obter(contexto: Context): SocialRepositorio {
            val banco = AppDatabase.getInstance(contexto)
            return SocialRepositorio(
                pessoaDao = banco.pessoaDao(),
                conversaDao = banco.conversaDao(),
                mensagemDao = banco.mensagemDao(),
                prefs = contexto.applicationContext.getSharedPreferences("social", Context.MODE_PRIVATE)
            )
        }
    }
}