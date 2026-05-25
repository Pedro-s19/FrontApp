package com.example.finalpro.Ui1.Screens.Comparativas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.example.finalpro.Ui1.Components.BottomNavBar
import com.example.finalpro.Ui1.Theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparativasScreen(
    navController: NavController,
    vm: ComparativasViewModel = hiltViewModel()
) {
    val ingresosVsGastos by vm.ingresosVsGastos.collectAsState()
    val presupuestos by vm.presupuestos.collectAsState()
    val loading by vm.loading.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                vm.cargarDatos()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        containerColor = BgPrimary,
        topBar = {
            TopAppBar(
                title = { Text("Comparativas", fontWeight = FontWeight.Bold, color = TextPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgPrimary)
            )
        },
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AccentPrimary)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    IngresosVsGastosChart(
                        data = ingresosVsGastos,
                        moneda = "COP"
                    )
                }
                item {
                    PresupuestoVsGastoChart(presupuestos = presupuestos)
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────
// Gráfico de barras agrupadas: Ingresos vs Gastos (últimos 6 meses)
// ─────────────────────────────────────────────────────────────────
@Composable
fun IngresosVsGastosChart(data: Map<String, Pair<Double, Double>>, moneda: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Ingresos vs Gastos (últimos 6 meses)",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))

            val hasData = data.values.any { it.first > 0.0 || it.second > 0.0 }
            if (!hasData || data.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Sin movimientos en los últimos 6 meses",
                        color = TextMuted,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                return@Column
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(12.dp).background(ColorIngreso, RoundedCornerShape(2.dp)))
                    Spacer(Modifier.width(4.dp))
                    Text("Ingresos", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
                Spacer(Modifier.width(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(12.dp).background(ColorGasto, RoundedCornerShape(2.dp)))
                    Spacer(Modifier.width(4.dp))
                    Text("Gastos", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
            }
            Spacer(Modifier.height(8.dp))

            Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                val allValues = data.values.flatMap { listOf(it.first, it.second) }
                val maxVal = allValues.maxOrNull()?.takeIf { it > 0 } ?: 1.0
                val entryCount = data.size
                val groupWidth = size.width / entryCount
                val barWidth = groupWidth * 0.3f
                val spacing = 4.dp.toPx()

                data.entries.forEachIndexed { i, entry ->
                    val (ingreso, gasto) = entry.value
                    val left = i * groupWidth + groupWidth * 0.15f

                    val ingresoHeight = (ingreso / maxVal * size.height).toFloat()
                    drawRoundRect(
                        color = ColorIngreso,
                        topLeft = Offset(left, size.height - ingresoHeight),
                        size = Size(barWidth, ingresoHeight),
                        cornerRadius = CornerRadius(4f)
                    )

                    val gastoHeight = (gasto / maxVal * size.height).toFloat()
                    drawRoundRect(
                        color = ColorGasto,
                        topLeft = Offset(left + barWidth + spacing, size.height - gastoHeight),
                        size = Size(barWidth, gastoHeight),
                        cornerRadius = CornerRadius(4f)
                    )
                }
            }

            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                data.keys.forEach { label ->
                    Text(
                        label,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────
// Barras horizontales: Presupuesto vs Gasto real
// ─────────────────────────────────────────────────────────────────
@Composable
fun PresupuestoVsGastoChart(presupuestos: List<com.example.finalpro.Data.Remote.Dto.Response.PresupuestoResponse>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Presupuesto vs Gasto real (mes actual)",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))

            if (presupuestos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No hay presupuestos para este mes",
                        color = TextMuted,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                return@Column
            }

            presupuestos.forEach { p ->
                val porcentaje = if (p.limiteMonto > 0)
                    (p.gastadoMonto / p.limiteMonto).toFloat().coerceIn(0f, 1f) else 0f

                Column(modifier = Modifier.padding(vertical = 6.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            p.categoriaNombre,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "${(porcentaje * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (porcentaje > 0.8f) ColorWarning else AccentPrimary
                        )
                    }

                    Spacer(Modifier.height(4.dp))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Border)
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth(porcentaje)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    if (porcentaje > 0.8f) ColorWarning else AccentPrimary
                                )
                        )
                    }

                    Spacer(Modifier.height(2.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Gastado: ${formatMoney(p.gastadoMonto, p.moneda)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                        Text(
                            "Límite: ${formatMoney(p.limiteMonto, p.moneda)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}