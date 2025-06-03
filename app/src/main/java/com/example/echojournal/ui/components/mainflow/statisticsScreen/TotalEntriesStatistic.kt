package com.example.echojournal.ui.components.mainflow.statisticsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Zeigt die Gesamtanzahl an Journal-Einträgen an.
 * In einer echten App würdest du hier den Wert aus dem ViewModel ziehen.
 */
@Composable
fun TotalEntriesStatistic() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Gesamtanzahl Einträge",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Hier Platzhalter-Text – später den echten Wert aus dem ViewModel laden
            Text(
                text = "42",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
