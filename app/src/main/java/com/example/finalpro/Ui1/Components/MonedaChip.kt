package com.example.finalpro.Ui1.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.finalpro.Data.Local.SessionManager
import com.example.finalpro.Ui1.Theme.*
import kotlinx.coroutines.launch

@Composable
fun MonedaChip(sessionManager: SessionManager) {
    var currentMoneda by remember { mutableStateOf("COP") }
    var expanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        sessionManager.getMoneda().collect { currentMoneda = it }
    }

    Box(contentAlignment = Alignment.Center) {
        AssistChip(
            onClick = { expanded = true },
            label = { Text(currentMoneda) },
            leadingIcon = { Text("💰") },
            trailingIcon = { Icon(Icons.Rounded.ArrowDropDown, null, Modifier.size(16.dp)) },
            shape = RoundedCornerShape(20.dp),
            colors = AssistChipDefaults.assistChipColors(containerColor = BgCard, labelColor = TextPrimary)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(BgSurface)
        ) {
            listOf("COP", "USD", "EUR", "MXN").forEach { moneda ->
                DropdownMenuItem(
                    text = { Text(moneda, color = TextPrimary) },
                    onClick = {
                        scope.launch { sessionManager.saveMoneda(moneda) }
                        expanded = false
                    }
                )
            }
        }
    }
}