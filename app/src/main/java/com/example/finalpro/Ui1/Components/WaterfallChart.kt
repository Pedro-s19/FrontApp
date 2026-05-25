package com.example.finalpro.Ui1.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalpro.Ui1.Theme.*
import kotlin.math.abs

@Composable
fun WaterfallChart(
    ingresos: Double,
    gastosFijos: Double,
    suscripciones: Double,
    otrosGastos: Double,
    ahorro: Double,
    moneda: String
) {
    val data = listOf(
        "Ingresos" to ingresos,
        "Fijos" to -gastosFijos,
        "Suscrip." to -suscripciones,
        "Otros" to -otrosGastos,
        "Ahorro" to ahorro
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Cascada financiera",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))

            val textMeasurer = rememberTextMeasurer()

            Canvas(modifier = Modifier.fillMaxWidth().height(240.dp)) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val barCount = data.size
                val barWidth = canvasWidth / (barCount * 2.5f)
                val gap = barWidth * 1.5f
                var currentX = gap / 2

                val maxAbsValue = data.maxOf { abs(it.second) }.coerceAtLeast(1.0)
                val zeroY = canvasHeight * 0.85f

                data.forEach { (label, value) ->
                    val barHeight = (abs(value) / maxAbsValue * (canvasHeight * 0.7f)).toFloat()
                    val color = if (value >= 0) ColorIngreso else ColorGasto

                    if (value >= 0) {
                        // Barra hacia arriba (ingreso/ahorro)
                        drawRoundRect(
                            color = color,
                            topLeft = Offset(currentX, zeroY - barHeight),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(4f)
                        )
                        // Valor encima
                        val texto = "$${value.toInt()}"
                        val result = textMeasurer.measure(texto, TextStyle(color = TextPrimary, fontSize = 10.sp))
                        drawText(
                            textMeasurer = textMeasurer,
                            text = texto,
                            topLeft = Offset(currentX + barWidth / 2 - result.size.width / 2, zeroY - barHeight - result.size.height - 2f)
                        )
                    } else {
                        // Barra hacia abajo (gasto)
                        drawRoundRect(
                            color = color,
                            topLeft = Offset(currentX, zeroY),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(4f)
                        )
                        // Valor debajo
                        val texto = "-$${abs(value).toInt()}"
                        val result = textMeasurer.measure(texto, TextStyle(color = TextPrimary, fontSize = 10.sp))
                        drawText(
                            textMeasurer = textMeasurer,
                            text = texto,
                            topLeft = Offset(currentX + barWidth / 2 - result.size.width / 2, zeroY + barHeight + 4f)
                        )
                    }

                    val labelResult = textMeasurer.measure(label, TextStyle(color = TextSecondary, fontSize = 9.sp))
                    drawText(
                        textMeasurer = textMeasurer,
                        text = label,
                        topLeft = Offset(currentX + barWidth / 2 - labelResult.size.width / 2, zeroY + (if (value < 0) barHeight else 0f) + 20f)
                    )

                    currentX += barWidth + gap
                }
            }
        }
    }
}