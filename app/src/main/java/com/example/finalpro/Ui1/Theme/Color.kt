package com.example.finalpro.Ui1.Theme

import androidx.compose.ui.graphics.Color

// ─── Fondos ───────────────────────────────────────────────
val BgPrimary     = Color(0xFFF4F5F8)   // gris claro app background
val BgSurface     = Color(0xFFFFFFFF)   // superficies blancas
val BgCard        = Color(0xFFFFFFFF)   // tarjetas
val BgCardAlt     = Color(0xFFF0F2F5)   // tarjeta alternativa
val Border        = Color(0xFFE2E6ED)   // bordes suaves

// ─── Verde principal ──────────────────────────────────────
val GreenPrimary  = Color(0xFF1A7A4A)   // verde principal
val GreenLight    = Color(0xFFE8F5EE)   // fondo verde claro
val GreenGlow     = Color(0x221A7A4A)   // resplandor verde

// ─── Semántica financiera ─────────────────────────────────
val ColorIngreso  = Color(0xFF1A7A4A)   // verde = ingresos/saldo positivo
val ColorGasto    = Color(0xFFC0392B)   // rojo = error/gasto/bloqueo
val ColorWarning  = Color(0xFFB45309)   // ámbar = advertencia
val ColorInfo     = Color(0xFF1D4ED8)   // azul = metas/progreso

// ─── Texto ────────────────────────────────────────────────
val TextPrimary   = Color(0xFF0F1117)   // texto oscuro principal
val TextSecondary = Color(0xFF5C6780)   // texto secundario
val TextMuted     = Color(0xFF9BA5B8)   // texto apagado

// ─── Tarjeta balance premium (dark) ──────────────────────
val CardDarkBg    = Color(0xFF0F1117)   // fondo tarjeta premium

// ─── Paleta de gráficos ───────────────────────────────────
val ChartPalette = listOf(
    Color(0xFF1A7A4A), Color(0xFF1D4ED8), Color(0xFFC0392B),
    Color(0xFFB45309), Color(0xFF7C3AED), Color(0xFF0891B2),
    Color(0xFFD97706), Color(0xFF059669)
)

val AccentPrimary = Color(0xFF1D4ED8)   // Azul principal para botones e iconos
val AccentGlow    = Color(0x331D4ED8)   // Resplandor sutil (opcional, usado en LoginScreen)
val AccentSecond  = Color(0xFF7C3AED)   // Púrpura para gráficos secundarios (si se usa)