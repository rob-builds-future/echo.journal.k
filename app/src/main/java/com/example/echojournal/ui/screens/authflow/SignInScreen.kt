package com.example.echojournal.ui.screens.authflow

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.echojournal.ui.viewModel.AuthViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Sign-In Screen mit Feldern am unteren Rand.
 * Wichtig: windowSoftInputMode="adjustResize" in AndroidManifest setzen.
 */
@Composable
fun SignInScreen(
    onSignedIn: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val user by viewModel.user.collectAsState()

    // Fokus und Tastatur
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val passwordRequester = remember { FocusRequester() }

    if (user != null) {
        onSignedIn()
        return
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding() // schiebt Inhalt bei geöffneter Tastatur
                .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } }
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.email.value = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { passwordRequester.requestFocus() }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.password.value = it },
                label = { Text("Passwort") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordRequester),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (error != null) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = { viewModel.signIn() },
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                if (loading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                else Text("Anmelden")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { onSignUpClick() }) {
                Text("Noch keinen Account? Registrieren")
            }
        }
    }
}