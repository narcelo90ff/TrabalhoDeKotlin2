package com.example.myapplication.dados.banco

import androidx.room3.Entity
import androidx.room3.PrimaryKey

/** Tipos de compra que o app vende. */
object TipoCompra {
    const val BOOST = "BOOST"
    const val SUPERLIKE = "SUPERLIKE"
    const val PLANO = "PLANO"
}

/** Situação de um pagamento. */
object StatusCompra {
    const val PENDENTE = "PENDENTE"
    const val PAGO = "PAGO"
    const val CANCELADO = "CANCELADO"
}

/** Uma compra (Boost, Super Like ou assinatura) e o código Pix gerado para ela. */
@Entity(tableName = "compras")
data class CompraEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tipo: String,
    /** Texto mostrado ao usuário, ex.: "5 Boosts" ou "Plano Gold (1 mês)". */
    val item: String,
    /** Quantidade de Boosts/Super Likes (0 para assinaturas). */
    val quantidade: Int = 0,
    /** Chave do plano (GOLD, PLUS...) quando tipo = PLANO. */
    val plano: String? = null,
    val valorCentavos: Long,
    /** Código "Pix copia e cola" (o mesmo que vai dentro do QR Code). */
    val codigoPix: String,
    val status: String = StatusCompra.PENDENTE,
    val criadoEm: Long = System.currentTimeMillis(),
    val pagoEm: Long? = null
)

/** Saldo do usuário. Sempre existe uma única linha (id = 1). */
@Entity(tableName = "carteira")
data class CarteiraEntity(
    @PrimaryKey val id: Int = 1,
    val superLikes: Int = 0,
    val boosts: Int = 0,
    val plano: String? = null
)