package com.example.finalpro.Ui1.Components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.finalpro.Ui1.Theme.*

@Composable
fun TransaccionItem(
    descripcion: String,
    monto: String,
    fecha: String,
    categoria: String,
    icono: String?,
    colorMonto: Color,
    onDelete: () -> Unit,
    onEdit: ((monto: Double, descripcion: String, fecha: String) -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var editando by remember { mutableStateOf(false) }

    // Extraer valor numerico del monto mostrado (quitar signo y moneda)
    val montoNumerico = monto.filter { it.isDigit() || it == '.' || it == ',' }
        .replace(",", "").toDoubleOrNull() ?: 0.0

    var editMonto by remember { mutableStateOf(montoNumerico.toString()) }
    var editDesc by remember { mutableStateOf(descripcion) }
    var editFecha by remember { mutableStateOf(fecha) }

    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded; editando = false }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorMonto.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = icono ?: "💰", fontSize = MaterialTheme.typography.titleMedium.fontSize)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = descripcion,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(text = categoria, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = monto,
                        color = colorMonto,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = fecha, style = MaterialTheme.typography.labelSmall, color = TextMuted)
                }
            }

            AnimatedVisibility(visible = expanded, enter = expandVertically(), exit = shrinkVertically()) {
                Column(modifier = Modifier.padding(horizontal = 14.dp).padding(bottom = 10.dp)) {
                    if (editando && onEdit != null) {
                        OutlinedTextField(
                            value = editMonto,
                            onValueChange = { editMonto = it.filter { c -> c.isDigit() || c == '.' } },
                            label = { Text("Monto") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentPrimary,
                                unfocusedBorderColor = Border,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                        Spacer(Modifier.height(6.dp))
                        OutlinedTextField(
                            value = editDesc,
                            onValueChange = { editDesc = it },
                            label = { Text("Descripcion") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentPrimary,
                                unfocusedBorderColor = Border,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                        Spacer(Modifier.height(6.dp))
                        OutlinedTextField(
                            value = editFecha,
                            onValueChange = { editFecha = it },
                            label = { Text("Fecha (YYYY-MM-DD)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentPrimary,
                                unfocusedBorderColor = Border,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            TextButton(onClick = { editando = false }) {
                                Text("Cancelar", color = TextSecondary)
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    editMonto.toDoubleOrNull()?.let { m ->
                                        onEdit(m, editDesc, editFecha)
                                        editando = false
                                        expanded = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentPrimary),
                                shape = RoundedCornerShape(10.dp)
                            ) { Text("Guardar") }
                        }
                    } else {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            if (onEdit != null) {
                                TextButton(onClick = { editando = true }) {
                                    Icon(Icons.Rounded.Edit, null, tint = AccentPrimary, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text("Editar", color = AccentPrimary)
                                }
                                Spacer(Modifier.width(8.dp))
                            }
                            TextButton(onClick = onDelete) {
                                Icon(Icons.Rounded.Delete, null, tint = ColorGasto, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Eliminar", color = ColorGasto)
                            }
                        }
                    }
                }
            }
        }
    }
}