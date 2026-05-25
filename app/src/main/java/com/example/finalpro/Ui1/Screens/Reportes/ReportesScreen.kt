package com.example.finalpro.Ui1.Screens.Reportes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.finalpro.Data.Remote.Dto.Response.DistribucionCategoriaResponse
import com.example.finalpro.Ui1.Components.BottomNavBar
import com.example.finalpro.Ui1.Components.HeatmapCalendar
import com.example.finalpro.Ui1.Theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportesScreen(
    navController: NavController,
    vm: ReportesViewModel = hiltViewModel()
) {
    val tendencia     by vm.tendencia.collectAsState()
    val distribucion  by vm.distribucion.collectAsState()
    val gastosDiarios by vm.gastosDiarios.collectAsState()
    val loading       by vm.loading.collectAsState()
    val error         by vm.error.collectAsState()

    var tabSeleccionado by remember { mutableIntStateOf(0) }
    val tabs = listOf("Tendencia", "Categorías", "Calendario")

    // ─── Recarga los gráficos cada vez que la pantalla vuelve a estar activa ───
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            vm.cargar()
        }
    }

    Scaffold(
        containerColor = BgPrimary,
        topBar = {
            Column(
                Modifier.fillMaxWidth().background(BgSurface)
                    .padding(horizontal = 20.dp).padding(top = 16.dp, bottom = 8.dp)
            ) {
                Text("Reportes", style = MaterialTheme.typography.headlineMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(14.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BgCardAlt)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    tabs.forEachIndexed { idx, label ->
                        val selected = tabSeleccionado == idx
                        Box(
                            Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selected) BgSurface else Color.Transparent)
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            TextButton(onClick = { tabSeleccionado = idx }, modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    label,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (selected) GreenPrimary else TextSecondary,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        if (loading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = GreenPrimary)
                    Spacer(Modifier.height(16.dp))
                    Text("Conectando con el servidor...", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                    Text("Esto puede tardar unos segundos", color = TextMuted, style = MaterialTheme.typography.labelSmall)
                }
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(Modifier.height(8.dp)) }

                if (error != null) {
                    item {
                        Card(
                            Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = ColorWarning.copy(alpha = 0.08f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ColorWarning.copy(alpha = 0.3f))
                        ) {
                            Column(
                                Modifier.fillMaxWidth().padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("⚠️", fontSize = 32.sp)
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    error ?: "",
                                    color = TextPrimary,
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(16.dp))
                                Button(
                                    onClick = { vm.cargar(vm.anio, vm.mes, vm.moneda) },
                                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Reintentar", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                when (tabSeleccionado) {
                    0 -> item {
                        val t = tendencia
                        if (t != null && t.valores.any { it > 0 }) {
                            BarChartView(etiquetas = t.etiquetas, valores = t.valores, moneda = t.moneda)
                            Spacer(Modifier.height(12.dp))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                val max = t.valores.maxOrNull() ?: 0.0
                                val avg = if (t.valores.isNotEmpty()) t.valores.average() else 0.0
                                QuickStatCard(Modifier.weight(1f), "Pico",          formatMoneyShort(max, t.moneda),         ColorGasto)
                                QuickStatCard(Modifier.weight(1f), "Promedio/día",  formatMoneyShort(avg, t.moneda),         ColorInfo)
                                QuickStatCard(Modifier.weight(1f), "Total",         formatMoneyShort(t.valores.sum(), t.moneda), ColorWarning)
                            }
                        } else if (error == null) {
                            EmptyChartCard("Sin gastos este mes", "Registra gastos para ver la tendencia")
                        }
                    }

                    1 -> item {
                        val d = distribucion
                        if (d != null && d.items.isNotEmpty()) {
                            DonutChartView(items = d.items, moneda = d.moneda)
                        } else if (error == null) {
                            EmptyChartCard("Sin datos por categoría", "Agrega gastos con categorías")
                        }
                    }

                    2 -> item {
                        if (error == null || gastosDiarios.isNotEmpty()) {
                            HeatmapCalendar(
                                gastosDiarios = gastosDiarios,
                                mes   = vm.mes,
                                anio  = vm.anio,
                                moneda = vm.moneda
                            )
                        } else {
                            EmptyChartCard("Sin datos de calendario", "Intenta reconectarte")
                        }
                    }
                }

                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun QuickStatCard(modifier: Modifier, label: String, value: String, color: Color) {
    Card(
        modifier, shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.07f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Spacer(Modifier.height(2.dp))
            Text(value, style = MaterialTheme.typography.titleSmall, color = color, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun BarChartView(etiquetas: List<String>, valores: List<Double>, moneda: String) {
    val maxValor = valores.maxOrNull()?.takeIf { it > 0 } ?: 1.0
    Card(
        Modifier.fillMaxWidth().height(240.dp), shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text("Gastos por día", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Spacer(Modifier.height(8.dp))
            androidx.compose.foundation.Canvas(Modifier.fillMaxWidth().weight(1f)) {
                val w = size.width; val h = size.height
                val barCount = valores.size
                val totalGap = w * 0.3f
                val barWidth = if (barCount > 0) (w - totalGap) / barCount else 0f
                val gapWidth = if (barCount > 1) totalGap / (barCount - 1) else 0f
                val maxH = h * 0.85f
                for (i in 0..3) {
                    val y = h * (1f - i / 3f)
                    drawLine(Color(0xFFE2E6ED), Offset(0f, y), Offset(w, y), strokeWidth = 1f)
                }
                valores.forEachIndexed { i, valor ->
                    val barH = (valor / maxValor * maxH).toFloat().coerceAtLeast(4f)
                    val x = i * (barWidth + gapWidth)
                    val top = h - barH
                    drawRoundRect(GreenPrimary.copy(alpha = 0.08f), Offset(x, 0f), Size(barWidth, h), CornerRadius(6f))
                    drawRoundRect(
                        Brush.verticalGradient(listOf(GreenPrimary, GreenPrimary.copy(alpha = 0.5f)), top, h),
                        Offset(x, top), Size(barWidth, barH), CornerRadius(8f)
                    )
                }
            }
            val step = if (etiquetas.size > 10) etiquetas.size / 5 else 1
            Row(Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                etiquetas.filterIndexed { i, _ -> i % step == 0 }.forEach {
                    Text(it, style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 9.sp, maxLines = 1)
                }
            }
        }
    }
}

@Composable
fun DonutChartView(items: List<DistribucionCategoriaResponse.ItemCategoriaGasto>, moneda: String) {
    val colores = ChartPalette.take(items.size)
    Card(
        Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(160.dp), contentAlignment = Alignment.Center) {
                    androidx.compose.foundation.Canvas(Modifier.size(160.dp)) {
                        val strokeWidth = 30.dp.toPx()
                        val radius = (size.minDimension - strokeWidth) / 2f
                        val center = Offset(size.width / 2, size.height / 2)
                        var startAngle = -90f
                        items.forEachIndexed { i, item ->
                            val sweep = (item.porcentaje / 100f * 360f).toFloat()
                            drawArc(
                                colores[i], startAngle, sweep - 1f, false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                                topLeft = Offset(center.x - radius, center.y - radius),
                                size = Size(radius * 2, radius * 2)
                            )
                            startAngle += sweep
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${items.size}", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                        Text("categorías", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                    }
                }
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items.forEachIndexed { i, item ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(10.dp).clip(CircleShape).background(colores[i]))
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(item.categoriaNombre, style = MaterialTheme.typography.labelSmall, color = TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("${item.porcentaje.toInt()}% · ${formatMoneyShort(item.total, moneda)}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = Border)
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total gastado:", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                Text(formatMoney(items.sumOf { it.total }, moneda), color = ColorGasto, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun EmptyChartCard(titulo: String, subtitulo: String) {
    Card(
        Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        Box(Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(titulo, color = TextSecondary, fontWeight = FontWeight.Bold)
                Text(subtitulo, color = TextMuted, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

fun formatMoneyShort(monto: Double, moneda: String): String {
    val simbolos = mapOf("COP" to "$", "USD" to "US$", "EUR" to "€", "MXN" to "MX$")
    val simbolo = simbolos[moneda] ?: moneda
    return when {
        monto >= 1_000_000 -> "$simbolo ${"%.1f".format(monto / 1_000_000)}M"
        monto >= 1_000     -> "$simbolo ${"%.0f".format(monto / 1_000)}k"
        else               -> "$simbolo ${"%.0f".format(monto)}"
    }
}