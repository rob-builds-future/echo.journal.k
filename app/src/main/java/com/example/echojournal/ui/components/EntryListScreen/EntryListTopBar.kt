package com.example.echojournal.ui.components.EntryListScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryListTopBar() {
    TopAppBar(
        title = { Text("Dein Journal") },
        navigationIcon = {
            IconButton(onClick = { /*  */ }) {
                Icon(Icons.Default.DateRange, contentDescription = "Stats")
            }
        },
        actions = {
            IconButton(onClick = { /*  */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
    )
}