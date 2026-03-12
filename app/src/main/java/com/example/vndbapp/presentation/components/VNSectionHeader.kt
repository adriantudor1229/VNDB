package com.example.vndbapp.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vndbapp.presentation.screens.vndetails.Mono
import com.example.vndbapp.ui.theme.Primary
import com.example.vndbapp.ui.theme.TextPrimary

@Composable
fun SectionHeader(title: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    Primary,
                    Offset(0f, 0f),
                    Offset(0f, size.height),
                    4.dp.toPx()
                )
            }
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            fontFamily = Mono,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            letterSpacing = 3.sp,
            color = TextPrimary
        )
    }
}
