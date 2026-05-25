package com.example.finalpro.Ui1.Theme

import java.text.DecimalFormat

fun formatMoney(monto: Double, moneda: String): String {
    val simbolos = mapOf("COP" to "$", "USD" to "US$", "EUR" to "€", "MXN" to "MX$")
    val simbolo = simbolos[moneda] ?: moneda
    val df = DecimalFormat("#,###.##")
    return "$simbolo ${df.format(monto)}"
}