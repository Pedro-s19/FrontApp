package com.example.finalpro.Ui1.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
    val currentRoute by navController.currentBackStackEntryAsState().let {
        val entry = it.value
        remember(entry) { derivedStateOf { entry?.destination?.route } }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
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
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            // Ítem 1: Inicio
            val item0 = bottomNavItems[0]
            NavigationBarItem(
                selected = currentRoute == item0.route,
                onClick = { navigateTo(navController, item0.route, currentRoute) },
                icon = {
                    Icon(
                        item0.icon, null,
                        modifier = Modifier.size(22.dp),
                        tint = if (currentRoute == item0.route) GreenPrimary else TextMuted
                    )
                },
                label = {
                    Text(
                        item0.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (currentRoute == item0.route) GreenPrimary else TextMuted
                    )
                },
                colors = navBarItemColors()
            )

            val item1 = bottomNavItems[1]
            NavigationBarItem(
                selected = currentRoute == item1.route,
                onClick = { navigateTo(navController, item1.route, currentRoute) },
                icon = {
                    Icon(
                        item1.icon, null,
                        modifier = Modifier.size(22.dp),
                        tint = if (currentRoute == item1.route) GreenPrimary else TextMuted
                    )
                },
                label = {
                    Text(
                        item1.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (currentRoute == item1.route) GreenPrimary else TextMuted
                    )
                },
                colors = navBarItemColors()
            )

            // Slot central vacío para el FAB
            NavigationBarItem(
                selected = false,
                onClick = {},
                icon = { Spacer(Modifier.size(22.dp)) },
                label = { Spacer(Modifier.height(14.dp)) },
                colors = navBarItemColors(),
                enabled = false
            )

            // Ítem 3: Reportes
            val item2 = bottomNavItems[2]
            NavigationBarItem(
                selected = currentRoute == item2.route,
                onClick = { navigateTo(navController, item2.route, currentRoute) },
                icon = {
                    Icon(
                        item2.icon, null,
                        modifier = Modifier.size(22.dp),
                        tint = if (currentRoute == item2.route) GreenPrimary else TextMuted
                    )
                },
                label = {
                    Text(
                        item2.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (currentRoute == item2.route) GreenPrimary else TextMuted
                    )
                },
                colors = navBarItemColors()
            )

            // Ítem 4: Más
            val item3 = bottomNavItems[3]
            val masRoutes = setOf(
                Screen.Mas.route, Screen.Ingresos.route,
                Screen.Recurrentes.route, Screen.Logros.route,
                Screen.Presupuesto.route, Screen.Comparativas.route
            )
            val masSelected = currentRoute in masRoutes
            NavigationBarItem(
                selected = masSelected,
                onClick = { navigateTo(navController, item3.route, currentRoute) },
                icon = {
                    Icon(
                        item3.icon, null,
                        modifier = Modifier.size(22.dp),
                        tint = if (masSelected) GreenPrimary else TextMuted
                    )
                },
                label = {
                    Text(
                        item3.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (masSelected) GreenPrimary else TextMuted
                    )
                },
                colors = navBarItemColors()
            )
        }

        // FAB central flotante sobre la barra
        FloatingActionButton(
            onClick = onFabClick,
            containerColor = GreenPrimary,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-20).dp)
                .size(52.dp)
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "Agregar", modifier = Modifier.size(26.dp))
        }
    }
}

@Composable
private fun navBarItemColors() = NavigationBarItemDefaults.colors(
    indicatorColor = GreenLight,
    selectedIconColor = GreenPrimary,
    selectedTextColor = GreenPrimary,
    unselectedIconColor = TextMuted,
    unselectedTextColor = TextMuted
)

private fun navigateTo(navController: NavController, route: String, currentRoute: String?) {
    if (currentRoute == route) return
    navController.navigate(route) {
        popUpTo(Screen.Dashboard.route) {
            saveState = true
            inclusive = false
        }
        launchSingleTop = true
        restoreState = true
    }
}