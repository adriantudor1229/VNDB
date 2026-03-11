package com.example.vndbapp.presentation.screens.bottomnav

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vndbapp.feature_login.provider

private val SpaceGrotesk = FontFamily(
    Font(
        googleFont = GoogleFont("Space Grotesk"),
        fontProvider = provider,
        weight = FontWeight.Normal
    ),
    Font(
        googleFont = GoogleFont("Space Grotesk"),
        fontProvider = provider,
        weight = FontWeight.Bold
    ),
)
private val Mono = FontFamily.Monospace
private val RadiusLg = RoundedCornerShape(4.dp)
private val RadiusXl = RoundedCornerShape(8.dp)
private val RadiusFull = RoundedCornerShape(12.dp)

private val BgDark = Color(0xFF0A0A0C)
private val Primary = Color(0xFF7F13EC)
private val AccentPurple = Color(0xFFAD92C9)
private val Slate100 = Color(0xFFF1F5F9)
private val Slate400 = Color(0xFF94A3B8)
private val Slate700 = Color(0xFF334155)
private val Green500 = Color(0xFF22C55E)

private fun displayStyle(
    fontSize: Float,
    color: Color,
    weight: FontWeight = FontWeight.Normal,
    letterSpacing: Float = 0f,
) = TextStyle(
    fontFamily = SpaceGrotesk,
    fontWeight = weight,
    fontSize = fontSize.sp,
    color = color,
    letterSpacing = letterSpacing.sp
)

private fun monoStyle(
    fontSize: Float,
    color: Color,
    weight: FontWeight = FontWeight.Normal,
    letterSpacing: Float = 0f,
) = TextStyle(
    fontFamily = Mono,
    fontWeight = weight,
    fontSize = fontSize.sp,
    color = color,
    letterSpacing = letterSpacing.sp
)

@Composable
private fun SectionHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind { drawLine(Primary, Offset(0f, 0f), Offset(0f, size.height), 4.dp.toPx()) }
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = monoStyle(12f, Slate100, FontWeight.Bold, 3f))
    }
}

@Composable
fun SettingsScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(900, easing = EaseInOut), RepeatMode.Reverse),
        label = "pulse"
    )

    Surface(color = BgDark, modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Outlined.Settings,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(20.dp)
                    )

                    Text("Settings.SYS", style = monoStyle(13f, Slate100, FontWeight.Bold, 2f))
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(Modifier.height(16.dp))


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            drawLine(
                                Primary.copy(0.10f),
                                Offset(0f, size.height),
                                Offset(size.width, size.height),
                                1.dp.toPx()
                            )
                        }
                        .padding(bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Box {

                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RadiusFull)
                                .background(Primary.copy(0.20f))
                                .border(2.dp, Primary.copy(0.40f), RadiusFull),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🐾", style = displayStyle(32f, Color.Unspecified))
                        }

                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Green500.copy(pulseAlpha))
                                .border(2.dp, BgDark, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }
                    Column {

                        Text(
                            "User_882194",
                            style = displayStyle(22f, Slate100, FontWeight.Bold, -0.5f)
                        )

                        Text("STATUS: ONLINE", style = monoStyle(10f, Primary, FontWeight.Bold, 2f))
                        Spacer(Modifier.height(2.dp))

                        Text("IP: 192.168.0.12", style = monoStyle(10f, AccentPurple.copy(0.60f)))
                    }
                }

                Spacer(Modifier.height(20.dp))

                SectionHeader("USER STATS")
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("📖  Novels Read", style = monoStyle(13f, AccentPurple.copy(0.80f)))
                    Text("42", style = monoStyle(13f, Slate100, FontWeight.Bold))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("🕐  Hours Logged", style = monoStyle(13f, AccentPurple.copy(0.80f)))
                    Text("156H", style = monoStyle(13f, Slate100, FontWeight.Bold))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("👥  Affiliation", style = monoStyle(13f, AccentPurple.copy(0.80f)))
                    Text("DOLLARS", style = monoStyle(13f, Primary, FontWeight.Bold))
                }

                Spacer(Modifier.height(20.dp))

                SectionHeader("ACHIEVEMENTS")
                Spacer(Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    listOf("⭐", "🌙", "⚡", "<>").forEach { icon ->
                        // rounded-lg → RadiusLg (0.25rem = 4dp)
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RadiusLg)
                                .border(1.dp, Slate700, RadiusLg),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(icon, style = displayStyle(20f, Slate400))
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                SectionHeader("ACTIVITY LOG")
                Spacer(Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(192.dp)
                        .clip(RadiusXl)
                        .background(Color.Black)
                        .border(1.dp, Primary.copy(0.10f), RadiusXl)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    listOf(
                        Pair(Green500, "[10:00:01] SETTINGS_OPENED - SESSION_ACTIVE"),
                        Pair(Slate400, "[10:01:14] DARK_MODE: ENABLED"),
                        Pair(Slate400, "[10:02:30] NOTIFICATIONS: ENABLED"),
                        Pair(Slate400, "[10:03:05] ENCRYPTION: ON"),
                        Pair(Primary, "[10:04:22] AFFILIATION_VERIFIED: \"DOLLARS\""),
                        Pair(Slate400, "[10:05:11] SESSION_TOKEN: REFRESHED"),
                        Pair(Slate400, "[10:06:00] SYSTEM_WAITING_FOR_INPUT..."),
                    ).forEach { (color, line) ->
                        Text(line, style = monoStyle(10f, color).copy(lineHeight = 18.sp))
                        Spacer(Modifier.height(4.dp))
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}