package com.example.echojournal.util

import java.util.Locale

/**
 * Hilfsobjekt, das einen LibreTranslate-Code (z.B. "en", "de", "pb" usw.)
 * in ein Android-kompatibles BCP-47-Tag umwandelt.
 *
 * Die Liste der Libre-Codes kommt zur Laufzeit aus der API.
 * Hier definieren wir nur noch die **Ausnahmen**, bei denen
 * "<code>-<CODE>".uppercase() nicht korrekt wäre.
 */
object LanguageUtil {

    /**
     * Wandelt einen LibreTranslate-Code in BCP-47-Tag um.
     *
     * - Wenn code eine bekannte Ausnahme ist (z.B. "pb", "ja"), greift die manuelle Regel.
     * - Wenn code z.B. "de", "es", "fr" etc. ist, wird daraus "<de>-<DE>", "<es>-<ES>" usw.
     * - Liefert code zurück, falls er schon ein valides Tag wäre (z.B. "en-GB", "pt-BR").
     */
    fun mapLibreToBcp47(libreCode: String): String {
        val lc = libreCode.lowercase(Locale.ROOT)

        return when (lc) {
            // Manuelle Ausnahmen:
            "pb" -> "pt-BR"   // LibreTranslate: "pb" = Português (Brasil)
            "pt" -> "pt-PT"   // Einfache "pt" (ohne Region) → "pt-PT"
            "en" -> "en-US"   // Standard-Englisch → US-Variant
            "ja" -> "ja-JP"   // Japanisch
            "zh" -> "zh-CN"   // Chinesisch → China
            "hi" -> "hi-IN"   // Hindi → Indien
            // … du kannst hier weitere Ausnahmen ergänzen, sobald du sie brauchst …

            // Wenn es schon ein BCP-47-Tag ist (z.B. "de-DE", "en-GB"), dann direkt zurückgeben:
            in Regex("^[a-z]{2}-[A-Z]{2}\$").find(lc)?.value ?: "" -> lc

            // Standardfall: code ist 2-Stellen-ISO (z.B. "de", "es", "fr" …),
            // wir hängen "<code>-<CODE>" dran und bekommen etwa "de-DE", "es-ES", "fr-FR" usw.
            else -> "$lc-${lc.uppercase(Locale.ROOT)}"
        }
    }

    /**
     * Hilfsfunktion: Aus dem BCP-47-Tag den tatsächlichen Locale-Objekt erstellen.
     * Beispiel: "pt-BR" → Locale("pt","BR"), "de-DE" → Locale.GERMANY, …
     */
    fun toLocale(bcp47Tag: String): Locale = Locale.forLanguageTag(bcp47Tag)
}
