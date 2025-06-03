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
 * Zeigt eine Beispiel-Aufschlüsselung der Einträge pro Monat.
 * In einer vollständigen Implementierung würdest du hier z.B. ein Diagramm oder eine dynamische Liste ausgeben.
 */
@Composable
fun EntriesByMonthStatistic() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Einträge nach Monat",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Platzhalter-Text – ggf. später durch Chart oder LazyColumn ersetzen
            Text(
                text = "Januar: 10\nFebruar: 8\nMärz: 12",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
