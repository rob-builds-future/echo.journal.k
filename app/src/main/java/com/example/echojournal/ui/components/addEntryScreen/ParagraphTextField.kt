//package com.example.echojournal.ui.components.addEntryScreen
//
//import android.content.Context
//import android.text.Spannable
//import android.text.SpannableStringBuilder
//import android.text.style.LineHeightSpan
//import android.widget.EditText
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Paint
//import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.viewinterop.AndroidView
//
///**
// * Ein EditText, das bei hartem Umbruch (\n) einen größeren
// * Abstand einfügt, bei softwraps den normalen Zeilenabstand nutzt.
// */
//@Composable
//fun ParagraphTextField(
//    value: TextFieldValue,
//    onValueChange: (TextFieldValue) -> Unit,
//    modifier: Modifier = Modifier,
//    /** extra Abstand (in px) nach Absätzen */
//    paragraphExtraSpacing: Int = 16,
//    /** Zeilenhöhe bei weichen Umbrüchen (in px) */
//    lineHeight: Int = 20,
//    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
//        color = MaterialTheme.colorScheme.onSurface
//    )
//) {
//    val context = LocalContext.current
//
//    // Wir müssen das EditText-Objekt zwischen Rekomposings stabil halten:
//    AndroidView(factory = { createEditText(context) }, update = { editText ->
//        // Setze Text, falls sich String geändert hat:
//        if (editText.text.toString() != value.text) {
//            editText.setText(value.text)
//            editText.setSelection(value.selection.start, value.selection.end)
//        }
//        // Anwenden unserer custom Spans:
//        applyParagraphSpacingSpans(
//            editText,
//            paragraphExtraSpacing,
//            lineHeight
//        )
//    }, modifier = modifier.fillMaxSize())
//
//    // Den Compose‐State synchron halten:
//    LaunchedEffect(Unit) {
//        // beim ersten Aufbau einmal TextWatcher anhängen
//        // und in diesem onValueChange aufrufen
//    }
//}
//
//private fun createEditText(context: Context): EditText {
//    return EditText(context).apply {
//        // multiline, keine Autokorrektur-Overlays etc.
//        setBackgroundColor(android.graphics.Color.TRANSPARENT)
//        setTextColor(MaterialTheme.colorScheme.onSurface.toArgb())
//        isSingleLine = false
//        // initialer Zeilenabstand für *Soft*-Wrap
//        setLineSpacing(0f, /*multiplier=*/1f)
//    }
//}
//
///**
// * Fügt jedem harten Umbruch (\n) einen eigenen LineHeightSpan hinzu,
// * der den Absatz-Abstand vergrößert.
// */
//private fun applyParagraphSpacingSpans(
//    editText: EditText,
//    paragraphExtraSpacing: Int,
//    lineHeight: Int
//) {
//    val raw = editText.text.toString()
//    val ssb = SpannableStringBuilder(raw)
//
//    // entferne alte Spans
//    ssb.getSpans(0, ssb.length, LineHeightSpan::class.java)
//        .forEach { ssb.removeSpan(it) }
//
//    // für jeden Absatz einen eigenen Span
//    var index = raw.indexOf('\n')
//    while (index >= 0) {
//        // Span ab dem Newline-Zeichen auf genau diese Zeile anwenden
//        ssb.setSpan(
//            CustomParagraphSpan(lineHeight, paragraphExtraSpacing),
//            index, index + 1,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        index = raw.indexOf('\n', index + 1)
//    }
//
//    editText.setText(ssb)
//}
//
///**
// * Unser eigener LineHeightSpan: normaler Zeilenaufbau,
// * aber nach Absatz das lineHeight+extraSpacing verwenden.
// */
//private class CustomParagraphSpan(
//    private val lineHeight: Int,
//    private val paragraphExtra: Int
//) : LineHeightSpan {
//    override fun chooseHeight(
//        text: CharSequence,
//        start: Int,
//        end: Int,
//        spanstartv: Int,
//        v: Int,
//        fm: Paint.FontMetricsInt
//    ) {
//        // Wenn das aktuelle Zeichen der Newline ist, vergrößere descent um extraSpacing:
//        if (text.getOrNull(end - 1) == '\n') {
//            // setze die Gesamthöhe auf lineHeight + extra
//            val originalHeight = fm.descent - fm.ascent
//            val target = lineHeight + paragraphExtra
//            val diff = target - originalHeight
//            // Wir verteilen den zusätzlichen Raum auf ascent und descent:
//            fm.descent += diff / 2
//            fm.ascent -= diff - diff / 2
//        } else {
//            // normaler Zeilenabstand
//            val originalHeight = fm.descent - fm.ascent
//            val diff = lineHeight - originalHeight
//            fm.descent += diff / 2
//            fm.ascent -= diff - diff / 2
//        }
//    }
//}