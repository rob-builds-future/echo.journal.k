package com.rbf.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.rbf.echojournal.data.remote.model.util.LanguageDto
import com.rbf.echojournal.ui.viewModel.LanguageViewModel
import com.rbf.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LanguagePickerList(
    label: String,
    prefsViewModel: PrefsViewModel = koinViewModel(),
    languageViewModel: LanguageViewModel = koinViewModel(),
    placeholder: String,
    onSelect: (LanguageDto) -> Unit
) {
    // 1) Alle Sprachen vom ViewModel
    val allLangs by languageViewModel.localizedLanguages.collectAsState(emptyList())
    // 2) aktuell gespeicherter Code – das ist unsere Single Source of Truth
    val currentCode by prefsViewModel.currentLanguage.collectAsState()

    var query by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier) {

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            singleLine = true, // Verhindert Zeilenumbruch
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done // oder ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }, // Keyboard schließen!
                onSearch = { focusManager.clearFocus() } // falls du Search willst
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(Modifier.height(12.dp))

        LazyColumn {
            items(
                items = allLangs.filter { it.name.contains(query, ignoreCase = true) },
                key   = { it.code }
            ) { dto ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // 1) lokalen DataStore updaten
                            prefsViewModel.setLanguage(dto.code)
                            // 2) Firestore/Backend updaten (falls gewünscht)
                            onSelect(dto)
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (dto.code == currentCode),
                        onClick  = {
                            prefsViewModel.setLanguage(dto.code)
                            onSelect(dto)
                        }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = dto.name)
                }
            }
        }
    }
}
