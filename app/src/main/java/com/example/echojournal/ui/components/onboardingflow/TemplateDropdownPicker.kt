package com.example.echojournal.ui.components.onboardingflow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.echojournal.R

@Composable
fun TemplatePickerOnboarding(
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier
) {
    // Die Optionen übernehmen wir wie im Settings-Screen
    val noneOption = R.string.template_none to R.string.preview_none
    val morningOptions = listOf(
        R.string.template_productive_morning to R.string.preview_morning_productive,
        R.string.template_goals to R.string.preview_morning_goals
    )
    val eveningOptions = listOf(
        R.string.template_evening_reflection to R.string.preview_evening_reflection,
        R.string.template_gratitude to R.string.preview_evening_gratitude
    )

    Column {
        TemplateRowOnboarding(noneOption, selected, onSelect)
        Spacer(Modifier.height(8.dp))
        Text(text = "Morgens")
        morningOptions.forEach { TemplateRowOnboarding(it, selected, onSelect) }
        Spacer(Modifier.height(8.dp))
        Text(text = "Abends")
        eveningOptions.forEach { TemplateRowOnboarding(it, selected, onSelect) }
    }
}

@Composable
private fun TemplateRowOnboarding(
    option: Pair<Int,Int>,
    selected: String,
    onSelect: (String) -> Unit
) {
    val (nameRes, previewRes) = option
    val name    = stringResource(nameRes)
    val preview = stringResource(previewRes)

    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(name) }
            .padding(vertical = 8.dp)
    ) {
        RadioButton(
            selected = name == selected,
            onClick  = { onSelect(name) }
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name)
            Spacer(Modifier.height(4.dp))
            Text(
                text = preview,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
