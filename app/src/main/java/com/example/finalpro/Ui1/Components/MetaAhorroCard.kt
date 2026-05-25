package com.example.finalpro.Ui1.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalpro.Data.Remote.Dto.Response.MetaAhorroResponse
import com.example.finalpro.Ui1.Theme.*

@Composable
fun MetaAhorroCard(
    meta: MetaAhorroResponse,
    onAbonar: (Double) -> Unit,
    onDelete: () -> Unit,
    onEditar: (Double) -> Unit,
    saldoDisponible: Double = 0.0
) {
    val progreso = if (meta.montoObjetivo > 0) (meta.montoActual / meta.montoObjetivo).coerceIn(0.0, 1.0).toFloat() else 0f
    val completada = meta.montoActual >= meta.montoObjetivo

    val barColor = when {
        completada      -> ColorIngreso
        progreso > 0.75 -> GreenPrimary
        progreso > 0.40 -> ColorWarning
        else            -> ColorInfo
    }

    var montoAbono   by remember { mutableStateOf("") }
    var showAbono    by remember { mutableStateOf(false) }
    var showEdit     by remember { mutableStateOf(false) }

    val abono = montoAbono.toDoubleOrNull() ?: 0.0
    val restante = (meta.montoObjetivo - meta.montoActual).coerceAtLeast(0.0)
    val saldoInsuficiente = abono > 0 && abono > saldoDisponible

    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp, if (completada) ColorIngreso.copy(alpha = 0.3f) else Border
        )
    ) {
        Column(Modifier.padding(18.dp)) {
            // Header
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(barColor.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (completada) Icons.Rounded.CheckCircle else Icons.Rounded.EmojiEvents,
                            null, tint = barColor, modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(meta.nombre, style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                        val estadoLabel = when {
                            completada      -> "✅ Completada"
                            progreso > 0.75 -> "🔥 Próxima a completarse"
                            else            -> "En curso"
                        }
                        Text(estadoLabel, style = MaterialTheme.typography.labelSmall,
                            color = if (completada) ColorIngreso else TextSecondary)
                    }
                }
                Row {
                    if (!completada) {
                        IconButton(onClick = { showEdit = !showEdit }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Rounded.Edit, null, tint = TextMuted, modifier = Modifier.size(16.dp))
                        }
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Rounded.Delete, null, tint = ColorGasto.copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Progreso
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    formatMoney(meta.montoActual, meta.moneda),
                    style = MaterialTheme.typography.titleMedium, color = barColor, fontWeight = FontWeight.Bold
                )
                Text(
                    "${(progreso * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium, color = barColor, fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(6.dp))
            Box(
                Modifier.fillMaxWidth().height(8.dp)
                    .clip(RoundedCornerShape(4.dp)).background(Border)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(progreso)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Brush.horizontalGradient(listOf(barColor, barColor.copy(alpha = 0.6f))))
                )
            }
            Spacer(Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Ahorrado", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                Text(
                    "Objetivo: ${formatMoney(meta.montoObjetivo, meta.moneda)}",
                    style = MaterialTheme.typography.labelSmall, color = TextMuted
                )
            }

            if (completada) {
                Spacer(Modifier.height(10.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(ColorIngreso.copy(alpha = 0.08f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🏆", fontSize = 16.sp)
                    Spacer(Modifier.width(8.dp))
                    Text("¡Meta alcanzada! Logro desbloqueado", color = ColorIngreso,
                        style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
                }
            }

            if (showEdit) {
                var nuevoObjetivo by remember { mutableStateOf(meta.montoObjetivo.toInt().toString()) }
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = Border)
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = nuevoObjetivo,
                        onValueChange = { nuevoObjetivo = it },
                        label = { Text("Nuevo objetivo") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenPrimary,
                            unfocusedBorderColor = Border,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    Button(
                        onClick = {
                            nuevoObjetivo.toDoubleOrNull()?.let { onEditar(it); showEdit = false }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("OK") }
                }
            }

            // Abonar
            if (!completada) {
                Spacer(Modifier.height(12.dp))
                if (!showAbono) {
                    OutlinedButton(
                        onClick = { showAbono = true },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.5f))
                    ) {
                        Icon(Icons.Rounded.Add, null, tint = GreenPrimary, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Abonar", color = GreenPrimary, fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    HorizontalDivider(color = Border)
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = montoAbono,
                        onValueChange = { montoAbono = it },
                        label = { Text("Monto a abonar") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        isError = saldoInsuficiente,
                        supportingText = if (saldoInsuficiente) {
                            { Text("No tienes saldo suficiente. Saldo disponible: ${formatMoney(saldoDisponible, meta.moneda)}", color = ColorGasto) }
                        } else if (abono > 0 && abono > restante) {
                            { Text("Supera el restante (${formatMoney(restante, meta.moneda)})", color = ColorWarning) }
                        } else null,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenPrimary,
                            unfocusedBorderColor = Border,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            errorBorderColor = ColorGasto
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = { showAbono = false; montoAbono = "" },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(46.dp)
                        ) { Text("Cancelar") }
                        Button(
                            onClick = {
                                if (abono > 0 && !saldoInsuficiente) {
                                    onAbonar(abono)
                                    showAbono = false
                                    montoAbono = ""
                                }
                            },
                            enabled = abono > 0 && !saldoInsuficiente,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GreenPrimary,
                                disabledContainerColor = Border
                            ),
                            modifier = Modifier.weight(1f).height(46.dp)
                        ) { Text("Abonar", fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }
    }
}