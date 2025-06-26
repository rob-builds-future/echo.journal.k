package com.rbf.echojournal.ui.components.onboardingflow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StepIndicator(
    current: Int,
    total: Int,
    activeColor: Color,
    inactiveColor: Color = androidx.compose.ui.graphics.Color.Companion.White
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(total) { i ->
            Box(
                Modifier
                    .padding(horizontal = 6.dp)
                    .size(14.dp)
                    .background(
                        if (i == current) activeColor else inactiveColor,
                        shape = RoundedCornerShape(7.dp)
                    )
            )
        }
    }
}