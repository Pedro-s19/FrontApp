package com.example.finalpro.Ui1.Screens.Presupuesto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.finalpro.Data.Remote.Dto.Response.CategoriaResponse
import com.example.finalpro.Data.Remote.Dto.Response.PresupuestoResponse
import com.example.finalpro.Ui1.Components.AmountTextField
import com.example.finalpro.Ui1.Components.BottomNavBar
import com.example.finalpro.Ui1.Theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresupuestoScreen(
    navController: NavController,
    vm: PresupuestoViewModel = hiltViewModel()
) {
    val presupuestos by vm.presupuestos.collectAsState()
    val categorias   by vm.categorias.collectAsState()
    val loading      by vm.loading.collectAsState()
    val alertas      by vm.alertas.collectAsState()
    var showSheet    by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { vm.cargarDatos() }

    // Resumen global
    val totalLimite  = presupuestos.sumOf { it.limiteMonto }
    val totalGastado = presupuestos.sumOf { it.gastadoMonto }
    val porcentajeGlobal = if (totalLimite > 0) (totalGastado / totalLimite).toFloat().coerceIn(0f, 1f) else 0f
    val colorGlobal = when {
        porcentajeGlobal >= 1f    -> ColorGasto
        porcentajeGlobal >= 0.80f -> ColorWarning
        else                      -> ColorIngreso
    }

    Scaffold(
        containerColor = BgPrimary,
        topBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(BgSurface)
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp, bottom = 16.dp)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Límites", style = MaterialTheme.typography.headlineMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { vm.cargarDatos() }) {
                        Icon(Icons.Rounded.Refresh, null, tint = TextSecondary)
                    }
                }

                // Banner global de resumen
                if (presupuestos.isNotEmpty()) {
                    Spacer(Modifier.height(14.dp))
                    Card(
                        Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colorGlobal.copy(alpha = 0.06f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colorGlobal.copy(alpha = 0.3f))
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text("Gasto total vs límites", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        "${formatMoney(totalGastado, presupuestos.firstOrNull()?.moneda ?: "COP")} / ${formatMoney(totalLimite, presupuestos.firstOrNull()?.moneda ?: "COP")}",
                                        style = MaterialTheme.typography.titleMedium, color = colorGlobal, fontWeight = FontWeight.Bold
                                    )
                                }
                                Box(
                                    Modifier.size(48.dp).clip(CircleShape).background(colorGlobal.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("${(porcentajeGlobal * 100).toInt()}%", color = colorGlobal, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Box(
                                Modifier.fillMaxWidth().height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)).background(Border)
                            ) {
                                Box(
                                    Modifier.fillMaxWidth(porcentajeGlobal).height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Brush.horizontalGradient(listOf(colorGlobal, colorGlobal.copy(alpha = 0.6f))))
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showSheet = true }, containerColor = GreenPrimary, shape = CircleShape) {
                Icon(Icons.Rounded.Add, null, tint = Color.White)
            }
        }
    ) { padding ->
        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GreenPrimary)
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (alertas.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(8.dp))
                        Card(
                            Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = ColorWarning.copy(alpha = 0.08f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ColorWarning.copy(alpha = 0.4f))
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text("⚠️ Alertas", style = MaterialTheme.typography.titleSmall, color = ColorWarning, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(6.dp))
                                alertas.forEach { alerta ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("•", color = ColorWarning)
                                        Spacer(Modifier.width(6.dp))
                                        Text(alerta, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                                    }
                                }
                            }
                        }
                    }
                }

                item { Spacer(Modifier.height(4.dp)) }

                if (presupuestos.isEmpty()) {
                    item {
                        Box(Modifier.fillMaxWidth().padding(top = 64.dp), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("💰", fontSize = 40.sp)
                                Spacer(Modifier.height(8.dp))
                                Text("Sin límites este mes", color = TextSecondary, fontWeight = FontWeight.Bold)
                                Text("Toca + para crear uno", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }

                items(presupuestos, key = { it.id }) { presupuesto ->
                    PresupuestoDetalleCard(
                        presupuesto = presupuesto,
                        onEdit = { nuevoLimite -> vm.actualizarPresupuesto(presupuesto.id, nuevoLimite) },
                        onDelete = { vm.eliminarPresupuesto(presupuesto.id) }
                    )
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }

    if (showSheet) {
        AgregarPresupuestoSheet(
            categorias = categorias,
            onDismiss = { showSheet = false },
            onConfirm = { catId, limite ->
                vm.crearPresupuesto(catId, limite)
                showSheet = false
            }
        )
    }
}

@Composable
fun PresupuestoDetalleCard(presupuesto: PresupuestoResponse, onEdit: (Double) -> Unit, onDelete: () -> Unit) {
    val porcentaje = if (presupuesto.limiteMonto > 0)
        (presupuesto.gastadoMonto / presupuesto.limiteMonto).toFloat().coerceIn(0f, 1f) else 0f

    val barColor = when {
        porcentaje >= 1f    -> ColorGasto
        porcentaje >= 0.80f -> ColorWarning
        else                -> ColorIngreso
    }
    val restante = (presupuesto.limiteMonto - presupuesto.gastadoMonto).coerceAtLeast(0.0)
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp, if (porcentaje >= 1f) ColorGasto.copy(alpha = 0.4f) else Border
        )
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(barColor.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.PieChart, null, tint = barColor, modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(presupuesto.categoriaNombre, style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                        Text("${presupuesto.anio} · Mes ${presupuesto.mes}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    }
                }
                Row {
                    IconButton(onClick = { showEditDialog = true }, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Rounded.Edit, null, tint = GreenPrimary, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Rounded.Delete, null, tint = ColorGasto.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            Box(Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(Border)) {
                Box(
                    Modifier.fillMaxWidth(porcentaje).height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Brush.horizontalGradient(listOf(barColor, barColor.copy(alpha = 0.6f))))
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Gastado", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text(formatMoney(presupuesto.gastadoMonto, presupuesto.moneda), color = barColor, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Usado", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text("${(porcentaje * 100).toInt()}%", color = barColor, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(if (porcentaje >= 1f) "Excedido" else "Restante", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text(
                        formatMoney(restante, presupuesto.moneda),
                        color = if (porcentaje >= 1f) ColorGasto else ColorIngreso,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            when {
                porcentaje >= 1f -> {
                    Spacer(Modifier.height(10.dp))
                    Row(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                            .background(ColorGasto.copy(alpha = 0.08f))
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.Warning, null, tint = ColorGasto, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("¡Límite superado!", color = ColorGasto, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
                porcentaje >= 0.80f -> {
                    Spacer(Modifier.height(10.dp))
                    Row(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                            .background(ColorWarning.copy(alpha = 0.08f))
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.Info, null, tint = ColorWarning, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Cerca del límite", color = ColorWarning, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }

    if (showEditDialog) {
        EditarPresupuestoDialog(
            limiteActual = presupuesto.limiteMonto,
            onDismiss = { showEditDialog = false },
            onConfirm = { onEdit(it); showEditDialog = false }
        )
    }
}

@Composable
fun EditarPresupuestoDialog(limiteActual: Double, onDismiss: () -> Unit, onConfirm: (Double) -> Unit) {
    var texto by remember { mutableStateOf(limiteActual.toInt().toString()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = BgSurface,
        title = { Text("Editar límite", color = TextPrimary, fontWeight = FontWeight.Bold) },
        text = { AmountTextField(value = texto, onValueChange = { texto = it }, label = "Nuevo límite") },
        confirmButton = {
            Button(
                onClick = { texto.toDoubleOrNull()?.let { onConfirm(it) } },
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = TextSecondary) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarPresupuestoSheet(categorias: List<CategoriaResponse>, onDismiss: () -> Unit, onConfirm: (String, Double) -> Unit) {
    var categoriaSeleccionada by remember { mutableStateOf(categorias.firstOrNull()) }
    var limiteText by remember { mutableStateOf("") }
    var expandedDropdown by remember { mutableStateOf(false) }

    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = BgSurface) {
        Column(Modifier.padding(horizontal = 24.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Nuevo límite", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)

            ExposedDropdownMenuBox(expanded = expandedDropdown, onExpandedChange = { expandedDropdown = it }) {
                OutlinedTextField(
                    value = categoriaSeleccionada?.nombre ?: "Selecciona una categoría",
                    onValueChange = {}, readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary, unfocusedBorderColor = Border,
                        focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary,
                        focusedContainerColor = BgCardAlt, unfocusedContainerColor = BgCardAlt
                    )
                )
                ExposedDropdownMenu(expanded = expandedDropdown, onDismissRequest = { expandedDropdown = false },
                    modifier = Modifier.background(BgSurface)) {
                    categorias.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text("${cat.icono ?: "📂"} ${cat.nombre}", color = TextPrimary) },
                            onClick = { categoriaSeleccionada = cat; expandedDropdown = false }
                        )
                    }
                }
            }

            AmountTextField(value = limiteText, onValueChange = { limiteText = it }, label = "Límite mensual", modifier = Modifier.fillMaxWidth())

            Button(
                onClick = {
                    val limite = limiteText.toDoubleOrNull()
                    val cat = categoriaSeleccionada
                    if (limite != null && cat != null) onConfirm(cat.id, limite)
                },
                enabled = limiteText.toDoubleOrNull() != null && categoriaSeleccionada != null,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary, disabledContainerColor = Border)
            ) { Text("Crear límite", fontWeight = FontWeight.Bold) }
        }
    }
}