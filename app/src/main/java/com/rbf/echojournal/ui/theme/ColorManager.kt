package com.rbf.echojournal.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.rbf.echojournal.R

object ColorManager {
    // Map von Theme-Namen zu Resource IDs
    private val themes: Map<String, Int> = mapOf(
        "Smaragd" to R.color.Smaragdgrün,
        "Wolkenlos" to R.color.Lichtblau,
        "Vintage" to R.color.Vintagepurpur,
        "Koralle" to R.color.Korallorange,
        "Bernstein" to R.color.Bernsteingelb
    )

    /**
     * Liefert die Compose-Color für den gegebenen Theme-Namen.
     * Muss in einem @Composable-Kontext aufgerufen werden.
     */
    @Composable
    fun getColor(name: String): Color {
        val resId = themes[name] ?: themes.values.first()
        return colorResource(id = resId)
    }
}