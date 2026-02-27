package com.example.vndbapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Dark color scheme (primary app experience) ────────────────────────────────
private val AppDarkColorScheme = darkColorScheme(
    primary          = Primary,
    onPrimary        = Color(0xFF0A0A0A),
    primaryContainer = PrimaryDim,
    onPrimaryContainer = Primary,

    background       = BgApp,
    onBackground     = TextPrimary,

    surface          = BgCard,
    onSurface        = TextPrimary,
    surfaceVariant   = BgThumb,
    onSurfaceVariant = TextSecondary,

    outline          = BorderDefault,
    outlineVariant   = BorderMuted,

    secondary        = TextSecondary,
    onSecondary      = BgApp,
    tertiary         = StatusHold,
    onTertiary       = BgApp,

    error            = Color(0xFFEF4444),
    onError          = Color.White,
)

// ── Light scheme (fallback — app is dark-first) ───────────────────────────────
private val AppLightColorScheme = lightColorScheme(
    primary          = Color(0xFF0284C7),
    onPrimary        = Color.White,
    background       = Color(0xFFF5F7F8),
    onBackground     = Color(0xFF0F172A),
    surface          = Color(0xFFFFFFFF),
    onSurface        = Color(0xFF0F172A),
)

// ── Theme entry point ─────────────────────────────────────────────────────────
@Composable
fun VNDBAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    // Dynamic color intentionally disabled — we own the exact palette
    val colorScheme = if (darkTheme) AppDarkColorScheme else AppLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content,
    )
}