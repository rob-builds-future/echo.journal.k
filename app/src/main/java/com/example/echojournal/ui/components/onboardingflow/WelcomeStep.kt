package com.example.echojournal.ui.components.onboardingflow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echojournal.R
import com.example.echojournal.ui.components.authflow.AnimatedEchoSymbol

@Composable
fun WelcomeStep(
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Überschrift: z.B. "Lerne echo kennen –"
        Row(modifier = Modifier.padding(horizontal = 18.dp)) {
            Text(
                text = stringResource(R.string.onboarding_welcome_headline_1),
                fontSize = 28.sp
            )
            Text(
                text = stringResource(R.string.onboarding_welcome_headline_echo),
                fontSize = 28.sp,
                color = colorResource(id = R.color.Lichtblau)
            )
            Text(
                text = stringResource(R.string.onboarding_welcome_headline_2),
                fontSize = 28.sp
            )
        }

        Text(
            text = stringResource(R.string.onboarding_welcome_subheadline),
            fontSize = 28.sp,
            lineHeight = 28.sp,
            modifier = Modifier.padding(horizontal = 18.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Echo-Row
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            Text(
                text = stringResource(R.string.onboarding_welcome_intro),
                fontSize = 24.sp
            )

            AnimatedEchoSymbol(
                color = colorResource(id = R.color.Lichtblau),
                maxDiameter = 100.dp,
                step = 20.dp,
                circleCount = 4,
                strokeWidth = 4.dp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(R.string.onboarding_welcome_body),
            fontSize = 24.sp,
            lineHeight= 24.sp,
            modifier = Modifier.padding(horizontal = 18.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text(stringResource(R.string.onboarding_button_next))
        }
    }
}
