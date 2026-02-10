package com.example.vndbapp.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.vndbapp.R

@Composable
fun ImageCard(
    imageUrl: String,
    vnId: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp, pressedElevation = 24.dp),
        modifier = modifier
            .padding(top = 1.dp)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() },
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current).data(imageUrl)
                .crossfade(true).build(),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier
                .width(200.dp)
                .height(300.dp)
        )
    }
}

@Composable
fun ImageCardDetails(
    imageThumbnail: String,
    modifier: Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp, pressedElevation = 24.dp),
        modifier = modifier
            .padding(top = 1.dp)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current).data(imageThumbnail)
                .crossfade(true).build(),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier
                .width(200.dp)
                .height(300.dp)
        )
    }
}
