package com.rbf.echojournal.ui.components.authflow

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EchoSplashScreen(
    color: Color,
    maxDiameter: Dp = 120.dp,
    step: Dp = 24.dp,
    circleCount: Int = 4,
    strokeWidth: Dp = 8.dp,
    text: String = "e.",
    textColor: Color = Color.White,
    textSize: Dp = 48.dp,
    textWeight: FontWeight = FontWeight.Bold,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(maxDiameter),
        contentAlignment = Alignment.Center
    ) {
        // Animated echo symbol as background
        val infiniteTransition = rememberInfiniteTransition(label = "echoPulse")

        // Create animated fractions for each circle (phase-shifted)
        val animatedFractions = List(circleCount) { index ->
            val phase = index * 0.20f
            infiniteTransition.animateFloat(
                initialValue = 0.92f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1300, easing = EaseInOutCubic),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset((phase * 1300).toInt())
                ),
                label = "circleAnim$index"
            )
        }

        // Draw echo circles
        repeat(circleCount) { index ->
            val baseDiameter = maxDiameter - step * index
            val animFraction = animatedFractions[index].value
            Box(
                Modifier
                    .size(baseDiameter * animFraction)
                    .border(
                        width = strokeWidth,
                        color = color.copy(alpha = 0.9f - 0.08f * index),
                        shape = CircleShape
                    )
            )
        }

        // Centered "e."
        Text(
            text = text,
            color = textColor,
            fontSize = textSize.value.sp,
            fontWeight = textWeight,
            style = MaterialTheme.typography.displayLarge
        )
    }
}
