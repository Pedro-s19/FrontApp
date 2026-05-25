package com.example.finalpro.Ui1.Theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Fuente Nunito (requiere archivos en res/font/)
val NunitoFamily = FontFamily(
    Font(com.example.finalpro.R.font.nunito_regular, FontWeight.Normal),
    Font(com.example.finalpro.R.font.nunito_semibold, FontWeight.SemiBold),
    Font(com.example.finalpro.R.font.nunito_bold, FontWeight.Bold)
)

// Tipografía personalizada para toda la app
val FinanceTypography = Typography(
    headlineLarge  = TextStyle(fontFamily = NunitoFamily, fontWeight = FontWeight.Bold,   fontSize = 28.sp),
    headlineMedium = TextStyle(fontFamily = NunitoFamily, fontWeight = FontWeight.Bold,   fontSize = 22.sp),
    titleLarge     = TextStyle(fontFamily = NunitoFamily, fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
    titleMedium    = TextStyle(fontFamily = NunitoFamily, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
    bodyLarge      = TextStyle(fontFamily = NunitoFamily, fontWeight = FontWeight.Normal, fontSize = 15.sp),
    bodyMedium     = TextStyle(fontFamily = NunitoFamily, fontWeight = FontWeight.Normal, fontSize = 13.sp),
    labelSmall     = TextStyle(fontFamily = NunitoFamily, fontWeight = FontWeight.SemiBold, fontSize = 11.sp),
)