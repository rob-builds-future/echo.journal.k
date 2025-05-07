package com.example.echojournal.ui.components.entryListScreen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun InspirationPopoverPlaceholder(onDismiss: () -> Unit) {
    // Simpler Dialog statt Popover
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Inspiration") },
        text = { Text("Dein täglicher Denkanstoß…") },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("OK") }
        }
    )
}