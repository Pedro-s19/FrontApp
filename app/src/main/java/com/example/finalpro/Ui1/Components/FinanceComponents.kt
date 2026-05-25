package com.example.finalpro.Ui1.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finalpro.Ui1.Theme.*

@Composable
fun SaldoDisponibleBanner(saldo: Double, moneda: String) {
    val color = when {
        saldo <= 0 -> ColorGasto
        saldo < 100_000 -> ColorWarning
        else -> ColorIngreso
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.08f))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Saldo disponible", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        Text(
            formatMoney(saldo.coerceAtLeast(0.0), moneda),
            style = MaterialTheme.typography.bodySmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ErrorSaldoText(saldoDisponible: Double, moneda: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(ColorGasto.copy(alpha = 0.08f))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Rounded.Warning, null, tint = ColorGasto, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text(
            "No tienes saldo suficiente. Saldo disponible: ${formatMoney(saldoDisponible.coerceAtLeast(0.0), moneda)}",
            style = MaterialTheme.typography.labelSmall,
            color = ColorGasto,
            fontWeight = FontWeight.SemiBold
        )
    }
}