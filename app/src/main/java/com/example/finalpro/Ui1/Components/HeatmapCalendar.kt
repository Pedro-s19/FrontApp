package com.example.finalpro.Ui1.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finalpro.Ui1.Theme.*
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun HeatmapCalendar(
    gastosDiarios: Map<LocalDate, Double>,
    mes: Int,
    anio: Int,
    moneda: String
) {
    val yearMonth = remember(anio, mes) { YearMonth.of(anio, mes) }
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = LocalDate.of(anio, mes, 1).dayOfWeek.value // 1 (Lunes) a 7 (Domingo)

    val maxGasto = gastosDiarios.values.maxOrNull() ?: 1.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Calendario de gastos",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))

            // Cabecera de días de la semana
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (day in listOf("L", "M", "X", "J", "V", "S", "D")) {
                    Text(day, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
            }
            Spacer(Modifier.height(4.dp))

            // Cuadrícula del mes
            val totalCells = firstDayOfWeek - 1 + daysInMonth
            val weeks = (totalCells + 6) / 7
            for (week in 0 until weeks) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (dayOfWeek in 1..7) {
                        val dayIndex = week * 7 + dayOfWeek - firstDayOfWeek
                        if (dayIndex in 0 until daysInMonth) {
                            val date = LocalDate.of(anio, mes, dayIndex + 1)
                            val gasto = gastosDiarios[date] ?: 0.0
                            val intensity = if (maxGasto > 0) (gasto / maxGasto).toFloat() else 0f
                            val color = lerpColor(ColorIngreso, ColorGasto, intensity.coerceIn(0f, 1f))

                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(2.dp)
                                    .background(color, RoundedCornerShape(4.dp))
                            )
                        } else {
                            Spacer(Modifier.size(32.dp)) // celda vacía
                        }
                    }
                }
            }

            // Leyenda de intensidad
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Bajo", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                Spacer(Modifier.width(4.dp))
                Box(
                    Modifier
                        .size(16.dp)
                        .background(ColorIngreso, RoundedCornerShape(2.dp))
                )
                Spacer(Modifier.width(8.dp))
                Box(
                    Modifier
                        .size(16.dp)
                        .background(ColorWarning, RoundedCornerShape(2.dp))
                )
                Spacer(Modifier.width(8.dp))
                Box(
                    Modifier
                        .size(16.dp)
                        .background(ColorGasto, RoundedCornerShape(2.dp))
                )
                Spacer(Modifier.width(4.dp))
                Text("Alto", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
        }
    }
}

fun lerpColor(start: Color, end: Color, fraction: Float): Color {
    return Color(
        red = (start.red + (end.red - start.red) * fraction).coerceIn(0f, 1f),
        green = (start.green + (end.green - start.green) * fraction).coerceIn(0f, 1f),
        blue = (start.blue + (end.blue - start.blue) * fraction).coerceIn(0f, 1f),
        alpha = 1f
    )
}