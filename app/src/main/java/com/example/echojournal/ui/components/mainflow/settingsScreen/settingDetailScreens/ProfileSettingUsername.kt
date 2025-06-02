package com.example.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echojournal.ui.viewModel.AuthViewModel
import com.example.echojournal.ui.viewModel.PrefsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileSettingUsername(
    prefsViewModel: PrefsViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    // Aktuellen Username aus Prefs holen
    val currentUsername by prefsViewModel.username.collectAsState()

    // Lokaler Text-State, der in das TextField bindet
    var textFieldValue by remember { mutableStateOf(currentUsername) }

    // Flag für den Erfolgs-Dialog
    var showDialog by remember { mutableStateOf(false) }

    // Damit sich das TextField anpasst, sobald sich PrefsFlow ändert
    LaunchedEffect(currentUsername) {
        textFieldValue = currentUsername
    }

    // CoroutineScope zum Aufrufen des suspend‐Fun‐Updates
    val scope = rememberCoroutineScope()


    Column(Modifier.padding(16.dp)) {
        Text(text = "Hier kannst du deinen Benutzernamen ändern.")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Aktueller Benutzername: ${currentUsername.ifBlank { "–" }}")
        Spacer(modifier = Modifier.height(32.dp))

        // → value nimmt nun usernameState (non-nullable String)
        TextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Neuer Benutzername") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedLabelColor = Color.White.copy(alpha = 0.8f),
                unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                disabledLabelColor = Color.White.copy(alpha = 0.4f),
                errorLabelColor = Color.White.copy(alpha = 0.8f),
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                disabledTextColor = Color.White,
                errorTextColor = Color.Red,
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Black,
                disabledContainerColor = Color.Black,
                errorContainerColor = Color.Black,
                focusedPlaceholderColor = Color.White,
                unfocusedPlaceholderColor = Color.White,
                disabledPlaceholderColor = Color.White,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Speichern-Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .clickable {
                    if (textFieldValue.isNotBlank() && textFieldValue != currentUsername) {
                        scope.launch {
                            // 1) Update in Firestore + AuthViewModel (dies schreibt dann auch in Prefs)
                            authViewModel.updateUsername(textFieldValue)
                            // 2) Erfolgsdialog öffnen
                            showDialog = true
                        }
                    }
                },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Gray,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Text(
                text = "Speichern",
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

    // Dialog (erst nach Firestore + Prefs‐Update):
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = { Text("Dein Nutzername wurde geändert zu „$textFieldValue“") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
