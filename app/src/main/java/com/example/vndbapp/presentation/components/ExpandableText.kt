package com.example.vndbapp.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vndbapp.presentation.screens.vndetails.Mono
import com.example.vndbapp.presentation.screens.vndetails.SpaceGrotesk
import com.example.vndbapp.ui.theme.Primary
import com.example.vndbapp.ui.theme.TextSecondary

@Composable
fun ExpandableText(
    text: String,
    minimizedMaxLines: Int = 4
) {
    var expanded by remember { mutableStateOf(false) }
    var showToggle by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.animateContentSize()
    ) {

        Text(
            text = text,
            fontFamily = SpaceGrotesk,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = TextSecondary,
            maxLines = if (expanded) Int.MAX_VALUE else minimizedMaxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { result ->
                if (!expanded) {
                    showToggle = result.hasVisualOverflow
                }
            }
        )

        if (showToggle) {
            Spacer(Modifier.height(6.dp))

            Text(
                text = if (expanded) "READ LESS" else "READ MORE",
                fontFamily = Mono,
                fontSize = 10.sp,
                letterSpacing = 1.5.sp,
                color = Primary,
                modifier = Modifier.clickable {
                    expanded = !expanded
                }
            )
        }
    }
}
