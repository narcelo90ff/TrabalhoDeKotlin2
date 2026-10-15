package com.example.myapplication.dados

object Pix {
    const val CHAVE_DEMO = "00000000-0000-4000-8000-000000000000" // troque pela sua chave Pix
    const val NOME_RECEBEDOR = "MEU APLICATIVO"
    const val CIDADE = "SAO PAULO"

    fun gerarCopiaECola(
        valorCentavos: Long,
        txid: String,
        chave: String = CHAVE_DEMO,
        nome: String = NOME_RECEBEDOR,
        cidade: String = CIDADE
    ): String {
        val conta = campo("00", "br.gov.bcb.pix") + campo("01", chave)
        val semCrc =
            campo("00", "01") +
                    campo("01", "12") +
                    campo("26", conta) +
                    campo("52", "0000") +
                    campo("53", "986") +
                    campo("54", formatarValor(valorCentavos)) +
                    campo("58", "BR") +
                    campo("59", nome.take(25)) +
                    campo("60", cidade.take(15)) +
                    campo("62", campo("05", txid.take(25))) +
                    "6304"
        return semCrc + crc16(semCrc)
    }

    private fun campo(id: String, valor: String): String =
        id + valor.length.toString().padStart(2, '0') + valor

    private fun formatarValor(centavos: Long): String =
        "${centavos / 100}.${(centavos % 100).toString().padStart(2, '0')}"

    fun crc16(texto: String): String {
        var crc = 0xFFFF
        for (byte in texto.toByteArray(Charsets.UTF_8)) {
            crc = crc xor ((byte.toInt() and 0xFF) shl 8)
            repeat(8) {
                crc = if ((crc and 0x8000) != 0) (crc shl 1) xor 0x1021 else crc shl 1
                crc = crc and 0xFFFF
            }
        }
        return crc.toString(16).uppercase().padStart(4, '0')
    }
}