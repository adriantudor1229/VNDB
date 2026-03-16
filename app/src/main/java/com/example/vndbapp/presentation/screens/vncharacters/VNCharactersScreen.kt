package com.example.vndbapp.presentation.screens.vncharacters

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vndbapp.data.model.CharacterRole
import com.example.vndbapp.data.model.Resource
import com.example.vndbapp.data.model.VNCharacter
import com.example.vndbapp.presentation.animations.ScanlineOverlay
import com.example.vndbapp.presentation.viewmodel.VisualNovelDetailViewModel
import com.example.vndbapp.ui.theme.BgApp
import com.example.vndbapp.ui.theme.TextPrimary
import com.example.vndbapp.ui.theme.TextSecondary

val TermBg = BgApp
val TermFg = TextPrimary
val TermMuted = TextSecondary

fun Modifier.terminalBorder(): Modifier = border(1.dp, TermFg)

fun Modifier.terminalLeftBorder(
    color: Color = TermFg,
    width: Dp = 2.dp,
): Modifier = drawBehind {
    drawLine(
        color = color,
        start = Offset(0f, 0f),
        end = Offset(0f, size.height),
        strokeWidth = width.toPx()
    )
}

@Composable
fun VNCharactersScreen(
    visualNovelId: String,
    viewModel: VisualNovelDetailViewModel = hiltViewModel(),
) {
    val characters by viewModel.characters.collectAsState()

    LaunchedEffect(visualNovelId) {
        viewModel.loadCharacters(visualNovelId)
    }

    val protagonist = when (characters) {
        is Resource.Success -> (characters as Resource.Success<List<VNCharacter>>)
            .data
            .firstOrNull { it.vns.find { it.id == visualNovelId }?.role == CharacterRole.MAIN }

        else -> null
    }

    val synopsis = "Divergence ratio: 1.048596. The timeline remains unstable. " +
            "Worldline shifts are imminent. [RECORD ENDS]"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TermBg)
    ) {
        ScanlineOverlay()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TerminalHeader()
            ProtagonistDossier(character = protagonist, vnId = visualNovelId)
            VnCharactersParts(characters = characters, vnId = visualNovelId)
            SynopsisLog(fullText = synopsis)
            TerminalFooter()
        }
    }
}
