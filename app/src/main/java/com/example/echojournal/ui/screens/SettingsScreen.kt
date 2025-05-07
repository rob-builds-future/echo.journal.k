package com.example.echojournal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.echojournal.ui.components.SettingsScreen.AppSettingsCard
import com.example.echojournal.ui.components.SettingsScreen.InstaButton
import com.example.echojournal.ui.components.SettingsScreen.ProfileSettingsCard
import com.example.echojournal.ui.components.SettingsScreen.SettingItem
import com.example.echojournal.util.SettingType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToProfile: (SettingType) -> Unit = {},
    onNavigateToAppSetting: (SettingType) -> Unit = {},
    onInstagramClick: () -> Unit = {},
    onLogoutConfirmed: () -> Unit = {},
    version: String = "v1.0"
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Deine Einstellungen", style = MaterialTheme.typography.titleMedium) }
            )
        }
    ) { innerPadding ->

        var showLogoutDialog by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ───────── Profil-Einstellungen ─────────
            Text(
                text = "Profil-Einstellungen",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 4.dp)
            )
            ProfileSettingsCard(
                onNavigateToProfile = onNavigateToProfile
            )

            // ───────── App-Einstellungen ─────────
            Text(
                text = "App-Einstellungen",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(start = 4.dp, top = 12.dp)
            )
            AppSettingsCard(
                onNavigateToAppSetting = onNavigateToAppSetting
            )

            // ───────── Abmelden ─────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Gray,
                    contentColor   = MaterialTheme.colorScheme.background
                )
            ) {
                SettingItem(
                    label = "Abmelden",
                    value = "",
                    onClick = { showLogoutDialog = true }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // ───────── Footer: Instagram + Version ─────────
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = CenterHorizontally
            ) {
                IconButton(onClick = onInstagramClick) {
                    InstaButton()
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = version,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text("Abmelden") },
                    text = { Text("Möchtest du dich wirklich abmelden?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showLogoutDialog = false
                                onLogoutConfirmed()
                            }
                        ) { Text("Ja") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showLogoutDialog = false }) {
                            Text("Nein")
                        }
                    }
                )
            }
        }
    }
}