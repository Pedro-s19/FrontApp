package com.example.finalpro.Ui1.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.finalpro.Ui1.Navigation.Screen
import com.example.finalpro.Ui1.Navigation.bottomNavItems
import com.example.finalpro.Ui1.Theme.*

@Composable
fun BottomNavBar(
    navController: NavController,
    onFabClick: () -> Unit = {}
) {
    val backEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backEntry?.destination?.route

    // Rutas que pertenecen al tab "Mas" para resaltarlo correctamente
    val masRoutes = setOf(
        Screen.Mas.route,
        Screen.Ingresos.route,
        Screen.Recurrentes.route,
        Screen.Logros.route,
        Screen.Presupuesto.route,
        Screen.Comparativas.route
    )

    NavigationBar(
        containerColor = BgSurface,
        tonalElevation = 0.dp,
        modifier = Modifier
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                ambientColor = GreenPrimary.copy(alpha = 0.08f),
                spotColor = GreenPrimary.copy(alpha = 0.12f)
            )
            .background(
                color = BgSurface,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
    ) {
        bottomNavItems.forEach { item ->
            // El tab "Mas" se activa para todas sus sub-rutas
            val selected = if (item.route == Screen.Mas.route) {
                currentRoute in masRoutes
            } else {
                currentRoute == item.route
            }

            NavigationBarItem(
                selected = selected,
                onClick = {
                    when (item.route) {
                        // Inicio: siempre vuelve al Dashboard limpiando el backstack
                        Screen.Dashboard.route -> {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Dashboard.route) {
                                    inclusive = true   // saca el Dashboard y vuelve a crearlo limpio
                                }
                                launchSingleTop = true
                            }
                        }
                        // Mas: si ya estamos en alguna sub-ruta de Mas, volvemos a MasScreen
                        Screen.Mas.route -> {
                            if (currentRoute != Screen.Mas.route) {
                                navController.navigate(Screen.Mas.route) {
                                    popUpTo(Screen.Dashboard.route) { inclusive = false }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                        // Resto de tabs: navegacion estandar
                        else -> {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(Screen.Dashboard.route) {
                                        saveState = true
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                },
                icon = {
                    Icon(
                        item.icon, null,
                        modifier = Modifier.size(22.dp),
                        tint = if (selected) GreenPrimary else TextMuted
                    )
                },
                label = {
                    Text(
                        item.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selected) GreenPrimary else TextMuted
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = GreenLight,
                    selectedIconColor = GreenPrimary,
                    selectedTextColor = GreenPrimary,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                )
            )
        }
    }
}