package com.example.vndbapp.presentation.screens.vndetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vndbapp.data.mapper.stripBBCode
import com.example.vndbapp.data.model.VisualNovel

@Composable
fun DescriptionScreen(visualNovel: VisualNovel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
            .verticalScroll(state = rememberScrollState())
    ) {
        Text(text = visualNovel.description.stripBBCode())
    }
}
