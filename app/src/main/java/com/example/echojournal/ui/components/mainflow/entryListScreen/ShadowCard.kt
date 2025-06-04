package com.example.echojournal.ui.components.mainflow.entryListScreen

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ShadowCard(
    onClick: () -> Unit,
    elevation: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val shadowColor = if (isDark) Color.White else Color.Black
    val shape = MaterialTheme.shapes.medium
    val elevationPx = with(LocalDensity.current) { elevation.toPx() }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .drawBehind {
                val cornerSize = shape.topStart
                val cornerRadiusPx = cornerSize.toPx(size, this)
                val paint = Paint().apply {
                    color = android.graphics.Color.TRANSPARENT
                    setShadowLayer(elevationPx, 0f, 0f, shadowColor.toArgb())
                    maskFilter = BlurMaskFilter(elevationPx, BlurMaskFilter.Blur.NORMAL)
                }
                val rect = RectF(0f, 0f, size.width, size.height)
                drawContext.canvas.nativeCanvas.apply {
                    save()
                    drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, paint)
                    restore()
                }
            }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = shape,
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Box(modifier = Modifier) {
                content()
            }
        }
    }
}
