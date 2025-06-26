package com.rbf.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rbf.echojournal.R
import com.rbf.echojournal.ui.viewModel.AuthViewModel
import com.rbf.echojournal.ui.viewModel.PrefsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileSettingUsername(
    prefsViewModel: PrefsViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    val currentUsername by prefsViewModel.username.collectAsState()
    var textFieldValue by remember { mutableStateOf(currentUsername) }
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    // Sync bei externem Update
    LaunchedEffect(currentUsername) {
        textFieldValue = currentUsername
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.profile_username_instruction),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(
                R.string.profile_username_current,
                currentUsername.ifBlank { "–" }
            ),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = { Text(stringResource(R.string.profile_username_label)) },
            singleLine = true, // Verhindert Zeilenumbruch
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done // oder ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }, // Keyboard schließen!
                onSearch = { focusManager.clearFocus() } // falls du Search willst
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (textFieldValue.isNotBlank() && textFieldValue != currentUsername) {
                        scope.launch {
                            authViewModel.updateUsername(textFieldValue)
                            showDialog = true
                        }
                    }
                },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Box(modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)) {
                Text(
                    text = stringResource(R.string.profile_username_button_save),
                    fontSize = 16.sp
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = AlertDialogDefaults.shape
                ),
            containerColor = MaterialTheme.colorScheme.surface,
            text = {
                Text(
                    text = stringResource(
                        R.string.profile_username_success,
                        textFieldValue
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(stringResource(R.string.button_ok))
                }
            }
        )
    }
}
