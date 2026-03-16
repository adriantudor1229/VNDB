package com.example.vndbapp.presentation.screens.vncharacters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SynopsisLog(fullText: String) {
    var displayed by remember(fullText) { mutableStateOf("") }

    LaunchedEffect(fullText) {
        delay(800L)
        fullText.forEachIndexed { i, _ ->
            displayed = fullText.substring(0, i + 1)
            delay(45L)
        }
    }

    var cursorVisible by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(530L)
            cursorVisible = !cursorVisible
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .terminalBorder()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "SYNOPSIS_LOG >_",
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = TermFg
        )
        Text(
            text = displayed + if (cursorVisible) "█" else " ",
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            lineHeight = 18.sp,
            color = TermFg,
            modifier = Modifier.heightIn(min = 80.dp)
        )
    }
}
