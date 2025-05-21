package com.example.echojournal.ui.components.mainflow.addEntryScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
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
    focusRequester: FocusRequester
) {
    // Wir halten den Text intern als TextFieldValue, damit Cursor-Position etc. erhalten bleiben
    var textState by remember { mutableStateOf(TextFieldValue(text = content)) }
    val wordCount = remember(textState.text) {
        textState.text
            .trim()
            .split("\\s+".toRegex())
            .filter { it.isNotBlank() }
            .size
    }
    val inspirationText = "Deine Inspiration erscheint hier…"
    var showInspiration by remember { mutableStateOf(true) }

//    // Dein Basis-TextStyle für das Eingabefeld:
//    val baseStyle = MaterialTheme.typography.bodyMedium
//        .copy(color = MaterialTheme.colorScheme.onSurface, lineHeight = 20.sp)
//
//    // Rechne dir den Absatz-Abstand aus:
//    val normalLH = baseStyle.lineHeight.value            // z.B. 20
//    val paragraphLH = (normalLH + 8f).sp                  // 20 + 8 = 28.sp
//
//    // Erzeuge einmalig deine Transformation:
//    val paragraphTransform = remember(baseStyle, paragraphLH) {
//        ParagraphLineHeightTransformation(baseStyle, paragraphLH)
//    }

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
                    if (textState.text.isEmpty() && showInspiration) {
                        Text(
                            text = inspirationText,
                            modifier = Modifier
                                .align(Alignment.TopStart),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            )
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
