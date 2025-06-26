package com.rbf.echojournal.ui.components.mainflow.entryListScreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.rbf.echojournal.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryListTopBar(
    title: AnnotatedString,
    onSettingsClick: () -> Unit,
    onStatsClick: () -> Unit,
    onStatsIconPositioned: (Offset) -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        // Kein navigationIcon mehr, alles in actions
        actions = {
            // Row für zwei Icons nebeneinander
            Row {
                IconButton(
                    onClick = onStatsClick,
                    modifier = Modifier.onGloballyPositioned {
                        onStatsIconPositioned(it.localToWindow(Offset.Zero))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Insights,
                        contentDescription = stringResource(R.string.contentdesc_stats)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.contentdesc_settings)
                    )
                }
            }
        }
    )
}