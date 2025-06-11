package com.example.echojournal.ui.components.onboardingflow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.echojournal.data.remote.model.util.LanguageDto

@Composable
fun LanguagePickerListOnboarding(
    languages: List<LanguageDto>,
    selectedCode: String,
    onSelect: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Sprache suchen") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // Nur gefilterte anzeigen
        val filtered = languages.filter { it.name.contains(query, ignoreCase = true) }
        LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
            items(filtered, key = { it.code }) { lang ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(lang.code) }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = lang.code == selectedCode,
                        onClick = { onSelect(lang.code) }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(lang.name)
                }
            }
        }
    }
}
