package com.example.vndbapp.ui.theme

import androidx.compose.ui.graphics.Color

// ── Brand ─────────────────────────────────────────────────────────────────────
val Primary       = Color(0xFF34A4F4)   // Dollars blue  (#34a4f4)
val PrimaryDim    = Color(0x1A34A4F4)   // blue/10
val PrimaryBorder = Color(0x4D34A4F4)   // blue/30

// ── Backgrounds ───────────────────────────────────────────────────────────────
val BgApp         = Color(0xFF0A0A0A)   // background-dark
val BgCard        = Color(0xFF0D1117)   // slate-900/30 equivalent
val BgThumb       = Color(0xFF1E293B)   // slate-800

// ── Borders ───────────────────────────────────────────────────────────────────
val BorderDefault = Color(0xFF1E293B)   // slate-800
val BorderMuted   = Color(0xFF334155)   // slate-700

// ── Text ──────────────────────────────────────────────────────────────────────
val TextPrimary   = Color(0xFFF1F5F9)   // slate-100
val TextSecondary = Color(0xFF94A3B8)   // slate-400
val TextMuted     = Color(0xFF64748B)   // slate-500
val TextDisabled  = Color(0xFF475569)   // slate-600

// ── Status badge colors ───────────────────────────────────────────────────────
val StatusSync    = Primary             // 100%_SYNC  → blue
val StatusActive  = Color(0xFF94A3B8)   // %_ACTIVE   → slate-400
val StatusHold    = Color(0xFFCA8A04)   // ON_HOLD    → yellow-600
val StatusPending = Color(0xFF475569)   // 0%_PENDING → slate-600

// ── Chat user accent colors ───────────────────────────────────────────────────
val Pink500        = Color(0xFFEC4899)
val Pink500Dim     = Color(0x4DEC4899)
val Pink500Faint   = Color(0x1AEC4899)
val Emerald500     = Color(0xFF10B981)
val Emerald500Dim  = Color(0x4D10B981)
val Emerald500Faint= Color(0x1A10B981)
val Amber500       = Color(0xFFF59E0B)
val Amber500Dim    = Color(0x4DF59E0B)
val Amber500Faint  = Color(0x1AF59E0B)

// ── Legacy (kept so existing files don't break) ───────────────────────────────
val Purple80      = Color(0xFFD0BCFF)
val PurpleGrey80  = Color(0xFFCCC2DC)
val Pink80        = Color(0xFFEFB8C8)
val Purple40      = Color(0xFF6650A4)
val PurpleGrey40  = Color(0xFF625B71)
val Pink40        = Color(0xFF7D5260)