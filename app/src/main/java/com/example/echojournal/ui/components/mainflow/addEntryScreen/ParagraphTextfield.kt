package com.example.echojournal.ui.components.mainflow.addEntryScreen
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun ParagraphTextField() {
    var text by remember { mutableStateOf("") }

    // 20sp for normal soft wraps, 12sp for hard breaks
    val normalLineHeight = 20.sp
    val manualBreakHeight = 12.sp

    val transformer = VisualTransformation { textInput ->
        val orig = textInput.text

        val styled = buildAnnotatedString {
            // 1) Put all the text in
            append(orig)

            // 2) Style EVERY char at 12sp (including the '\n's)
            addStyle(
                ParagraphStyle(lineHeight = manualBreakHeight),
                start = 0,
                end   = length
            )

            // 3) Now walk through orig, and wherever it's NOT '\n', style that span at 20sp
            var index = 0
            orig.forEach { c ->
                if (c != '\n') {
                    // find the run of non-newline chars
                    val runStart = index
                    // consume until the next '\n' (or end)
                    while (index < orig.length && orig[index] != '\n') {
                        index++
                    }
                    addStyle(
                        ParagraphStyle(lineHeight = normalLineHeight),
                        start = runStart,
                        end   = index
                    )
                }
                // advance past the '\n'
                index++
            }
        }

        TransformedText(styled, OffsetMapping.Identity)
    }

    OutlinedTextField(
        value               = text,
        onValueChange       = { text = it },
        modifier            = Modifier.fillMaxWidth(),
        visualTransformation = transformer,
        textStyle           = TextStyle(fontSize = 16.sp),
        singleLine          = false,
        maxLines            = Int.MAX_VALUE,
        placeholder         = { /* … */ }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewParagraphTextField() {
    ParagraphTextField()
}
