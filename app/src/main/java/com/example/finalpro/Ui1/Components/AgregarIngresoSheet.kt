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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarIngresoSheet(
    onDismiss: () -> Unit,
    onConfirm: (monto: Double, descripcion: String, fecha: String) -> Unit
) {
    var monto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)) }
    var datePickerOpen by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = BgSurface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Agregar ingreso", style = MaterialTheme.typography.headlineSmall, color = TextPrimary)

            AmountTextField(
                value = monto,
                onValueChange = { monto = it },
                label = "Monto (COP)",
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (opcional)") },
                shape = RoundedCornerShape(14.dp),
                colors = financeTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fecha,
                onValueChange = {},
                label = { Text("Fecha") },
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
                    onConfirm = { fecha = it.toString(); datePickerOpen = false }
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
                        monto.toDoubleOrNull()?.let { onConfirm(it, descripcion, fecha) }
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
}