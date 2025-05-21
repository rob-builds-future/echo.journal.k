package com.example.echojournal.ui.components.authflow

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun EchoSymbol(
    color: Color,
    maxDiameter: Dp = 215.dp,     // äußerster Kreis-Durchmesser
    step: Dp = 40.dp,             // Abstand zwischen den Kreisdurchmessern
    circleCount: Int = 4,         // Anzahl der Kreise
    strokeWidth: Dp = 8.dp,       // Linienstärke
    modifier: Modifier = Modifier // hier kannst du Größe/Padding/etc. anpassen
) {
    // Box mit genau der Größe des größten Kreises
    Box(
        modifier = modifier.size(maxDiameter),
        contentAlignment = Alignment.Center
    ) {
        // Zeichne circleCount Kreise, die jeweils um 'step' kleiner werden
        repeat(circleCount) { index ->
            val diameter = maxDiameter - step * index
            Box(
                Modifier
                    .size(diameter)
                    .border(
                        width = strokeWidth,
                        color = color,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun EchoLogoWithText(
    color: Color,
    maxDiameter: Dp,
    step: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EchoSymbol(
            color = color,
            maxDiameter = maxDiameter,
            step = step,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "echo.",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
