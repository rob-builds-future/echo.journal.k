package com.rbf.echojournal.ui.screens.authflow

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.rbf.echojournal.R
import com.rbf.echojournal.ui.components.authflow.EchoLogoWithText
import com.rbf.echojournal.ui.viewModel.AuthViewModel
import com.rbf.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    onSignedUp: () -> Unit,
    onSignInClick: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val prefsViewModel: PrefsViewModel = koinViewModel()
    val themeName by prefsViewModel.theme.collectAsState()
    val echoColor = com.rbf.echojournal.ui.theme.ColorManager.getColor(themeName)

    val username by viewModel.username.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val user by viewModel.user.collectAsState()

    // Error Handling
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(error) {
        error?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    // Fokus & Keyboard
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val emailRequester = remember { FocusRequester() }
    val passwordRequester = remember { FocusRequester() }

    // Passwort-Validierung
    val passwordValid = remember(password) {
        Regex("""^(?=.*[A-Z])(?=.*\d).{8,}$""").matches(password)
    }

    // Navigation nach erfolgreichem SignUp
    if (user != null) {
        onSignedUp()
        return
    }

    val backgroundPainter = painterResource(id = R.drawable.background2)

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
        unfocusedIndicatorColor = Color.Transparent,
    )

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
        contentColor = MaterialTheme.colorScheme.primary,
        disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    )

    Scaffold { insets ->
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
                    .background(Color.Black.copy(alpha = 0.25f))
            )

            // ─── UI im Vordergrund ───────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(insets)
                    .imePadding()
                    .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } }
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EchoLogoWithText(
                    color = echoColor,
                    maxDiameter = 170.dp,
                    step = 32.dp,
                    modifier = Modifier.padding(top = 32.dp)
                )
                Spacer(modifier = Modifier.weight(1f))

                // ─── Benutzername ───────────────────────────
                OutlinedTextField(
                    value = username,
                    onValueChange = { viewModel.username.value = it },
                    label = { Text(stringResource(R.string.label_username)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { emailRequester.requestFocus() }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                Spacer(Modifier.height(8.dp))

                // ─── E-Mail ────────────────────────────────
                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.email.value = it },
                    label = { Text(stringResource(R.string.label_email)) },
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
                        .focusRequester(emailRequester),
                    colors = textFieldColors
                )
                Spacer(Modifier.height(8.dp))

                // ─── Passwort ──────────────────────────────
                OutlinedTextField(
                    value = password,
                    onValueChange = { viewModel.password.value = it },
                    label = { Text(stringResource(R.string.label_password)) },
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
                Spacer(modifier = Modifier.height(4.dp))

                // ─── Passwort-Anforderungstext ────────────
                Text(
                    text = stringResource(R.string.text_password_requirements),
                    style = MaterialTheme.typography.bodySmall,
                    color = when {
                        password.isEmpty() -> Color.White
                        passwordValid -> colorResource(id = R.color.Smaragdgrün)
                        else -> MaterialTheme.colorScheme.error
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                )
                Spacer(Modifier.height(16.dp))

                // ─── Registrieren-Button ───────────────────
                val canRegister =
                    username.isNotBlank() &&
                            email.isNotBlank() &&
                            passwordValid &&
                            !loading

                Button(
                    onClick = { viewModel.signUp() },
                    enabled = canRegister,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = buttonColors,
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    if (loading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = stringResource(R.string.button_register),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(Modifier.weight(0.1f))

                // ─── Link zum Login ────────────────────────
                TextButton(
                    onClick = { onSignInClick() }) {
                    Text(
                        text = stringResource(R.string.text_already_registered_sign_in),
                        color = Color.White
                    )
                }
            }
        }
    }
}
