package com.example.myapplication.navegacao

/**
 * Todas as rotas do app em um só lugar, como pedido no trabalho.
 * As rotas de detalhe levam um argumento (id) de verdade na URL da rota.
 */
object Rotas {
    const val DESCOBRIR = "descobrir"
    const val EXPLORAR = "explorar"
    const val CURTIDAS = "curtidas"
    const val CONVERSAS = "conversas"
    const val PERFIL = "perfil"
    const val DOUBLE_DATE = "double_date"
    const val CONFIGURACOES = "configuracoes"
    const val FILTROS = "filtros"

    const val DETALHE_CURTIDA = "detalhe_curtida/{pessoaId}"
    const val DETALHE_CONVERSA = "detalhe_conversa/{conversaId}"
    const val EXPLORAR_DETALHE = "explorar_detalhe/{categoria}"

    fun detalheCurtida(pessoaId: Int) = "detalhe_curtida/$pessoaId"
    fun detalheConversa(conversaId: Int) = "detalhe_conversa/$conversaId"
    fun explorarDetalhe(categoria: String) = "explorar_detalhe/$categoria"
}