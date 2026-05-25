package com.example.finalpro.Ui1.Components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.example.finalpro.Ui1.Theme.*

@Composable
fun LineChartView(etiquetas: List<String>, valores: List<Double>, moneda: String) {
    if (valores.isEmpty()) return

    val maxValor = valores.maxOrNull() ?: 1.0
    val minValor = valores.minOrNull() ?: 0.0
    val rango = if (maxValor == minValor) 1.0 else maxValor - minValor

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "Tendencia diaria",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val textMeasurer = rememberTextMeasurer()

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val ancho = size.width
                val alto = size.height
                val puntoInicialX = 40f
                val puntoFinalX = ancho - 20f
                val pasoX = if (valores.size > 1) (puntoFinalX - puntoInicialX) / (valores.size - 1) else 0f


                val numLineasHorizontales = 4
                for (i in 0..numLineasHorizontales) {
                    val y = alto * (1 - i.toFloat() / numLineasHorizontales)
                    drawLine(
                        color = Border,
                        start = Offset(puntoInicialX, y),
                        end = Offset(puntoFinalX, y),
                        strokeWidth = 1f
                    )
                }


                for (i in 0..numLineasHorizontales) {
                    val valor = minValor + (rango * (1 - i.toFloat() / numLineasHorizontales))
                    val texto = "${"%.0f".format(valor)}"
                    val textLayoutResult = textMeasurer.measure(texto)
                    val x = puntoInicialX - textLayoutResult.size.width - 5f
                    val y = alto * (1 - i.toFloat() / numLineasHorizontales) - textLayoutResult.size.height / 2
                    drawText(
                        textMeasurer = textMeasurer,
                        text = texto,
                        topLeft = Offset(x, y),
                        style = TextStyle(color = TextSecondary)
                    )
                }


                val puntos = valores.mapIndexed { i, valor ->
                    val x = puntoInicialX + i * pasoX
                    val y = alto * (1 - ((valor - minValor) / rango).toFloat())
                    Offset(x, y)
                }

                val path = Path().apply {
                    if (puntos.isNotEmpty()) {
                        moveTo(puntos.first().x, puntos.first().y)
                        puntos.forEach { lineTo(it.x, it.y) }
                    }
                }
                drawPath(
                    path = path,
                    color = AccentPrimary,
                    style = Stroke(width = 3f, cap = StrokeCap.Round)
                )

                // Dibujar puntos
                puntos.forEach { punto ->
                    drawCircle(
                        color = AccentPrimary,
                        radius = 4f,
                        center = punto
                    )
                }
            }


            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                etiquetas.forEach { etiqueta ->
                    Text(
                        text = etiqueta,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        maxLines = 1,
                        modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}