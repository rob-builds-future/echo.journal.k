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
fun AnimatedEchoSymbol(
    color: Color,
    maxDiameter: Dp = 215.dp,
    step: Dp = 40.dp,
    circleCount: Int = 4,
    strokeWidth: Dp = 8.dp,
    modifier: Modifier = Modifier
) {
    // Infinite Animation für "Pulsieren"
    val infiniteTransition = rememberInfiniteTransition(label = "echoPulse")

    // Jeder Kreis bekommt eine phasenverschobene Animation (leichte Versetzung)
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

    Box(
        modifier = modifier.size(maxDiameter),
        contentAlignment = Alignment.Center
    ) {
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
    }
}

@Composable
fun AnimatedEchoLogoWithText(
    color: Color,
    maxDiameter: Dp,
    step: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedEchoSymbol(
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