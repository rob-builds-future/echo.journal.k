package com.example.echojournal.ui.screens.mainflow

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.echojournal.R
import com.example.echojournal.ui.components.mainflow.settingsScreen.AppSettingsCard
import com.example.echojournal.ui.components.mainflow.settingsScreen.InstaButton
import com.example.echojournal.ui.components.mainflow.settingsScreen.ProfileSettingsCard
import com.example.echojournal.ui.components.mainflow.settingsScreen.SettingItem
import com.example.echojournal.ui.components.mainflow.settingsScreen.SettingType
import com.example.echojournal.ui.viewModel.AuthViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigateToProfile: (SettingType) -> Unit = {},
    onNavigateToAppSetting: (SettingType) -> Unit = {},
    onInstagramClick: () -> Unit = {},
    onLogoutConfirmed: () -> Unit = {},
    version: String = "v1.0"
) {
    val authViewModel: AuthViewModel = koinViewModel()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(R.string.contentdesc_close)
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.settings_title),
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profil-Einstellungen Abschnitt
            Text(
                text = stringResource(R.string.settings_section_profile),
                modifier = Modifier.padding(start = 4.dp)
            )
            ProfileSettingsCard(onNavigateToProfile = onNavigateToProfile)

            // App-Einstellungen Abschnitt
            Text(
                text = stringResource(R.string.settings_section_app),
                modifier = Modifier.padding(start = 4.dp)
            )
            AppSettingsCard(onNavigateToAppSetting = onNavigateToAppSetting)

            // Abmelden
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Gray,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                SettingItem(
                    label = stringResource(R.string.settings_logout),
                    value = "",
                    onClick = { showLogoutDialog = true }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer: Instagram + Version
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = CenterHorizontally
            ) {
                IconButton(onClick = onInstagramClick) {
                    InstaButton()
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = version
                )
            }

            // Logout-Bestätigungsdialog
            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = AlertDialogDefaults.shape
                        ),
                    containerColor = MaterialTheme.colorScheme.surface,
                    title = {
                        Text(
                            stringResource(R.string.logout_dialog_title),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    text = {
                        Text(
                            stringResource(R.string.logout_dialog_message),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showLogoutDialog = false
                                authViewModel.signOut()
                                onLogoutConfirmed()
                            }, colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(stringResource(R.string.button_yes))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showLogoutDialog = false },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(stringResource(R.string.button_no))
                        }
                    }
                )
            }
        }
    }
}
