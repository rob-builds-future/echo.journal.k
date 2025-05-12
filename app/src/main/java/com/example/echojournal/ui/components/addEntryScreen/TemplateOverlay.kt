package com.example.echojournal.ui.components.addEntryScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Overlay, das vor dem Journal-Eintrag eingeblendet wird.
 * Zeigt den gewählten Vorlagentitel, Vorschautext und Aktionen an.
 *
 * @param templateName Name der ausgewählten Vorlage
 * @param templateText Vorschau-Text der Vorlage
 * @param onDismiss Schließt das Overlay und startet den Eintrag
 * @param onChange Vorlage ändern (öffnet Vorlagenauswahl)
 */
@Composable
fun TemplateOverlay(
    templateName: String,
    templateText: String,
    onDismiss: () -> Unit,
    onChange: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = templateName,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = templateText,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onChange) {
                        Text("Vorlage ändern")
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    Button(onClick = onDismiss) {
                        Text("Loslegen")
                    }
                }
            }
        }
    }
}
