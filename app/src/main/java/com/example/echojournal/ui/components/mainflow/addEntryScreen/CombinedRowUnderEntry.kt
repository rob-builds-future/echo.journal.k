package com.example.echojournal.ui.components.mainflow.addEntryScreen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.echojournal.R

@Composable
fun CombinedRowUnderEntry(
    content: String,
    currentTemplate: String,
    onTemplateSelected: (String) -> Unit,
    templateOptions: List<String>,
    templateMenuExpanded: Boolean,
    onTemplateMenuToggle: (Boolean) -> Unit,
    onShowInstructions: () -> Unit,
    echoColor: Color,
    isLightTheme: Boolean
) {
    // Wortanzahl berechnen
    val wordCount = remember(content) {
        content
            .trim()
            .split("\\s+".toRegex())
            .filter { it.isNotBlank() }
            .size
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Aktuelle Vorlage (klickbar) + Info-Button
        Text(
            text = currentTemplate.ifBlank { "Keine Vorlage" },
            style = MaterialTheme.typography.bodyLarge.copy(color = echoColor),
            modifier = Modifier.clickable { onTemplateMenuToggle(true) }
        )
        Spacer(modifier = Modifier.width(21.dp))
        // kleiner, runder "e."-Button statt Info-Icon
        Surface(
            modifier = Modifier
                .padding(end = 8.dp)       // ein bisschen Platz rechts vom Button
                .size(32.dp),              // 32dp haben sich als kompakte Größe bewährt
            shape = androidx.compose.foundation.shape.CircleShape,
            color = echoColor,
            contentColor = Color.White
        ) {
            IconButton(
                onClick = { onShowInstructions() },
                modifier = Modifier.size(32.dp)
            ) {
                Text(
                    text = "e.",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            }
        }
        // dropdown-Menü (wird unsichtbar, sobald onDismissRequest gerufen)
        DropdownMenu(
            expanded = templateMenuExpanded,
            onDismissRequest = { onTemplateMenuToggle(false) },
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = if (isLightTheme) Color.Black else Color.LightGray,
                    shape = MaterialTheme.shapes.medium
                ),
            containerColor = if (isLightTheme) Color.White else MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium
        ) {
            templateOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onTemplateMenuToggle(false)
                        onTemplateSelected(option)
                    }
                )
            }
        }

        // Spacer dazwischen, damit Wortzähler am rechten Rand sitzt
        Spacer(modifier = Modifier.weight(1f))

        // Rechts: Wortzähler
        Text(
            text = stringResource(R.string.text_words_2, wordCount),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End
        )
    }
}