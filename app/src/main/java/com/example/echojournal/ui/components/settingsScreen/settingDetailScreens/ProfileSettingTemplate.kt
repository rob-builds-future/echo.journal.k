package com.example.echojournal.ui.components.settingsScreen.settingDetailScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Screen zur Auswahl einer Journaling-Vorlage.
 * Zeigt vier Optionen: Keine Vorlage und drei Leitfäden.
 *
 * @param initialTemplateName der aktuell ausgewählte Vorlagenname
 * @param onSelect Callback mit dem Namen der gewählten Vorlage
 */

@Composable
fun ProfileSettingTemplate(
    initialTemplateName: String,
    onSelect: (String) -> Unit,
) {
    // Vorlagen-Optionen mit kurzem Vorschautext
    val noneOption = "Keine Vorlage" to "Starte mit leerer Seite."
    val morningOptions = listOf(
        "Produktiver Morgen" to "Was ist mein Hauptziel heute? Drei Prioritäten? Ablenkungen?",
        "Ziele im Blick" to "Welches Langzeit-Ziel verfolge ich? Was habe ich heute dafür getan?"
    )
    val eveningOptions = listOf(
        "Reflexion am Abend" to "Was habe ich heute erlebt? Wie fühle ich mich? Wofür bin ich dankbar?",
        "Dankbarkeits-Check" to "Nenne drei Dinge, für die du heute dankbar bist."
    )

    var selected by remember { mutableStateOf(initialTemplateName) }
    val onSelectLocal: (String) -> Unit = { name ->
        selected = name
        onSelect(name)
    }

    Column {
        // Keine Vorlage
        TemplateRow(noneOption, selected, onSelectLocal)
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Morgensession
        Text(
            text = "Am Morgen",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        morningOptions.forEach { TemplateRow(it, selected, onSelectLocal) }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Abendsession
        Text(
            text = "Am Abend",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        eveningOptions.forEach { TemplateRow(it, selected, onSelectLocal) }
    }
}

@Composable
private fun TemplateRow(
    option: Pair<String, String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    val (name, preview) = option
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onSelect(name) }
            .padding(vertical = 8.dp)
    ) {
        RadioButton(
            selected = (name == selected),
            onClick = { onSelect(name) }
        )
        Spacer(modifier = Modifier.padding(horizontal = 12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = preview,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}