package com.example.echojournal.ui.screens.mainflow

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.echojournal.data.remote.model.JournalEntry

@Composable
fun EntryDetailScreen(entry: JournalEntry) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Eintrag vom ${entry.createdAt}")
                Text(entry.content, modifier = Modifier.padding(top = 8.dp))
                // …
            }
        }
    }
}