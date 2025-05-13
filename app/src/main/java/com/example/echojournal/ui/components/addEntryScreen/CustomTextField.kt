package com.example.echojournal.ui.components.addEntryScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Ein vollständig anpassbares Text-Input-Feld basierend auf BasicTextField.
 *
 * @param value Der aktuelle Textzustand als TextFieldValue
 * @param onValueChange Callback, wenn sich der Text ändert
 * @param modifier Modifier für äußere Anpassungen (z.B. Größe)
 * @param backgroundColor Hintergrundfarbe des Felds
 * @param cursorColor Farbe des Textcursors
 * @param contentPadding Padding zwischen Feldrand und Textinhalt
 * @param borderColor Rahmenfarbe
 * @param borderWidth Rahmenstärke
 * @param shape Form des Rahmens/Ecken
 * @param textStyle Textstil für die Eingabe
 * @param placeholder Optionaler Composable-Content, der angezeigt wird, wenn value.text leer ist
 */
@Composable
fun CustomTextEditor(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    backgroundColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surface,
    cursorColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    contentPadding: PaddingValues = PaddingValues(8.dp),
    borderColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
    borderWidth: Dp = 1.dp,
    shape: Shape = MaterialTheme.shapes.small,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
    placeholder: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(shape)
            .background(backgroundColor)
            .border(borderWidth, borderColor, shape)
            .padding(contentPadding)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            cursorBrush = SolidColor(cursorColor),
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester),
        ) { inner ->
            if (value.text.isEmpty() && placeholder != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopStart
                ) {
                    placeholder()
                }
            }
            inner()
        }
    }
}

