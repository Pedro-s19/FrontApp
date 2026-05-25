package com.example.finalpro.Ui1.Components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EditCalendar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finalpro.Ui1.Screens.Auth.financeTextFieldColors
import com.example.finalpro.Ui1.Theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarMetaSheet(
    onDismiss: () -> Unit,
    onConfirm: (nombre: String, montoObjetivo: Double, fechaLimite: String?) -> Unit,
    balanceDisponible: Double = 0.0
) {
    var nombre by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var fechaLimite by remember { mutableStateOf("") }
    var datePickerOpen by remember { mutableStateOf(false) }
    var showWarning by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = BgSurface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Nueva meta de ahorro", style = MaterialTheme.typography.headlineSmall, color = TextPrimary)

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = financeTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            AmountTextField(
                value = monto,
                onValueChange = { monto = it },
                label = "Monto objetivo (COP)",
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fechaLimite,
                onValueChange = {},
                label = { Text("Fecha límite (opcional)") },
                readOnly = true,
                shape = RoundedCornerShape(14.dp),
                colors = financeTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { datePickerOpen = true }) {
                        Icon(Icons.Rounded.EditCalendar, null, tint = GreenPrimary)
                    }
                }
            )

            if (datePickerOpen) {
                DatePickerDialog(
                    onDismiss = { datePickerOpen = false },
                    onConfirm = { fechaLimite = it.toString(); datePickerOpen = false }
                )
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f).height(52.dp)
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = {
                        monto.toDoubleOrNull()?.let { m ->
                            if (balanceDisponible > 0 && m > balanceDisponible) {
                                showWarning = true
                            } else {
                                onConfirm(nombre, m, fechaLimite.ifEmpty { null })
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    modifier = Modifier.weight(1f).height(52.dp)
                ) {
                    Text("Guardar")
                }
            }
        }
    }

    if (showWarning) {
        AlertDialog(
            onDismissRequest = { showWarning = false },
            title = { Text("Advertencia", color = TextPrimary) },
            text = { Text("El monto objetivo supera tu balance disponible. ¿Deseas continuar?", color = TextSecondary) },
            confirmButton = {
                TextButton(onClick = {
                    showWarning = false
                    monto.toDoubleOrNull()?.let { m ->
                        onConfirm(nombre, m, fechaLimite.ifEmpty { null })
                    }
                }) { Text("Sí, continuar") }
            },
            dismissButton = {
                TextButton(onClick = { showWarning = false }) { Text("Cancelar") }
            },
            containerColor = BgCard
        )
    }
}