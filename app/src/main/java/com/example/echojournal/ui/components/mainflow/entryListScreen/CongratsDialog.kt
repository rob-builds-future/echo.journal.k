package com.example.echojournal.ui.components.mainflow.entryListScreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.echojournal.R

@Composable
fun CongratsDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onFlameIconPositioned: (Offset) -> Unit,
    onNiceClick: (Offset) -> Unit
) {
    if (!show) return

    var flameIconOffset by remember { mutableStateOf(Offset.Zero) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        icon = { /* optional */ },
        title = {
            Text(stringResource(R.string.congrats_title))
        },
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Whatshot,
                    contentDescription = null,
                    tint = Color.Yellow,
                    modifier = Modifier
                        .size(32.dp)
                        .onGloballyPositioned { coords ->
                            val pos = coords.localToWindow(Offset.Zero)
                            val center = Offset(
                                pos.x + coords.size.width / 2,
                                pos.y + coords.size.height / 2
                            )
                            flameIconOffset = center
                            onFlameIconPositioned(center)
                        }
                )
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.congrats_message))
            }
        },
        confirmButton = {
            TextButton(onClick = { onNiceClick(flameIconOffset) }) {
                Text(stringResource(R.string.button_nice))
            }
        }
    )
}