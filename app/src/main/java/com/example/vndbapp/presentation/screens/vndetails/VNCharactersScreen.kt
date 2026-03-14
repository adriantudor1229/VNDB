package com.example.vndbapp.presentation.screens.vndetails

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vndbapp.data.model.Resource
import com.example.vndbapp.data.model.VNCharacter
import com.example.vndbapp.presentation.viewmodel.VisualNovelDetailViewModel
import com.example.vndbapp.ui.theme.*
import kotlinx.coroutines.delay
import java.io.Serializable

// ─────────────────────────────────────────────────────────────────────────────
// THEME TOKENS
// Using existing project theme colors
// ─────────────────────────────────────────────────────────────────────────────

private val TermBg = BgApp
private val TermFg = TextPrimary
private val TermMuted = TextSecondary

// No filter - images will display in full color

// ─────────────────────────────────────────────────────────────────────────────
// MODIFIER EXTENSIONS
// ─────────────────────────────────────────────────────────────────────────────

/** Thin 1dp white border that wraps any composable — matches .terminal-border */
fun Modifier.terminalBorder(): Modifier = border(1.dp, TermFg)

/** Left-edge 2dp white accent line — matches CSS border-l-2 */
fun Modifier.terminalLeftBorder(
    color: Color = TermFg,
    width: Dp = 2.dp
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

/**
 * Root screen composable.
 *
 * Usage:
 *   VNCharactersScreen(
 *       visualNovelId = vnId,
 *       viewModel = viewModel
 *   )
 *
 * @param visualNovelId The ID of the visual novel to display characters for.
 * @param ViewModel to load character data.
 */
@Composable
fun VNCharactersScreen(
    visualNovelId: String,
    viewModel: VisualNovelDetailViewModel = hiltViewModel(),
) {
    val characters by viewModel.characters.collectAsState()

    LaunchedEffect(visualNovelId) {
        viewModel.loadCharacters(visualNovelId)
    }

    // For now, use placeholder data - in a real implementation you'd extract protagonist
    val protagonist = when (characters) {
        is Resource.Success -> (characters as Resource.Success<List<VNCharacter>>).data.firstOrNull()
        else -> null
    }

    val synopsis = "Divergence ratio: 1.048596. The timeline remains unstable. " +
                   "Worldline shifts are imminent. [RECORD ENDS]"
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TermBg)
    ) {
        // Purely decorative — sits above the background, below content
        ScanlineOverlay()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TerminalHeader()
            ProtagonistDossier(character = protagonist)
            VnCharactersParts(characters = characters)
            SynopsisLog(fullText = synopsis)
            TerminalFooter()
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 2. HEADER
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun TerminalHeader(
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                // Only a bottom divider line, no full box border
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

/**
 * Large featured-character block with portrait + stats.
 *
 * Field mapping — adjust to match your actual VNCharacter data class:
 *   character.name       → displayed as NAME
 *   character.image?.url → portrait
 *   character.description → used for empty state
 */
@Composable
fun ProtagonistDossier(character: VNCharacter?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .terminalBorder()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TerminalSectionLabel(text = "PROTAGONIST")

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
            // ── Portrait ─────────────────────────────────────────────────────
            AsyncImage(
                model = character.image?.url,
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                                modifier = Modifier
                    .size(128.dp)
                    .terminalBorder()
                    .padding(4.dp)
            )

            // ── Stats ─────────────────────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "NAME: ${character.name.uppercase()}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TermFg
                )
                TerminalStatBlock(
                    "ID" to "001-ALPHA"
                    // Placeholder - no role field in data model
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 4. CHARACTERS SECTION  (your existing composable, terminal-styled)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Wraps your original VNCharactersSection logic inside a terminal-bordered card.
 * All Resource states and the LazyRow are preserved exactly as you had them.
 */
@Composable
fun VnCharactersParts(characters: Resource<List<VNCharacter>>) {
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
                val charList = characters.data

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
                        // Add section label as the first item
                        item {
                            TerminalSectionLabel(text = "SUPPORTING_CAST")
                            Spacer(Modifier.height(12.dp))
                        }

                        // Add all character cards
                        items(charList) { character ->
                            SupportingCharacterCard(character = character)
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
// 5. SYNOPSIS LOG  (typewriter effect)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun SynopsisLog(fullText: String) {
    // Reveal text character-by-character
    var displayed by remember(fullText) { mutableStateOf("") }

    LaunchedEffect(fullText) {
        delay(800L) // initial pause before typing starts
        fullText.forEachIndexed { i, _ ->
            displayed = fullText.substring(0, i + 1)
            delay(45L) // typing speed — lower = faster
        }
    }

    // Blinking block cursor
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

/**
 * Animated diagonal scanline that drifts down the screen.
 * Drawn on a full-size Canvas — keep it at the bottom of the Box stack.
 */
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

/**
 * White-on-black inverted label pill — e.g. "PROTAGONIST", "SUPPORTING_CAST".
 * Matches the `bg-white text-black` heading style from the HTML.
 */
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

/**
 * Vertical list of KEY: VALUE stat lines.
 * Pass any number of pairs, e.g.  "ID" to "001-ALPHA"
 */
@Composable
fun TerminalStatBlock(entries1: Serializable, vararg entries: Pair<String, String>) {
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

/**
 * Small icon button with a 1dp white border.
 * @param label  Single glyph rendered as the button icon.
 */
@Composable
fun TerminalIconButton(
    onClick: () -> Unit,
    label: String
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

/**
 * Supporting character card with portrait + stats in a column layout.
 * Similar to ProtagonistDossier but smaller and for multiple characters.
 */
@Composable
fun SupportingCharacterCard(character: VNCharacter) {
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
            // Portrait
            AsyncImage(
                model = character.image?.url,
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                                modifier = Modifier
                    .size(80.dp)
                    .terminalBorder()
                    .padding(2.dp)
            )

            // Character info
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "NAME: ${character.name.uppercase()}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TermFg
                )
                TerminalStatBlock(
                    "ID" to character.takeIf { it.id.isNotEmpty() }?.id?.uppercase() ?: "N/A",
                    "ROLE" to "SUPPORTING", // Placeholder - no role field in data model
                    "STATUS" to "ACTIVE"
                )
            }
        }
    }
}

/**
 * Single character card used inside the LazyRow (kept for potential future use).
 *
 * This replaces / merges with your existing CharacterAvatar.
 * Left-border accent + grayscale portrait + name label underneath.
 */
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