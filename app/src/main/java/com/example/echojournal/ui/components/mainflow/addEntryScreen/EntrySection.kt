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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
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

    // TextState intern als TextFieldValue halten (damit Cursor‐Position etc. erhalten bleiben)
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = content))
    }

    // Je nach gewähltem Template den Placeholder‐Text setzen
    val placeholderText = when (currentTemplate) {
        // Hier Texte nach Bedarf anpassen / übersetzen
        "Produktiver Morgen" -> "Was ist dein Hauptziel heute? Welche drei Prioritäten stehen ganz oben auf deiner Liste?"
        "Ziele im Blick"    -> "Welches langfristige Ziel verfolgst du gerade? Was hast du heute dafür getan?"
        "Reflexion am Abend"-> "Welche Erlebnisse haben dich heute besonders bewegt? Wie fühlst du dich gerade?"
        "Dankbarkeits-Check"-> "Nenne drei Dinge, für die du heute dankbar bist."
        // wenn keine Vorlage ausgewählt, kannst du einen neutralen Hinweis geben:
        else -> "Beginne hier zu schreiben…"
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
                modifier = Modifier
                    .fillMaxSize(),
                focusRequester = focusRequester,
                backgroundColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                contentPadding = PaddingValues(8.dp), // Innen‐Padding
                borderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                borderWidth = 1.dp,
                shape = RoundedCornerShape(8.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                //visualTransformation = paragraphTransform,
                placeholder = {
                    if (textState.text.isEmpty()) {
                        if (textState.text.isEmpty()) {
                            Text(
                                text = placeholderText,
                                modifier = Modifier.align(Alignment.TopStart),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            )
                        }
                    }
                }
            )
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = "$wordCount Worte",
//                modifier = Modifier.fillMaxWidth(),
//                style = MaterialTheme.typography.bodySmall,
//                textAlign = TextAlign.End
//            )
        }
    }
}
