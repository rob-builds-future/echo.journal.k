package com.example.echojournal.service

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun NotificationPermissionRequester(
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val permission = Manifest.permission.POST_NOTIFICATIONS

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            onPermissionGranted()
        } else {
            // Optional: Hier könntet ihr einen Hinweis anzeigen, dass ohne Erlaubnis keine Notifikationen gehen.
        }
    }

    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                onPermissionGranted()
            }
            activity?.shouldShowRequestPermissionRationale(permission) == true -> {
                // Optional: erklärt, warum ihr die Benachrichtigungs-Erlaubnis braucht, und dann:
                launcher.launch(permission)
            }
            else -> {
                // Zum ersten Mal anfragen
                launcher.launch(permission)
            }
        }
    }
}