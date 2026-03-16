package com.example.vndbapp.presentation.screens.vncharacters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vndbapp.data.model.VNCharacter

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
                    "ROLE" to character.roleForVn(vnId),
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
