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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vndbapp.R
import com.example.vndbapp.ui.theme.Amber500
import com.example.vndbapp.ui.theme.Amber500Dim
import com.example.vndbapp.ui.theme.Amber500Faint
import com.example.vndbapp.ui.theme.BgApp
import com.example.vndbapp.ui.theme.BgCard
import com.example.vndbapp.ui.theme.Emerald500
import com.example.vndbapp.ui.theme.Emerald500Dim
import com.example.vndbapp.ui.theme.Emerald500Faint
import com.example.vndbapp.ui.theme.Pink500
import com.example.vndbapp.ui.theme.Pink500Dim
import com.example.vndbapp.ui.theme.Pink500Faint
import com.example.vndbapp.ui.theme.Primary
import com.example.vndbapp.ui.theme.PrimaryBorder
import com.example.vndbapp.ui.theme.PrimaryDim
import com.example.vndbapp.ui.theme.TextPrimary

private val SplineSans = FontFamily(
    Font(R.font.shared_mono_regular, weight = FontWeight.Normal),
    Font(R.font.spline_bold, weight = FontWeight.Bold),
)
private val Mono = FontFamily.Monospace

private val Sharp = RoundedCornerShape(0.dp)

sealed class ChatItem {
    data class SystemLine(val text: String, val sub: String? = null) : ChatItem()
    data class Notice(val text: String) : ChatItem()
    data class TerminalBlock(val lines: List<String>) : ChatItem()
    data class Message(
        val initial: String,
        val name: String,
        val nameColor: Color,
        val avatarBorderColor: Color,
        val avatarBgColor: Color,
        val avatarTextColor: Color,
        val time: String,
        val body: String,
    ) : ChatItem()
}

private val hardcodedItems: List<ChatItem> = listOf(
    ChatItem.SystemLine("- system: connection established -", "IP: 192.168.0.254 | LOC: IKEBUKURO"),
    ChatItem.Message(
        "B",
        "Baccano",
        Primary,
        PrimaryBorder,
        PrimaryDim,
        Primary.copy(0.70f),
        "22:04:11",
        "Is it true? About the headless rider in the underground mall?"
    ),
    ChatItem.Message(
        "K",
        "Kanra",
        Pink500,
        Pink500Dim,
        Pink500Faint,
        Pink500.copy(0.70f),
        "22:05:03",
        "Rumors are like smoke. They only show where the fire is. ＼(￣▽￣)／"
    ),
    ChatItem.Notice("User 'Izaya' has joined the room."),
    ChatItem.Message(
        "S",
        "Setton",
        Emerald500,
        Emerald500Dim,
        Emerald500Faint,
        Emerald500.copy(0.70f),
        "22:06:45",
        "I saw a black motorcycle near the station. It didn't make any sound."
    ),
    ChatItem.Message(
        "B",
        "Baccano",
        Primary,
        PrimaryBorder,
        PrimaryDim,
        Primary.copy(0.70f),
        "22:07:12",
        "That's her. The Dullahan of Ikebukuro."
    ),
    ChatItem.Message(
        "T",
        "Tanaka",
        Amber500,
        Amber500Dim,
        Amber500Faint,
        Amber500.copy(0.70f),
        "22:08:55",
        "Is anyone here part of the Yellow Scarves? I heard they're recruiting again."
    ),
    ChatItem.TerminalBlock(
        listOf(
            "> WARNING: NETWORK TRAFFIC DETECTED",
            "> ENCRYPTION LEVEL: MAXIMUM",
            "> ADMIN STATUS: HIDDEN"
        )
    ),
)

@Composable
fun DollarsChatScreen() {
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(900, easing = EaseInOut), RepeatMode.Reverse),
        label = "pulseAlpha"
    )

    Surface(modifier = Modifier.fillMaxSize(), color = BgApp) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar()
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(hardcodedItems) { item ->
                    when (item) {
                        is ChatItem.SystemLine -> SystemLineItem(item)
                        is ChatItem.Notice -> NoticeItem(item)
                        is ChatItem.TerminalBlock -> TerminalBlockItem(item)
                        is ChatItem.Message -> MessageItem(item)
                    }
                }
            }
            InputArea(inputText, { inputText = it }, pulseAlpha)
        }
    }
}

@Composable
private fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgApp)
            .drawBehind {
                drawLine(
                    Primary.copy(0.10f),
                    Offset(0f, size.height),
                    Offset(size.width, size.height),
                    1.dp.toPx()
                )
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("☰", fontFamily = SplineSans, fontSize = 22.sp, color = Primary)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "[ Dollars ]",
                fontFamily = SplineSans,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 4.sp,
                color = Primary
            )
            Spacer(Modifier.height(2.dp))
            Text(
                "LOG_ID: 0092-B",
                fontFamily = Mono,
                fontSize = 9.sp,
                letterSpacing = 0.5.sp,
                color = Primary.copy(0.40f)
            )
        }
        Text("👥", fontFamily = SplineSans, fontSize = 20.sp, color = Primary.copy(0.60f))
    }
}

@Composable
private fun SystemLineItem(item: ChatItem.SystemLine) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(color = Primary.copy(0.20f), thickness = 1.dp)
        Spacer(Modifier.height(8.dp))
        Text(item.text, fontFamily = Mono, fontSize = 9.sp, letterSpacing = 2.sp, color = Primary)
        item.sub?.let {
            Spacer(Modifier.height(4.dp))
            Text(it, fontFamily = Mono, fontSize = 8.sp, color = Primary.copy(0.50f))
        }
    }
}

@Composable
private fun NoticeItem(item: ChatItem.Notice) {
    Row(modifier = Modifier.padding(start = 48.dp)) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(IntrinsicSize.Min)
                .background(Primary.copy(0.30f))
        )
        Spacer(Modifier.width(12.dp))
        Text(
            "[Notice] ${item.text}",
            fontFamily = Mono,
            fontSize = 9.sp,
            color = Primary.copy(0.40f),
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
private fun TerminalBlockItem(item: ChatItem.TerminalBlock) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Primary.copy(0.05f))
            .drawBehind {
                drawLine(
                    Primary.copy(0.40f),
                    Offset(0f, 0f),
                    Offset(0f, size.height),
                    2.dp.toPx()
                )
            }
            .padding(12.dp)
    ) {
        Column {
            item.lines.forEach { line ->
                Text(line, fontFamily = Mono, fontSize = 10.sp, color = Primary, lineHeight = 16.sp)
            }
        }
    }
}

@Composable
private fun MessageItem(item: ChatItem.Message) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(item.avatarBgColor)
                .border(1.dp, item.avatarBorderColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                item.initial,
                fontFamily = SplineSans,
                fontSize = 9.sp,
                color = item.avatarTextColor
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    item.name,
                    fontFamily = SplineSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    letterSpacing = (-0.3).sp,
                    color = item.nameColor
                )
                Text(item.time, fontFamily = Mono, fontSize = 9.sp, color = Primary.copy(0.30f))
            }
            Spacer(Modifier.height(2.dp))
            Text(
                item.body,
                fontFamily = SplineSans,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = TextPrimary,
                modifier = Modifier.fillMaxWidth(0.90f)
            )
        }
    }
}

@Composable
private fun InputArea(inputText: String, onTextChange: (String) -> Unit, pulseAlpha: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    Primary.copy(0.10f),
                    Offset(0f, 0f),
                    Offset(size.width, 0f),
                    1.dp.toPx()
                )
            }
            .background(BgApp)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(Sharp)
                .background(BgCard)
                .border(1.dp, Primary.copy(0.20f), Sharp)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BasicTextField(
                value = inputText,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    fontFamily = SplineSans,
                    fontSize = 13.sp,
                    color = TextPrimary
                ),
                cursorBrush = SolidColor(Primary),
                decorationBox = { inner ->
                    if (inputText.isEmpty()) {
                        Text(
                            "Type a message...",
                            fontFamily = SplineSans,
                            fontSize = 13.sp,
                            color = Primary.copy(0.30f)
                        )
                    }
                    inner()
                }
            )
            Icon(
                imageVector = Icons.Outlined.EmojiEmotions,
                contentDescription = null,
                tint = Primary.copy(0.40f),
                modifier = Modifier.size(20.dp)
            )
            Text(
                "Send",
                fontFamily = SplineSans,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = (-0.5).sp,
                color = Primary
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "DOLLARS_PROTOCOL_V4.2",
                fontFamily = Mono,
                fontSize = 8.sp,
                color = Primary.copy(0.20f)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Primary.copy(pulseAlpha))
                )
                Text(
                    "Encrypted",
                    fontFamily = Mono,
                    fontSize = 8.sp,
                    color = Primary.copy(0.40f),
                    letterSpacing = (-0.3).sp
                )
            }
        }
    }
}
