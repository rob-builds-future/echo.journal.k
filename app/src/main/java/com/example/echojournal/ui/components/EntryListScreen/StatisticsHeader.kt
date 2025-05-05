package com.example.echojournal.ui.components.EntryListScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatisticsHeaderPlaceholder() {
    // Platzhalter für späteren StatisticsHeader
    Box(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text("Statistics Header")
    }
}