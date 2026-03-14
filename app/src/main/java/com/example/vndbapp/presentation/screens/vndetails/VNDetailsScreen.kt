package com.example.vndbapp.presentation.screens.vndetails

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vndbapp.R
import com.example.vndbapp.data.model.VisualNovel
import com.example.vndbapp.presentation.components.SectionHeader
import com.example.vndbapp.presentation.viewmodel.VisualNovelDetailViewModel
import com.example.vndbapp.ui.theme.BgApp

val SpaceGrotesk = FontFamily(
    Font(R.font.shared_mono_regular, weight = FontWeight.Normal),
    Font(R.font.spline_bold, weight = FontWeight.Bold),
)
val RadiusXl = RoundedCornerShape(8.dp)
val Mono = FontFamily.Monospace

@Composable
fun VisualNovelDetailScreen(
    visualNovel: VisualNovel,
    onNavigateToCharacters: (String) -> Unit = {},
    viewModel: VisualNovelDetailViewModel = hiltViewModel(),
) {
    val anim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        anim.animateTo(1f, tween(600, easing = EaseOutCubic))
    }

    LaunchedEffect(visualNovel.id) {
        viewModel.loadCharacters(visualNovel.id)
    }

    Surface(modifier = Modifier.fillMaxSize(), color = BgApp) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .graphicsLayer {
                    alpha = anim.value
                    translationY = (1f - anim.value) * 32f
                }) {

            VNHeaderImage(visualNovel)

            VNInfoSection(visualNovel)

            VNDescriptionSection(visualNovel)

            CharacterHeader(
                onNavigateToCharacters = onNavigateToCharacters,
                visualNovel = visualNovel
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun CharacterHeader(onNavigateToCharacters: (String) -> Unit = {}, visualNovel: VisualNovel) {
    Row(modifier = Modifier.padding(horizontal = 24.dp)) {
        SectionHeader(
            title = "CHARACTERS",
            onClick = { onNavigateToCharacters(visualNovel.id) })
    }
}