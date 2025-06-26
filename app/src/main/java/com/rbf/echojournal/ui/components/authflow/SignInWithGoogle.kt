package com.rbf.echojournal.ui.components.authflow

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.rbf.echojournal.R

@Composable
fun SignInWithGoogle(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val res     = if (isDark)
        R.drawable.android_dark_sq_ctn_3x
    else
        R.drawable.android_neutral_sq_ctn_3x

    Image(
        painter            = painterResource(id = res),
        contentDescription = stringResource(R.string.contentdesc_sign_in_with_google),
        modifier           = modifier
            .clickable(onClick = onClick)
    )
}
