package com.rbf.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rbf.echojournal.R
import com.rbf.echojournal.ui.components.mainflow.settingsScreen.SettingItem
import com.rbf.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileSettingInfo(
    memberSince: String,
    username: String,
    language: String,
    onLogout: () -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    val prefsViewModel: PrefsViewModel = koinViewModel()
    val currentTheme by prefsViewModel.theme.collectAsState()
    val currentTemplate by prefsViewModel.currentTemplate.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteConfirmationText by remember { mutableStateOf("") }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Dummy‐Status ebenfalls aus Resource
    //val dummyRemindersStatus = stringResource(R.string.profile_info_reminders_status)

    val displayMap = mapOf(
        "Smaragd" to R.string.theme_display_emerald,
        "Wolkenlos" to R.string.theme_display_cloudless,
        "Vintage" to R.string.theme_display_vintage,
        "Koralle" to R.string.theme_display_coral,
        "Bernstein" to R.string.theme_display_amber
    )
    val themeLabel =
        stringResource(displayMap[currentTheme] ?: R.string.theme_display_cloudless)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Column {
                Text(
                    text = stringResource(
                        R.string.profile_info_member_since,
                        memberSince
                    ),
                    modifier = Modifier.padding(
                        vertical = 12.dp,
                        horizontal = 16.dp
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(
                        R.string.profile_info_username,
                        username.ifBlank { "–" }),
                    modifier = Modifier.padding(
                        vertical = 12.dp,
                        horizontal = 16.dp
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = stringResource(R.string.profile_info_language, language),
                    modifier = Modifier.padding(
                        vertical = 12.dp,
                        horizontal = 16.dp
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = stringResource(R.string.profile_info_theme, themeLabel),
                    modifier = Modifier.padding(
                        vertical = 12.dp,
                        horizontal = 16.dp
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = stringResource(
                        R.string.profile_info_template,
                        currentTemplate.ifBlank { stringResource(R.string.template_none) }
                    ),
                    modifier = Modifier.padding(
                        vertical = 12.dp,
                        horizontal = 16.dp
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
//            Text(
//                text = stringResource(R.string.profile_info_reminders, dummyRemindersStatus),
//                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Medium
//            )
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // Buttons nach unten drücken

        // Logout-Card
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Gray,
                contentColor = MaterialTheme.colorScheme.background
            ),
            onClick = { showLogoutDialog = true }
        ) {
            SettingItem(
                label = stringResource(R.string.settings_logout),
                value = "",
                onClick = { showLogoutDialog = true }
            )
        }

        // Delete Profile Card (rot markiert)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 12.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Red.copy(alpha = 0.9f),
                contentColor = MaterialTheme.colorScheme.onError
            ),
            onClick = { showDeleteDialog = true }
        ) {
            SettingItem(
                label = stringResource(R.string.profile_info_delete),
                value = "",
                onClick = { showDeleteDialog = true }
            )
        }

        // Logout Dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text(stringResource(R.string.logout_dialog_title)) },
                text = { Text(stringResource(R.string.logout_dialog_message)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            onLogout()
                        }
                    ) { Text(stringResource(R.string.button_yes)) }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text(stringResource(R.string.button_no))
                    }
                }
            )
        }

// Delete Profile Dialog (mit "DELETE" als Bestätigung)
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(stringResource(R.string.profile_info_delete_confirm_title)) },
                text = {
                    Column {
                        Text(stringResource(R.string.profile_info_delete_confirm_message))
                        Spacer(Modifier.height(8.dp))
                        Text(
                            stringResource(R.string.delete_profile_confirm_type),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        TextField(
                            value = deleteConfirmationText,
                            onValueChange = { deleteConfirmationText = it },
                            placeholder = { Text("DELETE") },
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (deleteConfirmationText == "DELETE") {
                                showDeleteDialog = false
                                onDeleteConfirmed()
                            }
                        },
                        enabled = deleteConfirmationText == "DELETE"
                    ) {
                        Text(stringResource(R.string.profile_info_delete))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text(stringResource(R.string.button_cancel))
                    }
                }
            )
        }
    }
}