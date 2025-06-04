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
import androidx.compose.ui.text.AnnotatedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryListTopBar(
    title: AnnotatedString,
    onSettingsClick: () -> Unit,
    onStatsClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(title) } ,
        navigationIcon = {
            IconButton(onClick = onStatsClick) {
                Icon(imageVector = Icons.Default.Insights, contentDescription = "Erfolg & Meilensteine")
            }
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Default.MoreVert, contentDescription = "Settings")
            }
        }
    )
}