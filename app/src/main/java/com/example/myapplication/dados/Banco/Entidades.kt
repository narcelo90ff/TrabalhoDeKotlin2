package com.example.myapplication.dados.banco

import androidx.room3.Entity
import androidx.room3.PrimaryKey

object TipoCompra {
    const val BOOST = "BOOST"
    const val SUPERLIKE = "SUPERLIKE"
    const val PLANO = "PLANO"
}

object StatusCompra {
    const val PENDENTE = "PENDENTE"
    const val PAGO = "PAGO"
    const val CANCELADO = "CANCELADO"
}

@Entity(tableName = "compras")
data class CompraEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tipo: String,
    val item: String,
    val quantidade: Int = 0,
    val plano: String? = null,
    val valorCentavos: Long,
    val codigoPix: String,
    val status: String = StatusCompra.PENDENTE,
    val criadoEm: Long = System.currentTimeMillis(),
    val pagoEm: Long? = null
)

@Entity(tableName = "carteira")
data class CarteiraEntity(
    @PrimaryKey val id: Int = 1,
    val superLikes: Int = 0,
    val boosts: Int = 0,
    val plano: String? = null
)