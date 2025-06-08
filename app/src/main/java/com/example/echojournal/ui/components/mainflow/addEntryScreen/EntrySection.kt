package com.example.echojournal.ui.components.mainflow.addEntryScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.echojournal.R
import com.example.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun EntrySection(
    content: String,
    onContentChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    // PrefsViewModel holen, aktuellen Template-Namen beobachten
    val prefsViewModel: PrefsViewModel = koinViewModel()
    val currentTemplate by prefsViewModel.currentTemplate.collectAsState()

    // TextState intern als TextFieldValue halten (damit Cursor-Position o.Ä. erhalten bleiben)
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = content))
    }

    // Wir holen uns hier die lokalisierten Template-Labels:
    val noneLabel = stringResource(R.string.template_none)
    val morningLabel = stringResource(R.string.template_productive_morning)
    val goalsLabel = stringResource(R.string.template_goals)
    val eveningLabel = stringResource(R.string.template_evening_reflection)
    val gratitudeLabel = stringResource(R.string.template_gratitude)

    // Je nach aktuellem Template-Text den Placeholder-Res auswählen
    val placeholderRes = when (currentTemplate) {
        morningLabel   -> R.string.placeholder_productive_morning
        goalsLabel     -> R.string.placeholder_goals
        eveningLabel   -> R.string.placeholder_evening_reflection
        gratitudeLabel -> R.string.placeholder_gratitude
        else           -> R.string.placeholder_none
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            CustomTextEditor(
                value = textState,
                onValueChange = {
                    textState = it
                    onContentChange(it.text)
                },
                modifier = Modifier.fillMaxSize(),
                focusRequester = focusRequester,
                backgroundColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                contentPadding = PaddingValues(8.dp), // Innen-Padding
                borderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                borderWidth = 1.dp,
                shape = RoundedCornerShape(8.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                placeholder = {
                    if (textState.text.isEmpty()) {
                        Text(
                            text = stringResource(placeholderRes),
                            modifier = Modifier.align(Alignment.TopStart),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            )
        }
    }
}
