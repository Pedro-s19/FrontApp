package com.example.finalpro.Ui1.Screens.Logros

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.finalpro.Ui1.Components.BottomNavBar
import com.example.finalpro.Ui1.Theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogrosScreen(navController: NavController, vm: LogrosViewModel = hiltViewModel()) {
    val logros  by vm.logros.collectAsState()
    val puntos  by vm.puntos.collectAsState()
    val loading by vm.loading.collectAsState()
    val error   by vm.error.collectAsState()

    val desbloqueados = logros.filter { it.desbloqueado }
    val bloqueados    = logros.filter { !it.desbloqueado }

    Scaffold(
        containerColor = BgPrimary,
        topBar = {
            TopAppBar(
                title = { Text("Logros y Puntos", fontWeight = FontWeight.Bold, color = TextPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgSurface)
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
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // ── Tarjeta de puntos totales ─────────────────────────
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(listOf(Color(0xFF1A1040), Color(0xFF2D1B69))),
                                    RoundedCornerShape(24.dp)
                                )
                                .padding(24.dp)
                        ) {
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Puntos totales", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                                    Text(
                                        "$puntos pts",
                                        color = AccentPrimary,
                                        fontWeight = FontWeight.ExtraBold,
                                        style = MaterialTheme.typography.headlineLarge
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        "${desbloqueados.size} de ${logros.size} logros desbloqueados",
                                        color = Color.White.copy(alpha = 0.5f),
                                        fontSize = 12.sp
                                    )
                                }
                                Text("🏆", fontSize = 48.sp)
                            }
                        }
                    }
                }

                if (error != null) {
                    item {
                        Card(
                            Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = ColorWarning.copy(alpha = 0.08f)),
                            border = BorderStroke(1.dp, ColorWarning.copy(alpha = 0.3f))
                        ) {
                            Row(
                                Modifier.fillMaxWidth().padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(error ?: "", color = TextSecondary, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                                TextButton(onClick = { vm.cargar() }) {
                                    Text("Reintentar", color = GreenPrimary)
                                }
                            }
                        }
                    }
                }

                if (desbloqueados.isNotEmpty()) {
                    item {
                        Text(
                            "✅ Desbloqueados (${desbloqueados.size})",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(desbloqueados, key = { it.id }) { logro ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = AccentPrimary.copy(alpha = 0.10f)),
                            border = BorderStroke(1.dp, AccentPrimary.copy(alpha = 0.4f))
                        ) {
                            Row(
                                Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    Modifier
                                        .size(52.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(AccentPrimary.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(logro.icono, fontSize = 26.sp)
                                }
                                Spacer(Modifier.width(14.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(logro.nombre, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text(logro.descripcion, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                }
                                Box(
                                    Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(AccentPrimary.copy(alpha = 0.15f))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text("+${logro.puntos} pts", fontSize = 12.sp, color = AccentPrimary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                if (bloqueados.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "🔒 Por desbloquear (${bloqueados.size})",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(bloqueados, key = { it.id }) { logro ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = BgCard),
                            border = BorderStroke(1.dp, Border)
                        ) {
                            Row(
                                Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    Modifier
                                        .size(52.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Border),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("🔒", fontSize = 24.sp)
                                }
                                Spacer(Modifier.width(14.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(logro.nombre, fontWeight = FontWeight.Bold, color = TextSecondary)
                                    Text(logro.descripcion, style = MaterialTheme.typography.bodySmall, color = TextMuted)
                                }
                                Text("${logro.puntos} pts", fontSize = 12.sp, color = TextMuted)
                            }
                        }
                    }
                }

                if (logros.isEmpty() && error == null) {
                    item {
                        Box(
                            Modifier.fillMaxWidth().padding(top = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🏅", fontSize = 48.sp)
                                Spacer(Modifier.height(12.dp))
                                Text("Cargando logros...", color = TextSecondary, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(8.dp))
                                TextButton(onClick = { vm.cargar() }) {
                                    Text("Reintentar", color = GreenPrimary)
                                }
                            }
                        }
                    }
                }

                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}
