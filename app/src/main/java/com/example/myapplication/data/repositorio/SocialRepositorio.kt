package com.example.myapplication.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.myapplication.dados.AppDados
import com.example.myapplication.data.local.*
import com.example.myapplication.modelo.Conversa
import com.example.myapplication.modelo.Fotos
import com.example.myapplication.modelo.Mensagem
import com.example.myapplication.modelo.Perfil
import com.example.myapplication.modelo.Pessoa
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class ResumoConversa(
    val conversa: Conversa,
    val ultimaMensagem: String?,
    val pessoa: Pessoa?
)

class SocialRepositorio private constructor(
    private val pessoaDao: PessoaDao,
    private val conversaDao: ConversaDao,
    private val mensagemDao: MensagemDao,
    private val prefs: SharedPreferences
) {

    // Roda no IO e, se der erro, devolve o valor padrão (sem derrubar o app)
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

    // ---------- Inicialização ----------

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
        val beatriz = pessoaDao.inserir(Pessoa(nome = "Beatriz", idade = 24, bio = "Curtiu você de volta!")).toInt()
        val camila = pessoaDao.inserir(Pessoa(nome = "Camila", idade = 27, bio = "Match perfeito!")).toInt()

        val convBeatriz = conversaDao.inserir(Conversa(pessoaId = beatriz, nome = "Beatriz")).toInt()
        mensagemDao.inserir(Mensagem(conversaId = convBeatriz, texto = "Oii! Tudo bem?", minha = false))

        val convCamila = conversaDao.inserir(Conversa(pessoaId = camila, nome = "Camila")).toInt()
        mensagemDao.inserir(Mensagem(conversaId = convCamila, texto = "Combinado então", minha = false))
    }

    suspend fun atualizarContagens() {
        val (curtidas, conversas) = io<Pair<Int, Int>?>(null) {
            pessoaDao.contar() to conversaDao.contar()
        } ?: return
        AppDados.totalCurtidas = curtidas
        AppDados.totalConversas = conversas
    }

    // ---------- Pessoas ----------

    suspend fun listarPessoas(): List<Pessoa> =
        io(emptyList()) { pessoaDao.buscarTodas() }

    suspend fun buscarPessoa(id: Int): Pessoa? =
        io(null) { pessoaDao.buscarPorId(id) }

    suspend fun adicionarPessoa(nome: String, idade: Int, bio: String): Pessoa? {
        val nova = io<Pessoa?>(null) {
            val p = Pessoa(nome = nome.trim(), idade = idade, bio = bio)
            p.copy(id = pessoaDao.inserir(p).toInt())
        }
        atualizarContagens()
        return nova
    }

    suspend fun atualizarPessoa(pessoa: Pessoa) =
        io(Unit) { pessoaDao.atualizar(pessoa) }

    suspend fun alternarFavorito(pessoa: Pessoa) =
        atualizarPessoa(pessoa.copy(favorito = !pessoa.favorito))

    suspend fun removerPessoa(pessoa: Pessoa) {
        io(Unit) { pessoaDao.deletar(pessoa) }
        atualizarContagens()
    }

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
                        distanciaKm = perfil.distancia.filter { it.isDigit() }.toIntOrNull(), // "a 2 km" -> 2
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

    suspend fun listarResumos(): List<ResumoConversa> =
        io(emptyList()) {
            conversaDao.buscarTodas().map { conversa ->
                ResumoConversa(
                    conversa = conversa,
                    ultimaMensagem = mensagemDao.ultimaDaConversa(conversa.id)?.texto,
                    pessoa = conversa.pessoaId?.let { pessoaDao.buscarPorId(it) }
                )
            }
        }

    suspend fun buscarConversa(id: Int): Conversa? =
        io(null) { conversaDao.buscarPorId(id) }

    suspend fun criarConversa(nome: String): Conversa? {
        val nova = io<Conversa?>(null) {
            val c = Conversa(nome = nome.trim())
            c.copy(id = conversaDao.inserir(c).toInt())
        }
        atualizarContagens()
        return nova
    }

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
        if (limpo.isNotEmpty()) {
            io(Unit) { conversaDao.atualizar(conversa.copy(nome = limpo)) }
        }
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
        if (limpo.isNotEmpty()) {
            io(Unit) { mensagemDao.inserir(Mensagem(conversaId = conversaId, texto = limpo, minha = true)) }
        }
    }

    companion object {
        private const val TAG = "SocialRepositorio"
        private const val CHAVE_SEED = "seed_social_feito"

        fun obter(contexto: Context) = with(AppDatabase.getDatabase(contexto)) {
            SocialRepositorio(
                pessoaDao = pessoaDao(),
                conversaDao = conversaDao(),
                mensagemDao = mensagemDao(),
                prefs = contexto.applicationContext.getSharedPreferences("social", Context.MODE_PRIVATE)
            )
        }
    }
}