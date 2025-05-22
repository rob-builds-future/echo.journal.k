package com.example.echojournal.ui.components.authflow

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.echojournal.R

@Composable
fun SignInWithGoogle(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.android_neutral_sq_ctn_3x),
        contentDescription = "Mit Google anmelden",
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable { onClick() }
    )
}