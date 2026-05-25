package com.example.finalpro.Ui1.Screens.Gastos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.finalpro.Ui1.Components.*
import com.example.finalpro.Ui1.Theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastosScreen(
    navController: NavController,
    vm: GastosViewModel = hiltViewModel()
) {
    val gastos          by vm.gastos.collectAsState()
    val categorias      by vm.categorias.collectAsState()
    val loading         by vm.loading.collectAsState()
    val saldo           by vm.saldoDisponible.collectAsState()
    val alertasActivas  by vm.alertasActivas.collectAsState()
    val logroNuevo      by vm.logroNuevo.collectAsState()

    var showSheet       by remember { mutableStateOf(false) }
    var pendingGasto    by remember { mutableStateOf<Triple<Double, String, String>?>(null) }
    var pendingCatId    by remember { mutableStateOf("") }
    var filtroCategoria by remember { mutableStateOf<String?>(null) }

    val gastosFiltrados = remember(gastos, filtroCategoria) {
        if (filtroCategoria == null) gastos
        else gastos.filter { it.categoria?.nombre == filtroCategoria }
    }
    val nombresCategoria = remember(gastos) {
        gastos.mapNotNull { it.categoria?.nombre }.distinct()
    }
    val moneda = gastos.firstOrNull()?.moneda ?: "COP"
    
    if (alertasActivas.isNotEmpty()) {
        AlertaPresupuestoDialog(
            alertas = alertasActivas,
            onDismiss = {

                vm.dismissAlertas()
                pendingGasto?.let { (monto, desc, fecha) ->
                    vm.crearGasto(monto, desc, fecha, pendingCatId)
                }
                pendingGasto = null
                showSheet = false
            },
            onCancelar = {
                vm.dismissAlertas()
                pendingGasto = null
            }
        )
    }

    logroNuevo?.let { logro ->
        LogroDesbloqueadoDialog(logro = logro, onDismiss = { vm.dismissLogro() })
    }

    Scaffold(
        containerColor = BgPrimary,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgSurface)
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                Text(
                    "Gastos",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(6.dp))
                if (saldo < Double.MAX_VALUE) {
                    SaldoDisponibleBanner(saldo = saldo, moneda = moneda)
                }
            }
        },
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet = true },
                containerColor = GreenPrimary,
                shape = CircleShape
            ) {
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (nombresCategoria.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(4.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            item {
                                FilterChip(
                                    selected = filtroCategoria == null,
                                    onClick = { filtroCategoria = null },
                                    label = { Text("Todos", fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = GreenPrimary,
                                        selectedLabelColor = Color.White,
                                        containerColor = BgSurface,
                                        labelColor = TextSecondary
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = filtroCategoria == null,
                                        borderColor = Border,
                                        selectedBorderColor = GreenPrimary
                                    )
                                )
                            }
                            items(nombresCategoria) { nombre ->
                                FilterChip(
                                    selected = filtroCategoria == nombre,
                                    onClick = {
                                        filtroCategoria = if (filtroCategoria == nombre) null else nombre
                                    },
                                    label = { Text(nombre, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = GreenPrimary,
                                        selectedLabelColor = Color.White,
                                        containerColor = BgSurface,
                                        labelColor = TextSecondary
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = filtroCategoria == nombre,
                                        borderColor = Border,
                                        selectedBorderColor = GreenPrimary
                                    )
                                )
                            }
                        }
                    }
                }

                item { Spacer(Modifier.height(4.dp)) }

                if (gastosFiltrados.isEmpty()) {
                    item {
                        Box(
                            Modifier.fillMaxWidth().padding(top = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🧾", fontSize = 32.sp)
                                Spacer(Modifier.height(8.dp))
                                Text("Sin gastos registrados", color = TextSecondary, fontWeight = FontWeight.Bold)
                                Text("Toca + para agregar uno", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }

                items(items = gastosFiltrados, key = { it.id }) { gasto ->
                    TransaccionItem(
                        descripcion = gasto.descripcion ?: "Sin descripción",
                        monto = "-${formatMoney(gasto.monto, gasto.moneda)}",
                        fecha = gasto.fecha,
                        categoria = gasto.categoria?.nombre ?: "General",
                        icono = gasto.categoria?.icono,
                        colorMonto = ColorGasto,
                        onDelete = { vm.eliminarGasto(gasto.id) },
                        onEdit = { monto, desc, fecha -> vm.actualizarGasto(gasto.id, monto, desc, fecha) }
                    )
                }

                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }

    if (showSheet) {
        AgregarGastoSheet(
            onDismiss = { showSheet = false },
            onConfirm = { monto, desc, fecha, catId ->
                pendingGasto = Triple(monto, desc, fecha)
                pendingCatId = catId
                val hayAlertas = vm.verificarAlertas()
                if (!hayAlertas) {
                    vm.crearGasto(monto, desc, fecha, catId)
                    pendingGasto = null
                    showSheet = false
                }
            },
            categorias = categorias,
            saldoDisponible = saldo,
            moneda = moneda
        )
    }
}