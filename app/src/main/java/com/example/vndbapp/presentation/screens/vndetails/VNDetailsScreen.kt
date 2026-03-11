package com.example.vndbapp.presentation.screens.vndetails

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.vndbapp.R
import com.example.vndbapp.data.mapper.stripBBCode
import com.example.vndbapp.data.model.Resource
import com.example.vndbapp.data.model.VNCharacter
import com.example.vndbapp.data.model.VisualNovel
import com.example.vndbapp.presentation.components.CharacterAvatar
import com.example.vndbapp.presentation.viewmodel.VisualNovelDetailViewModel
import com.example.vndbapp.ui.theme.BgApp
import com.example.vndbapp.ui.theme.BgCard
import com.example.vndbapp.ui.theme.Primary
import com.example.vndbapp.ui.theme.TextMuted
import com.example.vndbapp.ui.theme.TextPrimary
import com.example.vndbapp.ui.theme.TextSecondary

private val SpaceGrotesk = FontFamily(
    Font(R.font.shared_mono_regular, weight = FontWeight.Normal),
    Font(R.font.spline_bold, weight = FontWeight.Bold),
)
private val Mono = FontFamily.Monospace
private val RadiusXl = RoundedCornerShape(8.dp)

@Composable
fun VisualNovelDetailScreen(
    visualNovel: VisualNovel,
    viewModel: VisualNovelDetailViewModel = hiltViewModel(),
) {
    val anim = remember { Animatable(0f) }
    val characters by viewModel.characters.collectAsState()

    LaunchedEffect(Unit) {
        anim.animateTo(1f, animationSpec = tween(600, easing = EaseOutCubic))
    }

    LaunchedEffect(visualNovel.id) {
        viewModel.loadCharacters(vnId = visualNovel.id)
    }

    Surface(modifier = Modifier.fillMaxSize(), color = BgApp) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .graphicsLayer {
                    alpha = anim.value
                    translationY = (1f - anim.value) * 32f
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {
                AsyncImage(
                    model = visualNovel.image.thumbnail,
                    contentDescription = visualNovel.title,
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
                                startY = 120f,
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Primary.copy(alpha = 0.06f))
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(Modifier.height(4.dp))

                Text(
                    text = "> FILE_ID: ${visualNovel.id}",
                    fontFamily = Mono,
                    fontSize = 10.sp,
                    color = Primary.copy(0.60f),
                    letterSpacing = 1.sp
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = visualNovel.title,
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    lineHeight = 32.sp,
                    color = TextPrimary,
                    letterSpacing = (-0.5).sp
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

                // Description header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            drawLine(Primary, Offset(0f, 0f), Offset(0f, size.height), 4.dp.toPx())
                        }
                        .padding(start = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DESCRIPTION",
                        fontFamily = Mono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 3.sp,
                        color = TextPrimary
                    )
                }

                Spacer(Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RadiusXl)
                        .background(BgCard)
                        .border(1.dp, Primary.copy(0.10f), RadiusXl)
                        .padding(16.dp)
                ) {
                    Text(
                        text = visualNovel.description.stripBBCode()
                            ?: "[ NO DESCRIPTION AVAILABLE ]",
                        fontFamily = SpaceGrotesk,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = TextSecondary
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Characters header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            drawLine(Primary, Offset(0f, 0f), Offset(0f, size.height), 4.dp.toPx())
                        }
                        .padding(start = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CHARACTERS",
                        fontFamily = Mono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 3.sp,
                        color = TextPrimary
                    )
                }

                Spacer(Modifier.height(12.dp))

                when (characters) {
                    is Resource.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Primary,
                                strokeWidth = 2.dp,
                            )
                        }
                    }
                    is Resource.Success -> {
                        val charList = (characters as Resource.Success<List<VNCharacter>>).data
                        if (charList.isEmpty()) {
                            Text(
                                text = "[ NO CHARACTERS FOUND ]",
                                fontFamily = Mono,
                                fontSize = 10.sp,
                                color = TextMuted,
                            )
                        } else {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp),
                            ) {
                                items(items = charList) { character ->
                                    CharacterAvatar(
                                        name = character.name,
                                        imageUrl = character.image?.url ?: "",
                                    )
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        Text(
                            text = "[ ${(characters as Resource.Error).message} ]",
                            fontFamily = Mono,
                            fontSize = 10.sp,
                            color = Primary.copy(0.5f),
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}