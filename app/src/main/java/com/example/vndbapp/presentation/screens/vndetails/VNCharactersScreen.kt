package com.example.vndbapp.presentation.screens.vndetails

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.vndbapp.data.model.CharacterRole
import com.example.vndbapp.data.model.Resource
import com.example.vndbapp.data.model.VNCharacter
import com.example.vndbapp.presentation.viewmodel.VisualNovelDetailViewModel
import com.example.vndbapp.ui.theme.BgApp
import com.example.vndbapp.ui.theme.TextPrimary
import com.example.vndbapp.ui.theme.TextSecondary
import kotlinx.coroutines.delay

// ─────────────────────────────────────────────────────────────────────────────
// THEME TOKENS
// ─────────────────────────────────────────────────────────────────────────────

private val TermBg = BgApp
private val TermFg = TextPrimary
private val TermMuted = TextSecondary

// ─────────────────────────────────────────────────────────────────────────────
// ROLE HELPER
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Resolves a character's role for a given VN ID and converts it to a
 * terminal-style display label.
 */
private fun VNCharacter.roleForVn(vnId: String): String {
    val role = vns.firstOrNull { it.id == vnId }?.role
        ?: vns.firstOrNull()?.role
        ?: role?.let {
            runCatching {
                CharacterRole.valueOf(it.uppercase())
            }.getOrNull()
        }
    return when (role) {
        CharacterRole.MAIN -> "PROTAGONIST"
        CharacterRole.PRIMARY -> "PRIMARY_CAST"
        CharacterRole.SIDE -> "SIDE_CAST"
        CharacterRole.APPEARS -> "APPEARS"
        else -> "UNKNOWN"
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// MODIFIER EXTENSIONS
// ─────────────────────────────────────────────────────────────────────────────

/** Thin 1dp border that wraps any composable */
fun Modifier.terminalBorder(): Modifier = border(1.dp, TermFg)

/** Left-edge 2dp accent line */
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

// ─────────────────────────────────────────────────────────────────────────────
// 1. MAIN SCREEN
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun VNCharactersScreen(
    visualNovelId: String,
    viewModel: VisualNovelDetailViewModel = hiltViewModel(),
) {
    val characters by viewModel.characters.collectAsState()

    LaunchedEffect(visualNovelId) {
        viewModel.loadCharacters(visualNovelId)
    }

    // Find the protagonist by role "main" for this specific VN
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

// ─────────────────────────────────────────────────────────────────────────────
// 2. HEADER
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun TerminalHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = TermFg,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "[ STATUS: AUTHORIZED ]\nFILE: REC_0421_LOG",
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            lineHeight = 16.sp,
            color = TermFg
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 3. PROTAGONIST DOSSIER
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun ProtagonistDossier(character: VNCharacter?, vnId: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .terminalBorder()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section label is dynamic — shows actual role or fallback
        TerminalSectionLabel(text = character?.roleForVn(vnId) ?: "PROTAGONIST")

        if (character == null) {
            Text(
                text = "[ NO DATA ]",
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = TermMuted
            )
            return@Column
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Portrait
            AsyncImage(
                model = character.image?.url,
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(128.dp)
                    .terminalBorder()
                    .padding(4.dp)
            )

            // Stats
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "NAME: ${character.name.uppercase()}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TermFg
                )
                TerminalStatBlock(
                    "ID" to character.id.uppercase(),
                    "ROLE" to character.roleForVn(vnId)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 4. CHARACTERS SECTION
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun VnCharactersParts(characters: Resource<List<VNCharacter>>, vnId: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .terminalBorder()
            .padding(16.dp)
    ) {
        when (characters) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = TermFg,
                        strokeWidth = 2.dp
                    )
                }
            }

            is Resource.Success -> {
                // Exclude the protagonist (role "main") from this list
                val charList = characters.data.filter { it.vns.find { it.id == vnId }?.role != CharacterRole.MAIN }

                if (charList.isEmpty()) {
                    Text(
                        text = "[ NO CHARACTERS FOUND ]",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = TermMuted,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
                    ) {
                        item {
                            TerminalSectionLabel(text = "SUPPORTING_CAST")
                            Spacer(Modifier.height(12.dp))
                        }
                        items(charList) { character ->
                            SupportingCharacterCard(character = character, vnId = vnId)
                        }
                    }
                }
            }

            is Resource.Error -> {
                Text(
                    text = "[ ${characters.message} ]",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    color = TermMuted,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 5. SYNOPSIS LOG (typewriter effect)
// ─────────────────────────────────────────────────────────────────────────────

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

// ─────────────────────────────────────────────────────────────────────────────
// 6. FOOTER
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun TerminalFooter() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "SYSTEM_VER_3.0.4",
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            fontStyle = FontStyle.Italic,
            color = TermMuted
        )
        Text(
            text = "ENCRYPTION: ENABLED",
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            fontStyle = FontStyle.Italic,
            color = TermMuted
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 7. SHARED ATOMS
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun ScanlineOverlay() {
    val transition = rememberInfiniteTransition(label = "scanline")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanlineY"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val y = size.height * progress
        drawLine(
            color = Color.White.copy(alpha = 0.05f),
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 2.dp.toPx()
        )
    }
}

@Composable
fun TerminalSectionLabel(text: String) {
    Text(
        text = text,
        fontFamily = FontFamily.Monospace,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
        color = TermBg,
        modifier = Modifier
            .background(TermFg)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

@Composable
fun TerminalStatBlock(vararg entries: Pair<String, String>) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        entries.forEach { (key, value) ->
            Text(
                text = "$key: $value",
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                lineHeight = 16.sp,
                color = TermMuted
            )
        }
    }
}

@Composable
fun TerminalIconButton(
    onClick: () -> Unit,
    label: String,
) {
    Box(
        modifier = Modifier
            .border(1.dp, TermFg)
            .clickable(onClick = onClick)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontFamily = FontFamily.Monospace,
            fontSize = 18.sp,
            color = TermFg
        )
    }
}

@Composable
fun SupportingCharacterCard(character: VNCharacter, vnId: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .terminalBorder()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = character.image?.url,
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .terminalBorder()
                    .padding(2.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "NAME: ${character.name.uppercase()}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TermFg
                )
                TerminalStatBlock(
                    "ID" to character.id.uppercase(),
                    "ROLE" to character.roleForVn(vnId),   // dynamic
                    "STATUS" to "ACTIVE"
                )
            }
        }
    }
}

@Composable
fun CharacterAvatar(name: String, imageUrl: String) {
    Column(
        modifier = Modifier
            .width(72.dp)
            .terminalLeftBorder()
            .padding(start = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.Start
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .terminalBorder()
        )
        Text(
            text = name.uppercase().take(10),
            fontFamily = FontFamily.Monospace,
            fontSize = 8.sp,
            color = TermFg,
            maxLines = 1
        )
    }
}