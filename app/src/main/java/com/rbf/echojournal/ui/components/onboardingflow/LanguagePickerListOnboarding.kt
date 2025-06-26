package com.rbf.echojournal.ui.components.onboardingflow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.rbf.echojournal.R
import com.rbf.echojournal.data.remote.model.util.LanguageDto

@Composable
fun LanguagePickerListOnboarding(
    languages: List<LanguageDto>,
    selectedCode: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text(stringResource(R.string.settings_profile_target_language)) },
            placeholder = { Text(stringResource(R.string.placeholder_search_language)) },
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
        Spacer(Modifier.height(8.dp))

        val filtered = languages.filter { it.name.contains(query, ignoreCase = true) }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(filtered, key = { it.code }) { lang ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(lang.code) }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = lang.code == selectedCode,
                        onClick = { onSelect(lang.code) }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(lang.name)
                }
            }
        }
    }
}

