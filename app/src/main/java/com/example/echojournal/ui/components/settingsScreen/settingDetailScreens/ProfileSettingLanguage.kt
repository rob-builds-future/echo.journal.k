package com.example.echojournal.ui.components.settingsScreen.settingDetailScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Screen zur Auswahl der Zielsprache.
 *
 * @param initialLanguage bisher ausgewählte Sprache-Code oder Name
 * @param onSelect Callback mit der ausgewählten Sprache (Name oder Code)
 */
@Composable
fun ProfileSettingLanguage(
    initialLanguage: String,
    onSelect: (String) -> Unit
) {
    // Statische Liste unterstützter Sprachen von LibreTranslate
    val languages = listOf(
        "Basque", "Bulgarian", "Catalan", "Chinese", "Czech", "Dutch",
        "English", "Esperanto", "French", "German", "Hungarian", "Irish",
        "Italian", "Japanese", "Kabyle", "Korean", "Portuguese", "Russian",
        "Scottish Gaelic", "Spanish", "Ukrainian"
    )

    var query by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf(initialLanguage) }

    Column {
        Text("Wähle deine Zielsprache aus.")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text("Suche Sprache") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedLabelColor = Color.White.copy(alpha = 0.8f),
                unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                disabledLabelColor = Color.White.copy(alpha = 0.4f),
                errorLabelColor = Color.White.copy(alpha = 0.8f),
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                disabledTextColor = Color.White,
                errorTextColor = Color.Red,
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Black,
                disabledContainerColor = Color.Black,
                errorContainerColor = Color.Black,
                focusedPlaceholderColor = Color.White,
                unfocusedPlaceholderColor = Color.White,
                disabledPlaceholderColor = Color.White,
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(languages.filter {
                it.contains(query, ignoreCase = true)
            }) { lang ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selected = lang
                            onSelect(lang)
                        }
                        .padding(vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (lang == selected),
                            onClick = {
                                selected = lang
                                onSelect(lang)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = lang)
                    }
                }
            }
        }
    }
}
