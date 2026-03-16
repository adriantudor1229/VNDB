package com.example.vndbapp.presentation.screens.vncharacters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun ProtagonistDossier(character: VNCharacter?, vnId: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .terminalBorder()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

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

            AsyncImage(
                model = character.image?.url,
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(128.dp)
                    .terminalBorder()
                    .padding(4.dp)
            )

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
