package com.example.echojournal.ui.screens.onboardingflow

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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echojournal.R
import com.example.echojournal.ui.components.authflow.EchoSymbol

/**
 * Begrüßungsbildschirm im Onboarding.
 */
@Composable
fun WelcomeScreen(
    step: Int = 1,
    totalSteps: Int = 2,
    onNext: () -> Unit
) {
    Scaffold (
        topBar = {
            // linearer Balken, Höhe 4dp, schwarz, 50% gefüllt (1 von 2)
            LinearProgressIndicator(
                progress = { step / totalSteps.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = Color.Black,
                trackColor = Color.LightGray,
            )
        }
    ){
        paddingValues ->
        // Painter oder Video Player
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Row (modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(
                    text = "Lerne ",
                    fontSize = 32.sp
                )
                Text(
                    text = "echo ",
                    fontSize = 32.sp,
                    color = colorResource(id = R.color.Lichtblau)
                )
                Text(
                    text = "kennen –",
                    fontSize = 32.sp
                )
            }
            Text(
                text = "Deinen Begleiter in dieser App.",
                fontSize = 32.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 16.dp,                          // Abstand zwischen den Elementen
                    alignment = Alignment.CenterHorizontally // gesamte Row zentrieren
                )
            ) {
                Text(
                    text = "Hey, ich bin echo!",
                    fontSize = 24.sp

                )
                EchoSymbol(
                    color = colorResource(id = R.color.Lichtblau),
                    maxDiameter = 100.dp,
                    step = 20.dp,
                    strokeWidth = 5.dp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Betrachte es als die Stimme deines zukünftigen Ich, das mit Dir Schreib- und Sprach- ziele erreicht.",
                fontSize = 24.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("Weiter")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeScreenPreview() {
    WelcomeScreen(step = 1, totalSteps = 2, onNext = { })
}

