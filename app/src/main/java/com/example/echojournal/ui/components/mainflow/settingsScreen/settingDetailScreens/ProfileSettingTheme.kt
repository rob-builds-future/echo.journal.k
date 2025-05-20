package com.example.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.echojournal.R

/**
 * Screen zur Auswahl der Echo-Farbe. Zeigt fünf vordefinierte Farbalternativen,
 * jeweils mit Echo-Icon auf farbigem Hintergrund, Bezeichnung und RadioButton.
 *
 * @param initialColorName Name der vorgewählten Farbe
 * @param onSelect Callback mit dem Namen der ausgewählten Farbe
 */

@Composable
fun ProfileSettingTheme(
    initialColorName: String,
    onSelect: (String) -> Unit
) {
    // Fünf Beispiel-Farben
    val options = listOf(
        "Smaragd" to colorResource(id = R.color.Smaragdgrün),
        "Wolkenlos" to colorResource(id = R.color.Lichtblau),
        "Vintage" to colorResource(id = R.color.Vintagepurpur),
        "Koralle" to colorResource(id = R.color.Korallorange),
        "Bernstein" to colorResource(id = R.color.Bernsteingelb)
    )
    var selected by remember { mutableStateOf(initialColorName) }

    Column {
        Text("Deine Echo-Farbe wählen:")
        Spacer(modifier = Modifier.height(16.dp))
        options.forEach { (name, color) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable {
                        selected = name
                        onSelect(name)
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = color, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_echo),
//                            contentDescription = "Echo Icon"
//                        )
                }
                Spacer(modifier = Modifier.padding(horizontal = 16.dp))
                Text(text = name, modifier = Modifier.weight(1f))
                RadioButton(
                    selected = name == selected,
                    onClick = {
                        selected = name
                        onSelect(name)
                    }
                )
            }
        }
    }
}
