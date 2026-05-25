package com.example.finalpro.Ui1.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finalpro.Ui1.Navigation.Screen
import com.example.finalpro.Ui1.Theme.*

@Composable
fun QuickAccessGrid(navController: NavController) {
    val items = listOf(
        Triple("Gastos",      Icons.Rounded.ShoppingCart,  Screen.Gastos.route),
        Triple("Ingresos",    Icons.Rounded.AttachMoney,   Screen.Ingresos.route),
        Triple("Presupuesto", Icons.Rounded.PieChart,      Screen.Presupuesto.route),
        Triple("Metas",       Icons.Rounded.EmojiEvents,   Screen.Metas.route),
        Triple("Comparar",    Icons.Rounded.BarChart,      Screen.Comparativas.route)
    )
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items.forEach { (title, icon, route) ->
            Card(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = {
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BgCard),
                border = BorderStroke(1.dp, Border)
            ) {
                Column(
                    Modifier.fillMaxSize().padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(icon, null, tint = AccentPrimary, modifier = Modifier.size(32.dp))
                    Spacer(Modifier.height(8.dp))
                    Text(title, style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                }
            }
        }
    }
}