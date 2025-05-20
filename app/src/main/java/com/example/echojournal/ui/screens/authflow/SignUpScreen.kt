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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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

@Composable
fun SignUpScreen(
    onSignedUp: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val username by viewModel.username.collectAsState()
    val email    by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val loading  by viewModel.loading.collectAsState()
    val error    by viewModel.error.collectAsState()
    val user     by viewModel.user.collectAsState()

    // Focus- & Keyboard-Controller
    val focusManager       = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val emailRequester     = remember { FocusRequester() }
    val passwordRequester  = remember { FocusRequester() }

    // sobald eingeloggt, weiter
    if (user != null) {
        onSignedUp()
        return
    }

    Scaffold { insets ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(insets)
                .imePadding() // passt Layout bei geöffneter Tastatur an
                .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } }
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Benutzername
            OutlinedTextField(
                value = username,
                onValueChange = { viewModel.username.value = it },
                label = { Text("Benutzername") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { emailRequester.requestFocus() }
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            // E-Mail
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
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailRequester)
            )
            Spacer(Modifier.height(8.dp))

            // Passwort
            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.password.value = it },
                label = { Text("Passwort") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordRequester)
            )
            Spacer(Modifier.height(16.dp))

            // Fehleranzeige
            error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            // Registrieren-Button
            Button(
                onClick = { viewModel.signUp() },
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor   = MaterialTheme.colorScheme.onBackground
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                if (loading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                else Text("Registrieren")
            }
            Spacer(Modifier.height(8.dp))

            // Link zum Login
            TextButton(onClick = { /* navigate to sign-in */ }) {
                Text("Bereits registriert? Anmelden")
            }
        }
    }
}