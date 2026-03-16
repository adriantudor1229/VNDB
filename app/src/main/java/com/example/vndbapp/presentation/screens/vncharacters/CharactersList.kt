package com.example.vndbapp.presentation.screens.vncharacters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vndbapp.data.model.CharacterRole
import com.example.vndbapp.data.model.Resource
import com.example.vndbapp.data.model.VNCharacter

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
                val charList =
                    characters.data.filter { it -> it.vns.find { it.id == vnId }?.role != CharacterRole.MAIN }

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
