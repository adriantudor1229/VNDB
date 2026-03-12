package com.example.vndbapp.presentation.screens.vndetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vndbapp.data.mapper.stripBBCode
import com.example.vndbapp.data.model.VisualNovel
import com.example.vndbapp.presentation.components.ExpandableText
import com.example.vndbapp.presentation.components.SectionHeader
import com.example.vndbapp.ui.theme.BgCard
import com.example.vndbapp.ui.theme.Primary
import com.example.vndbapp.ui.theme.TextSecondary

@Composable
fun VNDescriptionSection(vn: VisualNovel) {

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {

        SectionHeader("DESCRIPTION")

        Spacer(Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RadiusXl)
                .background(BgCard)
                .border(1.dp, Primary.copy(0.10f), RadiusXl)
                .padding(16.dp)
        ) {

            ExpandableText(
                text = vn.description.stripBBCode(),
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}
