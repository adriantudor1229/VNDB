package com.example.vndbapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.vndbapp.R

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage   = "com.google.android.gms",
    certificates      = R.array.com_google_android_gms_fonts_certs,
)

val InterFont = FontFamily(
    Font(googleFont = GoogleFont("Inter"), fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = GoogleFont("Inter"), fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = GoogleFont("Inter"), fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = GoogleFont("Inter"), fontProvider = provider, weight = FontWeight.Bold),
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily   = InterFont,
        fontWeight   = FontWeight.Bold,
        fontSize     = 20.sp,
        lineHeight   = 28.sp,
        letterSpacing = (-0.5).sp,
    ),
    titleMedium = TextStyle(
        fontFamily   = InterFont,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 15.sp,
        lineHeight   = 22.sp,
        letterSpacing = (-0.2).sp,
    ),
    bodyLarge = TextStyle(
        fontFamily   = InterFont,
        fontWeight   = FontWeight.Normal,
        fontSize     = 16.sp,
        lineHeight   = 24.sp,
        letterSpacing = 0.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily   = InterFont,
        fontWeight   = FontWeight.Normal,
        fontSize     = 14.sp,
        lineHeight   = 20.sp,
        letterSpacing = 0.sp,
    ),
    labelSmall = TextStyle(
        fontFamily   = InterFont,
        fontWeight   = FontWeight.Bold,
        fontSize     = 10.sp,
        lineHeight   = 14.sp,
        letterSpacing = 1.5.sp,
    ),
    labelMedium = TextStyle(
        fontFamily   = InterFont,
        fontWeight   = FontWeight.Bold,
        fontSize     = 11.sp,
        lineHeight   = 16.sp,
        letterSpacing = 1.sp,
    ),
)
