package com.example.vndbapp.presentation.screens.vndetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vndbapp.data.model.VisualNovel
import com.example.vndbapp.ui.theme.Primary
import com.example.vndbapp.ui.theme.TextPrimary

@Composable
fun VNInfoSection(vn: VisualNovel) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {

        Spacer(Modifier.height(4.dp))

        Text(
            text = "> FILE_ID: ${vn.id}",
            fontFamily = Mono,
            fontSize = 10.sp,
            color = Primary.copy(0.60f),
            letterSpacing = 1.sp
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = vn.title,
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            lineHeight = 32.sp,
            color = TextPrimary
        )

        Spacer(Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Primary)
            )

            Text(
                text = "STATUS: INDEXED",
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = Primary,
                letterSpacing = 2.sp
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}
