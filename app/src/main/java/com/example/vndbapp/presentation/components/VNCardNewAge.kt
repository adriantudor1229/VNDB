package com.example.vndbapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vndbapp.ui.theme.*

@Composable
fun VNCard(
    title: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    var pressed by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = if (pressed) Color(0x8034A4F4) else BorderDefault,
        animationSpec = tween(200),
        label = "cardBorder"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(BgCard)
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .clickable { pressed = true; onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        // ── Circular thumbnail ────────────────────────────────────────────
        AsyncImage(
            model = imageUrl,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(BgThumb)
                .border(1.dp, BorderMuted, CircleShape),
        )

        // ── Text content ──────────────────────────────────────────────────
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "100%_SYNC",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    fontSize = 9.sp,
                    letterSpacing = 0.5.sp,
                    color = Primary,
                    modifier = Modifier
                        .border(1.dp, Color(0x4D34A4F4), RoundedCornerShape(2.dp))
                        .padding(horizontal = 4.dp, vertical = 1.dp),
                )
                Text(
                    text = "UPDATED: 22.02.2026",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp,
                    letterSpacing = 0.3.sp,
                    color = TextMuted,
                )
            }
        }

        // ── Chevron ───────────────────────────────────────────────────────
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Open",
            tint = if (pressed) Primary else TextDisabled,
            modifier = Modifier.size(20.dp),
        )
    }
}