package com.example.myapplication.navegacao


object Rotas {
    const val DESCOBRIR = "descobrir"
    const val EXPLORAR = "explorar"
    const val CURTIDAS = "curtidas"
    const val CONVERSAS = "conversas"
    const val PERFIL = "perfil"
    const val DOUBLE_DATE = "double_date"
    const val DOUBLE_DATE_CONFIG = "double_date_config"
    const val ASTROLOGIA = "astrologia"
    const val CONFIGURACOES = "configuracoes"
    const val FILTROS = "filtros"
    const val LOCALIZACAO = "localizacao"
    const val ASSINATURA = "assinatura/{plano}"
    const val COMPRA = "compra/{tipo}"
    const val PAGAMENTO = "pagamento/{compraId}"

    const val DETALHE_CURTIDA = "detalhe_curtida/{pessoaId}"
    const val DETALHE_CONVERSA = "detalhe_conversa/{conversaId}"
    const val EXPLORAR_DETALHE = "explorar_detalhe/{categoria}"

    fun detalheCurtida(pessoaId: Int) = "detalhe_curtida/$pessoaId"
    fun detalheConversa(conversaId: Int) = "detalhe_conversa/$conversaId"
    fun explorarDetalhe(categoria: String) = "explorar_detalhe/$categoria"
    /** plano: "PLATINUM", "GOLD", "PLUS" ou "ANONIMO" */
    fun assinatura(plano: String) = "assinatura/$plano"
    /** tipo: "BOOST" ou "SUPERLIKE" */
    fun compra(tipo: String) = "compra/$tipo"
    /** compraId: id da compra pendente salva no banco (Room). */
    fun pagamento(compraId: Long) = "pagamento/$compraId"
}