package com.example.finalpro.Ui1.Components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.finalpro.Ui1.Screens.Auth.financeTextFieldColors
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun AmountTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    isError: Boolean = false
) {
    val decimalFormat = remember {
        val symbols = DecimalFormatSymbols(Locale.getDefault())
        symbols.groupingSeparator = ','
        symbols.decimalSeparator = '.'
        DecimalFormat("#,###.##", symbols).apply {
            isGroupingUsed = true
            maximumFractionDigits = 2
            minimumFractionDigits = 0
        }
    }

    var formatted by remember(value) { mutableStateOf(formatNumber(value, decimalFormat)) }

    OutlinedTextField(
        value = formatted,
        onValueChange = { newText ->
            val raw = newText.replace(",", "")
            val filtered = raw.filter { it.isDigit() || it == '.' }
            if (filtered.count { it == '.' } > 1) return@OutlinedTextField
            onValueChange(filtered)
            formatted = formatNumber(filtered, decimalFormat)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = singleLine,
        shape = RoundedCornerShape(14.dp),
        colors = financeTextFieldColors(),
        modifier = modifier,
        enabled = enabled,
        isError = isError
    )
}

private fun formatNumber(raw: String, formatter: DecimalFormat): String {
    if (raw.isEmpty()) return ""
    return try {
        val number = raw.toDouble()
        formatter.format(number)
    } catch (e: NumberFormatException) {
        raw
    }
}