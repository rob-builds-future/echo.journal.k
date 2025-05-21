import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

class NewLineVisualTransformation(
    // Die gewünschte Höhe für die zusätzliche "Spacer"-Zeile.
    // Ein Wert von z.B. 8.sp oder die Hälfte der normalen Zeilenhöhe könnte gut passen.
    private val spacerLineHeight: TextUnit = 2.sp
) : VisualTransformation {
    override fun filter(annotatedString: AnnotatedString): TransformedText {
        val originalText = annotatedString.text
        val newTextBuilder = AnnotatedString.Builder()

        originalText.forEach { char ->
            if (char == '\n') {
                // Ersten (originalen) Zeilenumbruch anhängen
                newTextBuilder.append(' ')

                // Style für den "Spacer"-Absatz pushen
                // Dieser Stil gilt für den leeren Absatz, der durch das nächste '\n' erzeugt wird.
                newTextBuilder.pushStyle(ParagraphStyle(lineHeight = spacerLineHeight))
                // Zweiten (Spacer-)Zeilenumbruch anhängen
                newTextBuilder.append(' ')
                // Style wieder entfernen, damit er nur für den Spacer-Absatz gilt
                newTextBuilder.pop()
            } else {
                newTextBuilder.append(char)
            }
        }

        return TransformedText(
            newTextBuilder.toAnnotatedString(),
            // Dein OffsetMapping ist für die Transformation \n -> \n\n (zwei Zeichen) korrekt.
            // Wir können es hier wiederverwenden, da wir auch ein '\n' durch zwei '\n' ersetzen,
            // nur dass das zweite '\n' Teil eines speziell gestylten (kurzen) Absatzes ist.
            NewLineOffsetMapping(originalText)
        )
    }

    /**
     * Eine OffsetMapping-Implementierung, die der Logik deiner ursprünglichen
     * NewLineVisualTransformation folgt.
     */
    private class NewLineOffsetMapping(private val originalText: String) : OffsetMapping {

        // Helferfunktion aus deinem Code, um Newline-Zeichen zu zählen
        private fun countNewLineChars(text: String, inclUpperBound: Int): Int {
            if (inclUpperBound < 0) return 0
            // Sicherstellen, dass inclUpperBound nicht außerhalb der Grenzen von text liegt
            val endIndex = if (inclUpperBound >= text.length) text.length else inclUpperBound + 1
            return text.substring(0, endIndex).count { it == '\n' }
        }

        override fun originalToTransformed(offset: Int): Int {
            // Zählt die '\n' im Originaltext bis (exklusive) der aktuellen Cursorposition.
            // Jedes '\n' fügt ein zusätzliches Zeichen im transformierten Text hinzu.
            val additionalCharCount = countNewLineChars(originalText, offset - 1)
            return offset + additionalCharCount
        }

        override fun transformedToOriginal(offset: Int): Int {
            // Der transformierte Text hat für jedes originale '\n' ein zusätzliches '\n'.
            // (z.B. original "a\nb" -> transformiert "a\n\nb")
            // Wir müssen zählen, wie viele dieser *zusätzlichen* '\n's vor dem
            // transformierten Offset liegen.
            val conceptualTransformedText = originalText.replace("\n", "\n\n")
            // Zählt alle '\n' im (konzeptuellen) transformierten Text bis zur Position (offset -1).
            val totalNewlinesInTransformedPrefix = countNewLineChars(conceptualTransformedText, offset - 1)
            // Da jedes originale '\n' zu zwei '\n's im transformierten Text führt,
            // ist die Anzahl der *hinzugefügten* Zeichen die Hälfte der Newlines im relevanten Präfix.
            val addedCharCount = totalNewlinesInTransformedPrefix / 2
            return offset - addedCharCount
        }
    }
}