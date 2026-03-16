package com.example.vndbapp.presentation.screens.vncharacters

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TerminalFooter() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "SYSTEM_VER_3.0.4",
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            fontStyle = FontStyle.Italic,
            color = TermMuted
        )
        Text(
            text = "ENCRYPTION: ENABLED",
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            fontStyle = FontStyle.Italic,
            color = TermMuted
        )
    }
}

@Composable
fun TerminalSectionLabel(text: String) {
    Text(
        text = text,
        fontFamily = FontFamily.Monospace,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
        color = TermBg,
        modifier = Modifier
            .background(TermFg)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

@Composable
fun TerminalStatBlock(vararg entries: Pair<String, String>) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        entries.forEach { (key, value) ->
            Text(
                text = "$key: $value",
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                lineHeight = 16.sp,
                color = TermMuted
            )
        }
    }
}

@Composable
fun TerminalIconButton(
    onClick: () -> Unit,
    label: String,
) {
    Box(
        modifier = Modifier
            .border(1.dp, TermFg)
            .clickable(onClick = onClick)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontFamily = FontFamily.Monospace,
            fontSize = 18.sp,
            color = TermFg
        )
    }
}
