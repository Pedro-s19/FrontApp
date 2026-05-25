package com.example.finalpro.Ui1.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finalpro.Ui1.Theme.*
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadarChart(
    categorias: List<String>,
    valores: List<Double>,
    valoresIdeales: List<Double>,
    moneda: String
) {
    val colores = ChartPalette.take(categorias.size)
    val maxValor = (valores + valoresIdeales).maxOrNull() ?: 1.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Equilibrio de gastos", style = MaterialTheme.typography.titleMedium,
                color = TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))

            Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 2 * 0.7f
                val angleStep = 2 * Math.PI / categorias.size

                // Ejes
                for (i in categorias.indices) {
                    val angle = i * angleStep - Math.PI / 2
                    val x = center.x + radius * cos(angle).toFloat()
                    val y = center.y + radius * sin(angle).toFloat()
                    drawLine(Color.LightGray, center, Offset(x, y), strokeWidth = 1f)
                }

                // Polígono ideal
                val pathIdeal = Path()
                for (i in valoresIdeales.indices) {
                    val angle = i * angleStep - Math.PI / 2
                    val r = (valoresIdeales[i] / maxValor * radius).toFloat()
                    val x = center.x + r * cos(angle).toFloat()
                    val y = center.y + r * sin(angle).toFloat()
                    if (i == 0) pathIdeal.moveTo(x, y) else pathIdeal.lineTo(x, y)
                }
                pathIdeal.close()
                drawPath(pathIdeal, color = AccentSecond.copy(alpha = 0.3f), style = Stroke(2f))

                // Polígono real
                val pathReal = Path()
                for (i in valores.indices) {
                    val angle = i * angleStep - Math.PI / 2
                    val r = (valores[i] / maxValor * radius).toFloat()
                    val x = center.x + r * cos(angle).toFloat()
                    val y = center.y + r * sin(angle).toFloat()
                    if (i == 0) pathReal.moveTo(x, y) else pathReal.lineTo(x, y)
                }
                pathReal.close()
                drawPath(pathReal, color = AccentPrimary.copy(alpha = 0.5f), style = Stroke(2f))

                // Etiquetas (opcional, aquí solo iconos)
                for (i in categorias.indices) {
                    val angle = i * angleStep - Math.PI / 2
                    val x = center.x + (radius + 20f) * cos(angle).toFloat()
                    val y = center.y + (radius + 20f) * sin(angle).toFloat()
                    drawCircle(Color.Transparent, 10f, Offset(x, y))
                }
            }
            // Leyenda
            Row {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(10.dp).background(AccentPrimary, RoundedCornerShape(2.dp)))
                    Spacer(Modifier.width(4.dp))
                    Text("Real", style = MaterialTheme.typography.labelSmall)
                }
                Spacer(Modifier.width(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(10.dp).background(AccentSecond, RoundedCornerShape(2.dp)))
                    Spacer(Modifier.width(4.dp))
                    Text("Ideal", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}