package com.example.echojournal.ui.components.addEntryScreen

import android.content.Context
import android.graphics.Paint.FontMetricsInt
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.LineHeightSpan
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Ein EditText mit differenziertem Zeilenabstand und optionaler automatischer Fokussierung.
 */
@Composable
fun ParagraphTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    paragraphExtraSpacing: Int = 16,
    lineHeight: Int = 20,
    autoFocus: Boolean = false
) {
    val context = LocalContext.current
    val textColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val bgColor   = MaterialTheme.colorScheme.surface.toArgb()

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            // 1) Erzeuge EditText
            val editText = createEditText(ctx, textColor, bgColor, lineHeight)

            // 2) Setze initialen Text & Selektion
            editText.setText(value.text)
            editText.setSelection(value.selection.start, value.selection.end)

            // 3) Wenn gewünscht, direkt fokussieren und Keyboard öffnen
            if (autoFocus) {
                editText.requestFocus()
                val imm = ctx.getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            }

            // 4) TextWatcher für Rückmeldung an Compose
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
                override fun afterTextChanged(s: Editable?) {}
                override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {
                    s?.let {
                        val txt = it.toString()
                        val start = editText.selectionStart.coerceIn(0, txt.length)
                        val end   = editText.selectionEnd.coerceIn(0, txt.length)
                        onValueChange(TextFieldValue(txt, TextRange(start, end)))
                    }
                }
            })

            editText
        },
        update = { editText ->
            // 5) Sync Compose → EditText
            if (editText.text.toString() != value.text) {
                editText.setText(value.text)
                editText.setSelection(
                    value.selection.start.coerceIn(0, value.text.length),
                    value.selection.end.coerceIn(0, value.text.length)
                )
            }
            // 6) Absätze-Spacing anwenden
            applyParagraphSpacingSpans(editText, paragraphExtraSpacing, lineHeight)
        }
    )
}

private fun createEditText(
    context: Context,
    textColor: Int,
    backgroundColor: Int,
    lineHeight: Int
): EditText = EditText(context).apply {
    setBackgroundColor(backgroundColor)
    setTextColor(textColor)
    isSingleLine = false
    // Soft-wrap = normaler Zeilenabstand
    setLineSpacing(0f, lineHeight / lineHeight.toFloat())
}

private fun applyParagraphSpacingSpans(
    editText: EditText,
    paragraphExtraSpacing: Int,
    lineHeight: Int
) {
    val raw = editText.text.toString()
    val ssb = SpannableStringBuilder(raw)
    ssb.getSpans(0, ssb.length, LineHeightSpan::class.java)
        .forEach { ssb.removeSpan(it) }
    var idx = raw.indexOf('\n')
    while (idx >= 0) {
        ssb.setSpan(
            CustomParagraphSpan(lineHeight, paragraphExtraSpacing),
            idx, idx + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        idx = raw.indexOf('\n', idx + 1)
    }
    editText.text = ssb
}

private class CustomParagraphSpan(
    private val lineHeight: Int,
    private val paragraphExtra: Int
) : LineHeightSpan {
    override fun chooseHeight(
        text: CharSequence,
        start: Int,
        end: Int,
        spanstartv: Int,
        v: Int,
        fm: FontMetricsInt
    ) {
        val orig = fm.descent - fm.ascent
        if (text.getOrNull(end - 1) == '\n') {
            val target = lineHeight + paragraphExtra
            val diff = target - orig
            fm.descent += diff / 2
            fm.ascent  -= diff - diff / 2
        } else {
            val diff = lineHeight - orig
            fm.descent += diff / 2
            fm.ascent  -= diff - diff / 2
        }
    }
}
