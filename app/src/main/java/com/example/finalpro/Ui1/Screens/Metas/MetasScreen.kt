package com.example.finalpro.Ui1.Screens.Metas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.finalpro.Ui1.Components.AgregarMetaSheet
import com.example.finalpro.Ui1.Components.BottomNavBar
import com.example.finalpro.Ui1.Components.MetaAhorroCard
import com.example.finalpro.Ui1.Theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetasScreen(
    navController: NavController,
    vm: MetasViewModel = hiltViewModel()
) {
    val metas   by vm.metas.collectAsState()
    val loading by vm.loading.collectAsState()
    val balance by vm.balance.collectAsState()
    val alertas by vm.alertas.collectAsState()
    var showSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { vm.cargarMetas() }

    val totalAhorrado  = metas.sumOf { it.montoActual }
    val totalObjetivos = metas.sumOf { it.montoObjetivo }
    val metasCompletadas = metas.count { it.montoActual >= it.montoObjetivo }

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
                    Column {
                        Text("Metas de ahorro", style = MaterialTheme.typography.headlineMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                        Text(
                            "Saldo disponible: ${formatMoney(balance.coerceAtLeast(0.0), "COP")}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (balance > 0) ColorIngreso else ColorGasto,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(onClick = { vm.cargarMetas() }) {
                        Icon(Icons.Rounded.Refresh, null, tint = TextSecondary)
                    }
                }
                if (metas.isNotEmpty()) {
                    Spacer(Modifier.height(14.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        MetaStatChip(Modifier.weight(1f), "${metas.size}", "Metas", ColorInfo)
                        MetaStatChip(Modifier.weight(1f), "$metasCompletadas", "Completadas", ColorIngreso)
                        MetaStatChip(Modifier.weight(1f), formatMoneyShort(totalAhorrado, "COP"), "Ahorrado", GreenPrimary)
                    }
                }
            }
        },
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet = true },
                containerColor = GreenPrimary,
                shape = CircleShape
            ) { Icon(Icons.Rounded.Add, null, tint = Color.White) }
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

                if (metas.isEmpty()) {
                    item {
                        Box(Modifier.fillMaxWidth().padding(top = 64.dp), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🎯", fontSize = 40.sp)
                                Spacer(Modifier.height(8.dp))
                                Text("Sin metas de ahorro", color = TextSecondary, fontWeight = FontWeight.Bold)
                                Text("Toca + para crear una", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }

                items(metas, key = { it.id }) { meta ->
                    MetaAhorroCard(
                        meta     = meta,
                        onAbonar = { monto -> vm.agregarAhorro(meta.id, monto) },
                        onDelete = { vm.eliminarMeta(meta.id) },
                        onEditar = { nuevoObjetivo -> vm.editarObjetivo(meta.id, nuevoObjetivo) },
                        saldoDisponible = balance
                    )
                }

                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }

    if (showSheet) {
        AgregarMetaSheet(
            onDismiss = { showSheet = false },
            onConfirm = { nombre, monto, fecha ->
                vm.crearMeta(nombre, monto, fecha)
                showSheet = false
            },
            balanceDisponible = balance
        )
    }
}

@Composable
private fun MetaStatChip(modifier: Modifier, value: String, label: String, color: Color) {
    Column(
        modifier
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(alpha = 0.08f))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, color = color, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Text(label, color = TextSecondary, style = MaterialTheme.typography.labelSmall)
    }
}

private fun formatMoneyShort(monto: Double, moneda: String): String {
    val simbolos = mapOf("COP" to "$", "USD" to "US$", "EUR" to "€", "MXN" to "MX$")
    val simbolo = simbolos[moneda] ?: moneda
    return when {
        monto >= 1_000_000 -> "$simbolo ${"%.1f".format(monto / 1_000_000)}M"
        monto >= 1_000     -> "$simbolo ${"%.0f".format(monto / 1_000)}k"
        else               -> "$simbolo ${"%.0f".format(monto)}"
    }
}
