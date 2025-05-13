package com.example.echojournal.ui.components.addEntryScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Schreibbereich mit Inspiration-Overlay und Wortzähler.
 */
@Composable
fun EntrySection(
    content: String,
    onContentChange: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        var textState by remember { mutableStateOf(TextFieldValue(content)) }
        var showInspiration by remember { mutableStateOf(true) }
        val inspirationText = "Deine Inspiration erscheint hier..."

        val wordCount = remember(textState.text) {
            textState.text
                .trim()                                           // führende/trailing Leerzeichen entfernen
                .split("\\s+".toRegex())                         // an einer oder mehreren Whitespaces splitten
                .filter { it.isNotBlank() }                      // leere Strings rausfiltern
                .size                                             // Größe der Liste ist die Anzahl der Wörter
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface
            )
        ) {
            TextField(
                value = textState,
                onValueChange = {
                    textState = it
                    onContentChange(it.text)
                },
                modifier = Modifier.fillMaxSize(),
                placeholder = { Text("Schreibe hier deinen Eintrag...") }
            )
            if (textState.text.isEmpty() && showInspiration) {
                Text(
                    text = inspirationText,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$wordCount Worte",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End
        )
    }
}