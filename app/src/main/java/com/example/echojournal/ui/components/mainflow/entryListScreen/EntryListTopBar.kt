package com.example.echojournal.ui.components.mainflow.entryListScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryListTopBar(
    title: AnnotatedString,
    onSettingsClick: () -> Unit,
    onStatsClick: () -> Unit
) {
    TopAppBar(
        title = { Text(title) } ,
        navigationIcon = {
            IconButton(onClick = onStatsClick) {
                Icon(Icons.Default.DateRange, contentDescription = "Stats")
            }
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
    )
}