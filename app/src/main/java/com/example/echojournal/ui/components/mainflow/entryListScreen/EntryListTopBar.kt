package com.example.echojournal.ui.components.mainflow.entryListScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import com.example.echojournal.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryListTopBar(
    title: AnnotatedString,
    onSettingsClick: () -> Unit,
    onStatsClick: () -> Unit,
    onStatsIconPositioned: (Offset) -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onStatsClick,
                modifier = Modifier.onGloballyPositioned {
                    onStatsIconPositioned(it.localToWindow(Offset.Zero))
                }) {
                Icon(
                    imageVector = Icons.Default.Insights,
                    contentDescription = stringResource(R.string.contentdesc_stats)
                )
            }
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.contentdesc_settings)
                )
            }
        }
    )
}
