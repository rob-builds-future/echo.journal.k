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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


/**
 * Screen zum Ändern des Benutzernamens.
 * @param initialUsername aktueller Benutzername zum Vorbefüllen
 * @param onSave Callback mit neuem Benutzernamen
 */
@Composable
fun ProfileSettingUsername(
    initialUsername: String,
    onSave: (String) -> Unit,
) {
    var username by remember { mutableStateOf(initialUsername) }
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Text(text = "Hier kannst du deinen Benutzernamen ändern.")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Dein aktueller Benutzername: $initialUsername")
        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
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

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .clickable {
                    onSave(username)
                    showDialog = true
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

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = { Text("Dein Nutzername wurde geändert zu $username") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
