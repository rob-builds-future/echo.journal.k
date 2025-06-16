package com.example.echojournal.ui.components.mainflow.addEntryScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.echojournal.R

@Composable
fun TranslationSection(
    translationText: String,
    echoColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        // Splitting text on manual line breaks to preserve paragraph spacing
        val lines = if (translationText.isEmpty()) listOf(stringResource(R.string.text_translating))
        else translationText.split("\n")
        lines.forEachIndexed { idx, line ->
            Text(
                text = line,
//                style = MaterialTheme.typography.bodyMedium.copy(
////                    fontWeight = FontWeight.Bold
////                ),
                fontWeight = FontWeight.Bold,
                color = echoColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
            if (idx < lines.lastIndex) {
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}