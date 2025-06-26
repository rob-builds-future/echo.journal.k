package com.rbf.echojournal.ui.components.onboardingflow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rbf.echojournal.R
import com.rbf.echojournal.ui.theme.ColorManager

@Composable
fun ThemePickerOnboarding(
    options: List<String>,
    selectedTheme: String,
    onSelect: (String) -> Unit,
    modifier: Modifier
) {
    // Map für Farbnamen zu String-Resources
    val displayMap = mapOf(
        "Smaragd" to R.string.theme_display_emerald,
        "Wolkenlos" to R.string.theme_display_cloudless,
        "Vintage" to R.string.theme_display_vintage,
        "Koralle" to R.string.theme_display_coral,
        "Bernstein" to R.string.theme_display_amber
    )

    Column(modifier = modifier) {
        options.forEach { key ->
            val color = ColorManager.getColor(key)
            val label = stringResource(displayMap[key] ?: R.string.theme_display_cloudless)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(key) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(color = color, shape = RoundedCornerShape(6.dp))
                )
                Spacer(Modifier.width(12.dp))
                Text(text = label)
                Spacer(Modifier.weight(1f))
                RadioButton(
                    selected = key == selectedTheme,
                    onClick = { onSelect(key) }
                )
            }
        }
    }
}
