package com.rbf.echojournal.ui.components.onboardingflow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StepLabels(
    labels: List<String>,
    currentStep: Int,
    activeColor: Color,
    inactiveColor: Color = Color.LightGray
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        labels.forEachIndexed { i, label ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier
                        .size(24.dp)
                        .background(
                            if (i == currentStep) activeColor else inactiveColor,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("${i+1}", color = Color.White, fontSize = 14.sp)
                }
                Text(
                    label,
                    fontSize = 12.sp,
                    color = if (i == currentStep) activeColor else inactiveColor,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
