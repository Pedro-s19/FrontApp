package com.example.finalpro.Ui1.Screens.Mas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalpro.Ui1.Components.BottomNavBar
import com.example.finalpro.Ui1.Navigation.Screen
import com.example.finalpro.Ui1.Theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasScreen(navController: NavController) {
    Scaffold(
        containerColor = BgPrimary,
        topBar = {
            TopAppBar(
                title = { Text("Mas opciones", fontWeight = FontWeight.Bold, color = TextPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgSurface)
            )
        },
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(Modifier.height(8.dp)) }

            item {
                Text(
                    "Finanzas",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            item {
                MasCard(
                    icon = Icons.AutoMirrored.Rounded.TrendingUp,
                    iconBg = GreenLight,
                    iconTint = GreenPrimary,
                    title = "Ingresos",
                    subtitle = "Registra y consulta tus entradas de dinero",
                    onClick = {
                        // popUpTo(Mas) inclusive=false para que el boton atras vuelva a MasScreen
                        navController.navigate(Screen.Ingresos.route) {
                            popUpTo(Screen.Mas.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }
            item {
                MasCard(
                    icon = Icons.Rounded.Repeat,
                    iconBg = ColorInfo.copy(alpha = 0.12f),
                    iconTint = ColorInfo,
                    title = "Suscripciones y fijos",
                    subtitle = "Gastos recurrentes mensuales",
                    onClick = {
                        navController.navigate(Screen.Recurrentes.route) {
                            popUpTo(Screen.Mas.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }
            item {
                MasCard(
                    icon = Icons.Rounded.PieChart,
                    iconBg = ColorWarning.copy(alpha = 0.12f),
                    iconTint = ColorWarning,
                    title = "Presupuesto",
                    subtitle = "Controla cuanto puedes gastar por categoria",
                    onClick = {
                        navController.navigate(Screen.Presupuesto.route) {
                            popUpTo(Screen.Mas.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }
            item {
                MasCard(
                    icon = Icons.Rounded.BarChart,
                    iconBg = GreenLight,
                    iconTint = GreenPrimary,
                    title = "Comparativas",
                    subtitle = "Compara tus gastos entre meses",
                    onClick = {
                        navController.navigate(Screen.Comparativas.route) {
                            popUpTo(Screen.Mas.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }
            item {
                MasCard(
                    icon = Icons.Rounded.EmojiEvents,
                    iconBg = ColorWarning.copy(alpha = 0.12f),
                    iconTint = ColorWarning,
                    title = "Metas de ahorro",
                    subtitle = "Bolsillos de ahorro con descuento automatico",
                    onClick = {
                        navController.navigate(Screen.Metas.route) {
                            popUpTo(Screen.Mas.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }

            item {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Gamificacion",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            item {
                MasCard(
                    icon = Icons.Rounded.Star,
                    iconBg = ColorWarning.copy(alpha = 0.12f),
                    iconTint = ColorWarning,
                    title = "Mis logros",
                    subtitle = "Desbloquea insignias y sube de nivel",
                    onClick = {
                        navController.navigate(Screen.Logros.route) {
                            popUpTo(Screen.Mas.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun MasCard(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 15.sp)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
            Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp))
        }
    }
}