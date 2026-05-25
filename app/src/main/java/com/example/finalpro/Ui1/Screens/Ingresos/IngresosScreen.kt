package com.example.finalpro.Ui1.Screens.Ingresos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.finalpro.Ui1.Components.AgregarIngresoSheet
import com.example.finalpro.Ui1.Components.BottomNavBar
import com.example.finalpro.Ui1.Components.TransaccionItem
import com.example.finalpro.Ui1.Theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngresosScreen(
    navController: NavController,
    vm: IngresosViewModel = hiltViewModel()
) {
    val ingresos by vm.ingresos.collectAsState()
    val loading  by vm.loading.collectAsState()
    var showSheet by remember { mutableStateOf(false) }

    // ─── Recarga cada vez que la pantalla se vuelve activa ─────────────────
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            vm.cargarIngresos()
        }
    }

    Scaffold(
        containerColor = BgPrimary,
        topBar = {
            TopAppBar(
                title = { Text("Ingresos", fontWeight = FontWeight.Bold, color = TextPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgSurface),
                actions = {
                    if (ingresos.isNotEmpty()) {
                        val total = ingresos.sumOf { it.monto }
                        Text(
                            text = formatMoney(total, ingresos.firstOrNull()?.moneda ?: "COP"),
                            color = ColorIngreso,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                }
            )
        },
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet = true },
                containerColor = AccentPrimary,
                shape = androidx.compose.foundation.shape.CircleShape
            ) {
                Icon(Icons.Rounded.Add, null, tint = androidx.compose.ui.graphics.Color.White)
            }
        }
    ) { padding ->
        when {
            loading -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AccentPrimary)
                }
            }

            // ─── Empty state ────────────────────────────────────────────────
            ingresos.isEmpty() -> {
                Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Card(
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = ColorIngreso.copy(alpha = 0.1f))
                        ) {
                            Box(Modifier.padding(24.dp)) {
                                Icon(
                                    Icons.AutoMirrored.Rounded.TrendingUp,
                                    contentDescription = null,
                                    tint = ColorIngreso,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                        Text(
                            "Sin ingresos registrados",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TextPrimary
                        )
                        Text(
                            "Toca + para agregar tu primer ingreso\ny comenzar a llevar el control.",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            // ─── Lista de ingresos ──────────────────────────────────────────
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item { Spacer(Modifier.height(4.dp)) }
                    items(items = ingresos, key = { it.id }) { ingreso ->
                        TransaccionItem(
                            descripcion  = ingreso.descripcion ?: "Sin descripción",
                            monto        = "+${formatMoney(ingreso.monto, ingreso.moneda)}",
                            fecha        = ingreso.fecha,
                            categoria    = "Ingreso",
                            icono        = "💵",
                            colorMonto   = ColorIngreso,
                            onDelete     = { vm.eliminarIngreso(ingreso.id) },
                            onEdit       = { monto, desc, fecha ->
                                vm.actualizarIngreso(ingreso.id, monto, desc, fecha)
                            }
                        )
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }

    if (showSheet) {
        AgregarIngresoSheet(onDismiss = { showSheet = false }) { monto, desc, fecha ->
            vm.crearIngreso(monto, desc, fecha)
            showSheet = false
        }
    }
}