package com.example.finalpro.Ui1.Screens.Dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.finalpro.Data.Remote.Dto.Response.ResumenMensualResponse
import com.example.finalpro.Ui1.Components.AgregarIngresoSheet
import com.example.finalpro.Ui1.Components.BottomNavBar
import com.example.finalpro.Ui1.Components.PresupuestoResumenCard
import com.example.finalpro.Ui1.Navigation.Screen
import com.example.finalpro.Ui1.Screens.Reportes.BarChartView
import com.example.finalpro.Ui1.Theme.*
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    vm: DashboardViewModel = hiltViewModel()
) {
    val resumen      by vm.resumen.collectAsState()
    val loading      by vm.loading.collectAsState()
    val email        by vm.sessionManager.getEmail().collectAsState(initial = "")
    val moneda       by vm.sessionManager.getMoneda().collectAsState(initial = "COP")
    val rol          by vm.sessionManager.getRol().collectAsState(initial = null)
    val alertas      by vm.alertas.collectAsState()
    val gastosSemana by vm.gastosSemana.collectAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showFabSheet     by remember { mutableStateOf(false) }
    var showIngresoSheet by remember { mutableStateOf(false) }

    // Recarga al volver a esta pantalla (onResume)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(lifecycle, moneda) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            vm.cargarResumen()
        }
    }

    val saludo = remember {
        val hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when {
            hora < 12 -> "Buenos dias"
            hora < 19 -> "Buenas tardes"
            else      -> "Buenas noches"
        }
    }
    val iniciales     = remember(email) { email?.split("@")?.firstOrNull()?.take(2)?.uppercase() ?: "FP" }
    val nombreUsuario = remember(email) { email?.split("@")?.firstOrNull()?.replaceFirstChar { it.uppercase() } ?: "Usuario" }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            key(rol) {
                ModalDrawerSheet(
                    drawerContainerColor = BgSurface,
                    modifier = Modifier.width(280.dp)
                ) {
                    Spacer(Modifier.height(32.dp))
                    Column(
                        Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            Modifier.size(72.dp).clip(CircleShape).background(GreenLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(iniciales, color = GreenPrimary, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(nombreUsuario, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(email ?: "", color = TextSecondary, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Spacer(Modifier.height(24.dp))
                    HorizontalDivider(color = Border, modifier = Modifier.padding(horizontal = 16.dp))
                    Spacer(Modifier.height(16.dp))

                    Text("Moneda preferida", color = TextSecondary, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 24.dp))
                    Spacer(Modifier.height(8.dp))
                    LazyRow(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(listOf("COP", "USD", "EUR", "MXN").size) { idx ->
                            val m = listOf("COP", "USD", "EUR", "MXN")[idx]
                            FilterChip(
                                selected = moneda == m,
                                onClick = { scope.launch { vm.sessionManager.saveMoneda(m) } },
                                label = { Text(m, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = GreenPrimary,
                                    selectedLabelColor = Color.White,
                                    containerColor = BgCardAlt,
                                    labelColor = TextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true, selected = moneda == m,
                                    borderColor = Border, selectedBorderColor = GreenPrimary
                                )
                            )
                        }
                    }

                    if (rol == "ROLE_ADMIN") {
                        Spacer(Modifier.height(8.dp))
                        HorizontalDivider(color = Border, modifier = Modifier.padding(horizontal = 16.dp))
                        NavigationDrawerItem(
                            label = { Text("Administrar usuarios", color = GreenPrimary) },
                            icon = { Icon(Icons.Rounded.Group, null, tint = GreenPrimary) },
                            selected = false,
                            onClick = {
                                navController.navigate(Screen.AdminUsuarios.route) {
                                    popUpTo(Screen.Dashboard.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }

                    Spacer(Modifier.weight(1f))
                    HorizontalDivider(color = Border, modifier = Modifier.padding(horizontal = 16.dp))
                    NavigationDrawerItem(
                        label = { Text("Cerrar sesion", color = ColorGasto) },
                        icon = { Icon(Icons.AutoMirrored.Rounded.ExitToApp, null, tint = ColorGasto) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                vm.sessionManager.clearSession()
                                navController.navigate(Screen.Login.route) { popUpTo(0) { inclusive = true } }
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    ) {
        Scaffold(
            containerColor = BgPrimary,
            // FAB centrado — aqui es donde debe vivir, no dentro de NavigationBar
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showFabSheet = true },
                    containerColor = GreenPrimary,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = "Agregar", modifier = Modifier.size(26.dp))
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = { BottomNavBar(navController = navController) }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(saludo, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                            Text(nombreUsuario, style = MaterialTheme.typography.headlineMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            Modifier.size(44.dp).clip(CircleShape).background(GreenLight)
                                .border(2.dp, GreenPrimary.copy(alpha = 0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Text(iniciales, color = GreenPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }

                if (alertas.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = ColorWarning.copy(alpha = 0.08f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ColorWarning.copy(alpha = 0.4f))
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Alertas", style = MaterialTheme.typography.titleSmall, color = ColorWarning, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(6.dp))
                                alertas.forEach { alerta ->
                                    Row(Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Text("*", color = ColorWarning)
                                        Spacer(Modifier.width(6.dp))
                                        Text(alerta, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    if (loading) {
                        Box(Modifier.fillMaxWidth().height(200.dp).padding(horizontal = 20.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = GreenPrimary)
                        }
                    } else {
                        resumen?.let { r -> BalanceMasterCard(r = r) } ?: EmptyBalanceCard()
                    }
                }

                item {
                    if (gastosSemana.isNotEmpty()) {
                        WeeklyBarsChart(data = gastosSemana, moneda = moneda)
                    }
                }

                item {
                    Text(
                        "Acceso rapido",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                    )
                    Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        QuickChip(Modifier.weight(1f), Icons.AutoMirrored.Rounded.TrendingUp, "Ingresos", ColorIngreso) {
                            navController.navigate(Screen.Ingresos.route) {
                                popUpTo(Screen.Dashboard.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                        QuickChip(Modifier.weight(1f), Icons.AutoMirrored.Rounded.TrendingDown, "Gastos", ColorGasto) {
                            navController.navigate(Screen.Gastos.route) {
                                popUpTo(Screen.Dashboard.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                        QuickChip(Modifier.weight(1f), Icons.Rounded.EmojiEvents, "Metas", ColorInfo) {
                            navController.navigate(Screen.Metas.route) {
                                popUpTo(Screen.Dashboard.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                        QuickChip(Modifier.weight(1f), Icons.Rounded.BarChart, "Reportes", ColorWarning) {
                            navController.navigate(Screen.Reportes.route) {
                                popUpTo(Screen.Dashboard.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    }
                }

                item {
                    resumen?.let { r ->
                        if (r.porcentajePresupuestoUsado != null) {
                            PresupuestoResumenCard(
                                usado = r.porcentajePresupuestoUsado,
                                restante = r.presupuestoRestante ?: 0.0,
                                moneda = r.moneda
                            )
                        }
                    }
                }

                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }

    // FAB sheet
    if (showFabSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFabSheet = false },
            containerColor = BgSurface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                Modifier.padding(horizontal = 24.dp).padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Que quieres registrar?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                FabSheetOption(Icons.AutoMirrored.Rounded.TrendingDown, ColorGasto, ColorGasto.copy(alpha = 0.1f), "Nuevo gasto", "Registra un gasto puntual") {
                    showFabSheet = false
                    navController.navigate(Screen.Gastos.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
                FabSheetOption(Icons.AutoMirrored.Rounded.TrendingUp, ColorIngreso, ColorIngreso.copy(alpha = 0.1f), "Nuevo ingreso", "Registra una entrada de dinero") {
                    showFabSheet = false
                    showIngresoSheet = true
                }
                FabSheetOption(Icons.Rounded.EmojiEvents, ColorInfo, ColorInfo.copy(alpha = 0.1f), "Abonar a meta", "Se descuenta de tu saldo disponible") {
                    showFabSheet = false
                    navController.navigate(Screen.Metas.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    if (showIngresoSheet) {
        AgregarIngresoSheet(onDismiss = { showIngresoSheet = false }) { monto, desc, fecha ->
            vm.registrarIngreso(monto, desc, fecha)
            showIngresoSheet = false
        }
    }
}

// ---------------------------------------------------------------------------
// Composables locales
// ---------------------------------------------------------------------------

@Composable
private fun FabSheetOption(icon: ImageVector, tint: Color, bg: Color, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        onClick = onClick, modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(bg), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = tint, modifier = Modifier.size(22.dp))
            }
            Column {
                Text(title, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }
    }
}

@Composable
private fun QuickChip(modifier: Modifier, icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Card(
        modifier = modifier, onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(
            Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(22.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.SemiBold)
        }
    }
}

// ---------------------------------------------------------------------------
// Balance card
// ---------------------------------------------------------------------------

@Composable
fun BalanceMasterCard(r: ResumenMensualResponse) {
    val porcentajeGastado = if (r.totalIngresos > 0) (r.totalGastos / r.totalIngresos).coerceIn(0.0, 1.0) else 0.0
    val barColor = when {
        porcentajeGastado > 0.85 -> ColorGasto
        porcentajeGastado > 0.60 -> ColorWarning
        else -> ColorIngreso
    }
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 12.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Color(0xFF0F1117), Color(0xFF1A2235))), RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Saldo disponible", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.6f))
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = formatMoney(r.balance.coerceAtLeast(0.0), r.moneda),
                            style = MaterialTheme.typography.headlineLarge,
                            color = if (r.balance >= 0) Color(0xFF4ADE80) else ColorGasto,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        Modifier.size(40.dp).clip(CircleShape).background(barColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (r.balance >= 0) Icons.AutoMirrored.Rounded.TrendingUp else Icons.AutoMirrored.Rounded.TrendingDown,
                            null, tint = barColor, modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
                Column {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Comprometido", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                        Text("${(porcentajeGastado * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = barColor, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(6.dp))
                    Box(Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)).background(Color.White.copy(alpha = 0.1f))) {
                        Box(
                            Modifier.fillMaxWidth(porcentajeGastado.toFloat()).height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Brush.horizontalGradient(listOf(barColor, barColor.copy(alpha = 0.7f))))
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    BalanceChip(Modifier.weight(1f), "Ingresos", formatMoneyShort(r.totalIngresos, r.moneda), Color(0xFF4ADE80))
                    BalanceChip(Modifier.weight(1f), "Gastos",   formatMoneyShort(r.totalGastos, r.moneda),   ColorGasto.copy(alpha = 0.9f))
                    r.presupuestoRestante?.let {
                        BalanceChip(Modifier.weight(1f), "Restante", formatMoneyShort(it.coerceAtLeast(0.0), r.moneda), ColorInfo.copy(alpha = 0.9f))
                    }
                }
            }
        }
    }
}

@Composable
private fun BalanceChip(modifier: Modifier, label: String, value: String, color: Color) {
    Column(
        modifier.clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.07f)).padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
        Spacer(Modifier.height(2.dp))
        Text(value, style = MaterialTheme.typography.labelMedium, color = color, fontWeight = FontWeight.Bold)
    }
}

// ---------------------------------------------------------------------------
// Weekly chart
// ---------------------------------------------------------------------------

@Composable
fun WeeklyBarsChart(data: Map<String, Double>, moneda: String) {
    val labels = data.keys.toList()
    val values = data.values.toList()
    if (values.isEmpty()) return
    Card(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Gastos de la semana", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            BarChartView(etiquetas = labels, valores = values, moneda = moneda)
        }
    }
}

// ---------------------------------------------------------------------------
// Empty state
// ---------------------------------------------------------------------------

@Composable
private fun EmptyBalanceCard() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 12.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(8.dp))
                Text("Sin datos este mes", color = TextSecondary, fontWeight = FontWeight.Bold)
                Text("Agrega ingresos para comenzar", color = TextMuted, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Helpers
// ---------------------------------------------------------------------------

private fun formatMoneyShort(monto: Double, moneda: String): String {
    val simbolos = mapOf("COP" to "$", "USD" to "US$", "EUR" to "EUR", "MXN" to "MX$")
    val simbolo = simbolos[moneda] ?: moneda
    return when {
        monto >= 1_000_000 -> "$simbolo ${"%.1f".format(monto / 1_000_000)}M"
        monto >= 1_000     -> "$simbolo ${"%.0f".format(monto / 1_000)}k"
        else               -> "$simbolo ${"%.0f".format(monto)}"
    }
}