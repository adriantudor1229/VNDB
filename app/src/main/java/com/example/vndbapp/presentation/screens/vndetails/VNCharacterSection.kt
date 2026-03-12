package com.example.vndbapp.presentation.screens.vndetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vndbapp.data.model.Resource
import com.example.vndbapp.data.model.VNCharacter
import com.example.vndbapp.presentation.components.CharacterAvatar
import com.example.vndbapp.presentation.components.SectionHeader
import com.example.vndbapp.ui.theme.Primary
import com.example.vndbapp.ui.theme.TextMuted

@Composable
fun VNCharactersSection(characters: Resource<List<VNCharacter>>) {

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {

        SectionHeader("CHARACTERS")

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
                        strokeWidth = 2.dp
                    )
                }
            }

            is Resource.Success -> {

                val charList = characters.data

                if (charList.isEmpty()) {

                    Text(
                        "[ NO CHARACTERS FOUND ]",
                        fontFamily = Mono,
                        fontSize = 10.sp,
                        color = TextMuted
                    )

                } else {

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {

                        items(charList) { character ->

                            CharacterAvatar(
                                name = character.name,
                                imageUrl = character.image?.url ?: ""
                            )

                        }
                    }
                }
            }

            is Resource.Error -> {

                Text(
                    "[ ${characters.message} ]",
                    fontFamily = Mono,
                    fontSize = 10.sp,
                    color = Primary.copy(0.5f)
                )
            }
        }
    }
}
