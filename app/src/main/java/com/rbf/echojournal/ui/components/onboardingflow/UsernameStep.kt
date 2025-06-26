package com.rbf.echojournal.ui.components.onboardingflow

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.rbf.echojournal.R
import com.rbf.echojournal.ui.viewModel.AuthViewModel
import com.rbf.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun UsernameStep(
    onNext: (String) -> Unit,
    onBack: () -> Unit,
    authViewModel: AuthViewModel = koinViewModel(),
    prefsViewModel: PrefsViewModel = koinViewModel()
) {
    val currentUsername by prefsViewModel.username.collectAsState()

    var username by remember(currentUsername) { mutableStateOf(currentUsername) }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(stringResource(R.string.onboarding_username_title))
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(R.string.onboarding_username_label)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done // oder ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }, // Keyboard schließen!
                onSearch = { focusManager.clearFocus() } // falls du Search willst
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        BottomBarButton(
            text = stringResource(R.string.onboarding_button_next),
            onClick = {
                if (username.isNotBlank()) {
                    authViewModel.updateUsername(username)
                    onNext(username)
                }
            },
            enabled = username.isNotBlank(),
        )
    }
}
