package com.example.echojournal.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.echojournal.data.local.JournalEntry

@Composable
fun EntryDetailScreen(entry: JournalEntry) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Eintrag vom ${entry.createdAt}")
        Text(entry.content, modifier = Modifier.padding(top = 8.dp))
        // …
    }
}