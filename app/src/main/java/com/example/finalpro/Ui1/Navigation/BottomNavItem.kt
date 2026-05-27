package com.example.finalpro.Ui1.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Login         : Screen("login")
    object Register      : Screen("register")
    object Dashboard     : Screen("dashboard")
    object Gastos        : Screen("gastos")
    object Ingresos      : Screen("ingresos")
    object Presupuesto   : Screen("presupuesto")
    object Metas         : Screen("metas")
    object Reportes      : Screen("reportes")
    object Comparativas  : Screen("comparativas")
    object AdminUsuarios : Screen("admin_usuarios")
    object Recurrentes   : Screen("recurrentes")
    object Logros        : Screen("logros")
    object Mas           : Screen("mas")
}

data class BottomNavItem(val label: String, val route: String, val icon: ImageVector)

val bottomNavItems = listOf(
    BottomNavItem("Inicio",   Screen.Dashboard.route, Icons.Rounded.Home),
    BottomNavItem("Gastos",   Screen.Gastos.route,    Icons.Rounded.ShoppingCart),
    BottomNavItem("Reportes", Screen.Reportes.route,  Icons.Rounded.BarChart),
    BottomNavItem("Mas",      Screen.Mas.route,       Icons.Rounded.GridView),
)