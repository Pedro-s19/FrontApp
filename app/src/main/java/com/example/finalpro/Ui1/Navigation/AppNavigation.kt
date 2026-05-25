package com.example.finalpro.Ui1.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finalpro.Data.Local.SessionManager
import com.example.finalpro.Ui1.Screens.Admin.AdminScreen
import com.example.finalpro.Ui1.Screens.Auth.LoginScreen
import com.example.finalpro.Ui1.Screens.Auth.RegisterScreen
import com.example.finalpro.Ui1.Screens.Comparativas.ComparativasScreen
import com.example.finalpro.Ui1.Screens.Dashboard.DashboardScreen
import com.example.finalpro.Ui1.Screens.Gastos.GastosScreen
import com.example.finalpro.Ui1.Screens.Ingresos.IngresosScreen
import com.example.finalpro.Ui1.Screens.Logros.LogrosScreen
import com.example.finalpro.Ui1.Screens.Mas.MasScreen
import com.example.finalpro.Ui1.Screens.Presupuesto.PresupuestoScreen
import com.example.finalpro.Ui1.Screens.Metas.MetasScreen
import com.example.finalpro.Ui1.Screens.Recurrentes.RecurrentesScreen
import com.example.finalpro.Ui1.Screens.Reportes.ReportesScreen

@Composable
fun AppNavigation(sessionManager: SessionManager) {
    val navController = rememberNavController()
    val startDest = if (sessionManager.isLoggedIn()) Screen.Dashboard.route else Screen.Login.route

    NavHost(navController = navController, startDestination = startDest) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onGoToRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Dashboard.route)     { DashboardScreen(navController) }
        composable(Screen.Gastos.route)        { GastosScreen(navController) }
        composable(Screen.Ingresos.route)      { IngresosScreen(navController) }
        composable(Screen.Presupuesto.route)   { PresupuestoScreen(navController) }
        composable(Screen.Metas.route)         { MetasScreen(navController) }
        composable(Screen.Reportes.route)      { ReportesScreen(navController) }
        composable(Screen.Comparativas.route)  { ComparativasScreen(navController) }
        composable(Screen.AdminUsuarios.route) { AdminScreen(navController) }
        composable(Screen.Recurrentes.route)   { RecurrentesScreen(navController) }
        composable(Screen.Logros.route)        { LogrosScreen(navController) }
        composable(Screen.Mas.route)           { MasScreen(navController) }
    }
}