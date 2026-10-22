package com.example.myapplication.navegacao


object Rotas {
    const val DESCOBRIR = "descobrir"
    const val EXPLORAR = "explorar"
    const val CURTIDAS = "curtidas"
    const val CONVERSAS = "conversas"
    const val PERFIL = "perfil"
    const val DOUBLE_DATE = "double_date"
    const val DOUBLE_DATE_CONFIG = "double_date_config"
    const val CENTRAL_SEGURANCA = "central_seguranca"
    const val CONFIG_MENSAGENS = "config_mensagens"
    const val ASTROLOGIA = "astrologia"
    const val CONFIGURACOES = "configuracoes"
    const val FILTROS = "filtros"
    const val LOCALIZACAO = "localizacao"
    const val ASSINATURA = "assinatura/{plano}"
    const val COMPRA = "compra/{tipo}"
    const val PAGAMENTO = "pagamento/{compraId}"
    const val EDITAR_BIO = "editar_bio"
    const val EDITAR_PERGUNTA = "editar_pergunta"
    const val EDITAR_ENCONTRO = "editar_encontro"
    const val SELECIONAR = "selecionar/{tipo}"

    const val DETALHE_CURTIDA = "detalhe_curtida/{pessoaId}"
    const val DETALHE_CONVERSA = "detalhe_conversa/{conversaId}"
    const val EXPLORAR_DETALHE = "explorar_detalhe/{categoria}"

    fun detalheCurtida(pessoaId: Int) = "detalhe_curtida/$pessoaId"
    fun detalheConversa(conversaId: Int) = "detalhe_conversa/$conversaId"
    fun explorarDetalhe(categoria: String) = "explorar_detalhe/$categoria"
    fun assinatura(plano: String) = "assinatura/$plano"
    fun compra(tipo: String) = "compra/$tipo"
    fun pagamento(compraId: Long) = "pagamento/$compraId"
    fun selecionar(tipo: String) = "selecionar/$tipo"
}