package com.rbf.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rbf.echojournal.R
import com.rbf.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileSettingTemplate() {
    val prefs: PrefsViewModel = koinViewModel()
    val current by prefs.currentTemplate.collectAsState()

    var selected by remember { mutableStateOf(current) }
    LaunchedEffect(current) { selected = current }

    val onSelect: (String) -> Unit = { name ->
        selected = name
        prefs.setTemplate(name)
    }

    // Optionen als Pair<nameRes, previewRes>
    val noneOption = R.string.template_none to R.string.preview_none
    val morningOptions = listOf(
        R.string.template_productive_morning to R.string.preview_morning_productive,
        R.string.template_goals to R.string.preview_morning_goals
    )
    val eveningOptions = listOf(
        R.string.template_evening_reflection to R.string.preview_evening_reflection,
        R.string.template_gratitude to R.string.preview_evening_gratitude
    )

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // Keine Vorlage
        TemplateRow(noneOption, selected, onSelect)
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Morgens
        Text(
            text = stringResource(R.string.template_section_morning),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        morningOptions.forEach { TemplateRow(it, selected, onSelect) }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Abends
        Text(
            text = stringResource(R.string.template_section_evening),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        eveningOptions.forEach { TemplateRow(it, selected, onSelect) }
    }
}

@Composable
private fun TemplateRow(
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
