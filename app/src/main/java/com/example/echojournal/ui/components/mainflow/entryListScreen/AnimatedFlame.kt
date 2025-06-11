package com.example.echojournal.ui.components.mainflow.entryListScreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedFlame(
    from: Offset,
    to: Offset,
    onFinished: () -> Unit,
    flameColor: Color
) {
    // Animations-Fortschritt von 0f bis 1f
    val progress = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val initialSize = 48.dp

    // Interpolierte Werte
    val currentOffset = lerpOffset(from, to, progress.value)
    val scale = when {
        progress.value < 0.15f -> 1f + 1.2f * (progress.value / 0.15f) // Start-Pop
        else -> 1.2f - 0.7f * ((progress.value - 0.15f) / 0.85f) // Schrumpft auf 0.5
    }.coerceAtLeast(0.2f)

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1100)
        )
        onFinished()
    }

    Box(
        Modifier
            .absoluteOffset {
                androidx.compose.ui.unit.IntOffset(
                    currentOffset.x.toInt(),
                    currentOffset.y.toInt()
                )
            }
            .scale(scale)
            .size(initialSize)
    ) {
        Icon(
            imageVector = Icons.Default.Whatshot,
            contentDescription = null,
            tint = flameColor,
            modifier = Modifier
                .size(initialSize)
        )
    }
}

// Lineare Interpolation für Offset
fun lerpOffset(start: Offset, end: Offset, fraction: Float): Offset {
    return Offset(
        start.x + (end.x - start.x) * fraction,
        start.y + (end.y - start.y) * fraction
    )
}
