package com.example.vndbapp.presentation.screens.vndetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.vndbapp.data.model.VisualNovel
import com.example.vndbapp.ui.theme.BgApp
import com.example.vndbapp.ui.theme.Primary

@Composable
fun VNHeaderImage(vn: VisualNovel) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {

        AsyncImage(
            model = vn.image.thumbnail,
            contentDescription = vn.title,
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, BgApp),
                        startY = 120f
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary.copy(alpha = 0.06f))
        )
    }
}
