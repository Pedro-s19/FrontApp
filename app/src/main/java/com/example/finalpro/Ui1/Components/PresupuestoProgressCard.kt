package com.example.finalpro.Ui1.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.finalpro.Ui1.Theme.*

@Composable
fun PresupuestoProgressCard(
    categoria: String,
    limite: Double,
    gastado: Double,
    moneda: String,
    onEdit: (Double) -> Unit,
    onDelete: () -> Unit
) {
    val porcentaje = if (limite > 0) (gastado / limite) * 100 else 0.0
    val progress = (porcentaje / 100f).toFloat().coerceIn(0f, 1f)
    var editing by remember { mutableStateOf(false) }
    var nuevoLimite by remember { mutableStateOf(limite.toString()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(categoria, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Row {
                    IconButton(onClick = { editing = true }) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = null,
                            tint = AccentPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = null,
                            tint = ColorGasto,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))

            if (editing) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = nuevoLimite,
                        onValueChange = { nuevoLimite = it },
                        label = { Text("Nuevo límite") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentPrimary,
                            unfocusedBorderColor = Border,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            nuevoLimite.toDoubleOrNull()?.let { onEdit(it); editing = false }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentPrimary)
                    ) {
                        Text("Guardar")
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Border)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (porcentaje > 80) ColorWarning else AccentPrimary)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Gastado: ${formatMoney(gastado, moneda)}", color = ColorGasto)
                    Text(text = "Límite: ${formatMoney(limite, moneda)}", color = TextSecondary)
                }
                Text(
                    text = "${porcentaje.toInt()}% usado",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (porcentaje > 80) ColorWarning else AccentPrimary
                )
            }
        }
    }
}