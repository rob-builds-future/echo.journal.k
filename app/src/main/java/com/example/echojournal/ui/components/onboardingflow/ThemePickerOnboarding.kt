package com.example.echojournal.ui.components.onboardingflow

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
import androidx.compose.ui.unit.dp
import com.example.echojournal.ui.theme.ColorManager

@Composable
fun ThemePickerOnboarding(
    options: List<String>,
    selectedTheme: String,
    onSelect: (String) -> Unit,
    modifier: Modifier
) {
    Column {
        options.forEach { key ->
            val color = ColorManager.getColor(key)
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
                Text(text = key)
                Spacer(Modifier.weight(1f))
                RadioButton(
                    selected = key == selectedTheme,
                    onClick = { onSelect(key) }
                )
            }
        }
    }
}
