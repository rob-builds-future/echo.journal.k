package com.rbf.echojournal.util

//PorterStemmer zur Neutralisierung der Worte für TopWordStatistic
class PorterStemmerEn {
    private var b = CharArray(50)       // buffer for word to be stemmed
    private var i = 0                   // offset into b
    private var j = 0                   // a general offset into b

    fun stem(word: String): String {
        // Resette Puffer
        b = CharArray(word.length + 2)
        i = 0

        // Fülle Puffer mit den Buchstaben
        for (ch in word) add(ch)
        // Führe den Stemmer aus
        stem()
        // Rückgabe des gestemmten Worts
        return String(b, 0, i)
    }

    private fun add(ch: Char) {
        if (i == b.size) {
            b = b.copyOf(b.size + 20)
        }
        b[i++] = ch
    }

    private fun stem() {
        if (i > 1) {
            // Schritt 1a
            if (endsWith("sses")) {
                i -= 2
            } else if (endsWith("ies")) {
                setTo("i")
            } else if (endsWith("ss")) {
                // nothing
            } else if (endsWith("s")) {
                i--
            }

            // Schritt 1b
            if (endsWith("eed")) {
                if (measure() > 0) i--
            } else if ((endsWith("ed") && vowelInStem("ed")) ||
                (endsWith("ing") && vowelInStem("ing"))) {
                i = j
                when {
                    endsWith("at") -> setTo("ate")
                    endsWith("bl") -> setTo("ble")
                    endsWith("iz") -> setTo("ize")
                    doubleConsonant(i - 1) -> {
                        val ch = b[i - 1]
                        if (ch != 'l' && ch != 's' && ch != 'z') i--
                    }
                    measure() == 1 && cvc(i - 1) -> setTo("e")
                }
            }

            // Schritt 1c
            if (endsWith("y") && vowelInStem("y")) {
                b[i - 1] = 'i'
            }

            // Schritt 2 und 3 und 4 können bei Bedarf noch ergänzt werden…
        }
    }

    private fun endsWith(s: String): Boolean {
        val length = s.length
        val o = i - length
        if (o < 0) return false
        for (k in s.indices) {
            if (b[o + k] != s[k]) return false
        }
        j = i
        i = o
        return true
    }

    private fun setTo(s: String) {
        val length = s.length
        val o = j + length
        for (k in s.indices) {
            b[j + k] = s[k]
        }
        i = o
    }

    private fun vowelInStem(suffix: String): Boolean {
        val length = suffix.length
        val o = i - length
        for (k in 0 until o) {
            if (!isConsonant(b[k])) return true
        }
        return false
    }

    private fun measure(): Int {
        var n = 0
        var i = 0
        val length = this.i
        while (true) {
            if (i >= length) return n
            if (!isConsonant(b[i])) break
            i++
        }
        i++
        while (true) {
            while (true) {
                if (i >= length) return n
                if (isConsonant(b[i])) break
                i++
            }
            i++
            n++
            while (true) {
                if (i >= length) return n
                if (!isConsonant(b[i])) break
                i++
            }
            i++
        }
    }

    private fun isConsonant(ch: Char): Boolean {
        return when (ch) {
            'a', 'e', 'i', 'o', 'u' -> false
            'y' -> false
            else -> true
        }
    }

    private fun doubleConsonant(i: Int): Boolean {
        if (i < 1) return false
        return b[i] == b[i - 1] && isConsonant(b[i])
    }

    private fun cvc(i: Int): Boolean {
        if (i < 2) return false
        if (!isConsonant(b[i]) || isConsonant(b[i - 1]) || !isConsonant(b[i - 2])) return false
        val ch = b[i]
        return ch != 'w' && ch != 'x' && ch != 'y'
    }
}
