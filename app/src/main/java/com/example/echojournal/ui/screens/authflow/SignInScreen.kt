package com.example.echojournal.ui.screens.authflow

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.echojournal.R
import com.example.echojournal.ui.components.authflow.EchoLogoWithText
import com.example.echojournal.ui.components.authflow.SignInWithGoogle
import com.example.echojournal.ui.viewModel.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInScreen(
    onSignedIn: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val user by viewModel.user.collectAsState()

    // Error Handling
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current
    val activity = remember(context) {
        context as? Activity
            ?: throw IllegalStateException("SignInScreen muss in einer Activity gerendert werden!")
    }

    LaunchedEffect(error) {
        if (error != null) {
            Toast
                .makeText(context, error, Toast.LENGTH_SHORT)
                .show()
            // Optional: danach das Error-State wieder zurücksetzen
            viewModel.clearError()
        }
    }

    // Fokus und Tastatur
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val passwordRequester = remember { FocusRequester() }

    // Navigation
    if (user != null) {
        onSignedIn()
        return
    }

    val backgroundPainter = painterResource(id = R.drawable.background)

    // einheitliche TextField-Farben
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
    )

    // Button-Farben: primary / onPrimary drehen sich je nach Theme
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.onPrimary,
        contentColor = MaterialTheme.colorScheme.primary
    )

    Scaffold { paddingValues ->
        // Painter oder Video Player
        Box(Modifier.fillMaxSize()) {
            // ─── Hintergrundbild ───────────────────────────
            Image(
                painter = backgroundPainter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            // ─── Schwarzer Filter darüber ─────────────────
            Box(
                Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )

            // ─── Bestehende UI ───────────────────────
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .imePadding()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { focusManager.clearFocus() }
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EchoLogoWithText(
                        color = colorResource(id = R.color.Lichtblau),
                        maxDiameter = 200.dp,
                        step = 35.dp,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
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
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )

                    Spacer(modifier = Modifier.height(8.dp))

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
                            .focusRequester(passwordRequester),
                        colors = textFieldColors
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.signIn() },
                        enabled = !loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = buttonColors,
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        if (loading) CircularProgressIndicator(
                            modifier = Modifier.size(
                                24.dp
                            )
                        )
                        else Text("Anmelden")
                    }
                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "ODER",
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    SignInWithGoogle(
                        onClick = { viewModel.signInWithGoogleOneTap(activity) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Wechsel zu Signup
                    TextButton(
                        onClick = { onSignUpClick() },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Noch keinen Account? Registrieren",
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}