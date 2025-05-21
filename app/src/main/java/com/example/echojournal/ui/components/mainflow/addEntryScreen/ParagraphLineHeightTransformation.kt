// ParagraphLineHeightTransformation.kt
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit

class ParagraphLineHeightTransformation(
    private val baseTextStyle: TextStyle,
    private val paragraphLineHeight: TextUnit
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        // Baue ein AnnotatedString, das auf jedes '\n' einen ParagraphStyle setzt:
        val styled = buildAnnotatedString {
            raw.split('\n').forEachIndexed { idx, segment ->
                // 1) eigentlicher Text
                withStyle(SpanStyle(fontSize = baseTextStyle.fontSize, color = baseTextStyle.color)) {
                    append(segment)
                }
                // 2) wenn’s kein letzter Segment ist, an '\n' hängen und style draufpacken
                if (idx < raw.lineSequence().count() - 1) {
                    append('\n')
                    val start = length - 1
                    addStyle(ParagraphStyle(lineHeight = paragraphLineHeight), start, start + 1)
                }
            }
        }

        // Simpler OffsetMapping: hier 1:1 (keine zusätzliche chars eingefügt)
        val offset = object : OffsetMapping {
            override fun originalToTransformed(offset: Int) = offset
            override fun transformedToOriginal(offset: Int) = offset
        }

        return TransformedText(styled, offset)
    }
}
