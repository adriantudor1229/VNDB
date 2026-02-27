package com.example.vndbapp.feature_login

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.vndbapp.R
import kotlinx.coroutines.delay

// ── Font ──────────────────────────────────────────────────────────────────────

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val ShareTechMono = FontFamily(
    Font(googleFont = GoogleFont("Share Tech Mono"), fontProvider = provider)
)

val SpaceGrotesk = FontFamily(
    Font(googleFont = GoogleFont("Space Grotesk"), fontProvider = provider)
)

// ── Colors (from Figma HTML) ──────────────────────────────────────────────────

private val BgDark      = Color(0xFF191022)   // background-dark
private val Primary     = Color(0xFF7F13EC)   // primary purple
private val PrimaryGlow = Color(0x667F13EC)   // purple glow
private val PrimaryDim  = Color(0x1A7F13EC)   // subtle purple tint
private val TextPrimary = Color(0xFFF1F5F9)   // slate-100
private val TextMuted   = Color(0xFF94A3B8)   // slate-400
private val BorderIdle  = Color(0xFF334155)   // slate-700
private val IconMuted   = Color(0x4DF1F5F9)   // slate-100 @ 30% (opacity-30)

// ── Concentric-ring logo (matches the HTML icon) ──────────────────────────────

@Composable
private fun DollarsLogo() {
    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(128.dp)) {
            val cx = size.width / 2
            val cy = size.height / 2
            val white = Color(0xFFF1F5F9)

            // Outer ring
            drawCircle(color = white, radius = size.width / 2, style = Stroke(width = 4.dp.toPx()))
            // Middle ring
            drawCircle(color = white, radius = size.width / 4, style = Stroke(width = 2.dp.toPx()))
            // Inner filled dot
            drawCircle(color = white, radius = 12.dp.toPx())
            // Top tick
            drawLine(color = white, start = Offset(cx, 0f), end = Offset(cx, 12.dp.toPx()), strokeWidth = 4.dp.toPx())
            // Bottom tick
            drawLine(color = white, start = Offset(cx, size.height - 12.dp.toPx()), end = Offset(cx, size.height), strokeWidth = 4.dp.toPx())
        }
    }
}

// ── Underline-style input (matches the HTML border-b style) ──────────────────

@Composable
private fun TerminalField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isPassword: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    onDone: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }
    val lineColor by animateColorAsState(
        targetValue = if (isFocused) Primary else BorderIdle,
        animationSpec = tween(200),
        label = "lineColor"
    )

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            fontFamily = ShareTechMono,
            fontSize = 11.sp,
            letterSpacing = 2.sp,
            color = TextMuted,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                visualTransformation = if (isPassword)
                    PasswordVisualTransformation('•') else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(imeAction = imeAction),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                    onDone = { onDone() }
                ),
                textStyle = TextStyle(
                    fontFamily = ShareTechMono,
                    fontSize = 15.sp,
                    color = TextPrimary,
                ),
                cursorBrush = SolidColor(Primary),
                decorationBox = { inner ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontFamily = ShareTechMono,
                                fontSize = 15.sp,
                                color = TextMuted.copy(alpha = 0.5f),
                            )
                        }
                        inner()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isFocused = it.isFocused }
            )
            // Bottom border line
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(lineColor)
            )
        }
    }
}

// ── Login Screen ──────────────────────────────────────────────────────────────

@Composable
fun DollarsLoginScreen(
    onNavigate: () -> Unit = {},
    onJoin: () -> Unit = {},
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val anim = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        anim.animateTo(1f, animationSpec = tween(800, easing = EaseOutCubic))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark),
    ) {
        // ── Background glows ──────────────────────────────────────────────
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x0D7F13EC), Color.Transparent),
                    center = center,
                    radius = size.minDimension
                ),
                radius = size.minDimension
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x1A7F13EC), Color.Transparent),
                    center = Offset(-80f, size.height * 0.25f),
                    radius = 300f
                ),
                radius = 300f,
                center = Offset(-80f, size.height * 0.25f)
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x1A7F13EC), Color.Transparent),
                    center = Offset(size.width + 80f, size.height * 0.75f),
                    radius = 300f
                ),
                radius = 300f,
                center = Offset(size.width + 80f, size.height * 0.75f)
            )
        }

        // ── Content ───────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = anim.value
                    translationY = (1f - anim.value) * 40f
                }
                .systemBarsPadding()                 // respects status + nav bar
                .padding(horizontal = 32.dp)
                .padding(top = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {

            // ── Logo + Title ──────────────────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                DollarsLogo()
                Text(
                    text = "DOLLARS",
                    fontFamily = SpaceGrotesk,
                    fontSize = 36.sp,          // text-4xl = 2.25rem ≈ 36sp
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    letterSpacing = 10.8.sp,   // tracking-[0.3em] = 0.3 * 36sp
                )
            }

            // ── Form ──────────────────────────────────────────────────────
            Column(modifier = Modifier.fillMaxWidth()) {
                TerminalField(
                    value = username,
                    onValueChange = { username = it },
                    label = "USERNAME",
                    placeholder = "_",
                )
                Spacer(Modifier.height(20.dp))
                TerminalField(
                    value = password,
                    onValueChange = { password = it },
                    label = "PASS",
                    placeholder = "••••••",
                    isPassword = true,
                    imeAction = ImeAction.Done,
                    onDone = { onNavigate() },
                )
                Spacer(Modifier.height(28.dp))

                var pressed by remember { mutableStateOf(false) }
                val scale by animateFloatAsState(
                    targetValue = if (pressed) 0.97f else 1f,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "scale"
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer { scaleX = scale; scaleY = scale }
                        .background(Primary, RoundedCornerShape(4.dp))
                        .clickable { pressed = true; onNavigate() }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ENTER",
                        fontFamily = ShareTechMono,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 7.sp,
                    )
                }
            }

            // ── Join + Icons ──────────────────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // Join link with blinking cursor
                Row(
                    modifier = Modifier.clickable { onJoin() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = ">_ NO ACCOUNT? JOIN THE DOLLARS",
                        fontFamily = ShareTechMono,
                        fontSize = 11.sp,
                        color = TextMuted,
                        letterSpacing = 1.sp,
                    )
                    var cursorVisible by remember { mutableStateOf(true) }
                    LaunchedEffect(Unit) {
                        while (true) { delay(530); cursorVisible = !cursorVisible }
                    }
                    Box(
                        modifier = Modifier
                            .width(6.dp)
                            .height(13.dp)
                            .alpha(if (cursorVisible) 1f else 0f)
                            .background(TextMuted)
                    )
                }

                // Icons row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(28.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.alpha(0.3f)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Terminal,
                        contentDescription = "Terminal",
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Icon(
                        imageVector = Icons.Outlined.Shield,
                        contentDescription = "Shield",
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Icon(
                        imageVector = Icons.Outlined.LockOpen,
                        contentDescription = "Lock Open",
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true, widthDp = 380, heightDp = 820)
@Composable
fun DollarsLoginScreenPreview() {
    DollarsLoginScreen()
}