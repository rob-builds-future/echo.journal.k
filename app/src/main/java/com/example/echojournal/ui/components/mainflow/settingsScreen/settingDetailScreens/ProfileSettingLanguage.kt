
import androidx.compose.runtime.Composable
import com.example.echojournal.ui.viewModel.AuthViewModel
import com.example.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileSettingLanguage(
    prefsViewModel: PrefsViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel(),
    languageViewModel: LanguageViewModel = koinViewModel()
) {
    // Der Callback hier wird nach dem lokalen Speichern (Prefs) ausgeführt
    LanguagePickerList(
        label = "Zielsprache",
        prefsViewModel = prefsViewModel,
        languageViewModel = languageViewModel,
        onSelect = { dto ->
            // 1) lokal (DataStore)
            prefsViewModel.setLanguage(dto.code)
            // 2) remote (Firestore + lokaler User-State)
            authViewModel.updatePreferredLanguage(dto.code)
        }
    )
}
