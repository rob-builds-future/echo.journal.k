package com.example.echojournal.ui.components.mainflow.addEntryScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StyledParagraphTextField() {
    var text by remember { mutableStateOf("") }

    // hier muss das buildAnnotatedString selbst das letzte (und einzige) Statement sein:
    val styledText = remember(text) {
        buildAnnotatedString {
            val paras = text.split('\n')
            paras.forEachIndexed { idx, para ->
                // den Absatztext anhängen
                val startPara = length
                append(para)
                val endPara = length
                addStyle(
                    ParagraphStyle(lineHeight = 20.sp),
                    startPara, endPara
                )

                if (idx < paras.lastIndex) {
                    // erste leere Zeile auf 0 sp zusammenklappen
                    append('\n')
                    addStyle(
                        ParagraphStyle(lineHeight = 0.sp),
                        length - 1, length
                    )
                    // zweite leere Zeile als Spacer
                    append('\n')
                    addStyle(
                        ParagraphStyle(lineHeight = 28.sp),
                        length - 1, length
                    )
                }
            }
        } // ← AnnotatedString wird hier zurückgegeben
    }

    BasicTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        textStyle = TextStyle(fontSize = 16.sp),
        visualTransformation = VisualTransformation.None,
        decorationBox = { innerTextField ->
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(Modifier.padding(12.dp)) {
                    if (text.isEmpty()) {
                        Text(
                            "Type here…",
                            style = TextStyle(color = Color.Gray, fontSize = 16.sp)
                        )
                    }
                    // hier wird der Text mit Deinen Styles gezeichnet
                    innerTextField()
                    Text(styledText, style = TextStyle(fontSize = 16.sp))
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewStyledParagraphTextField() {
    StyledParagraphTextField()
}