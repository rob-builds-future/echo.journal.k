import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileSettingTheme(
    prefsViewModel: PrefsViewModel = koinViewModel()
) {
    // Aktuellen Theme-Namen aus ViewModel
    val currentTheme by prefsViewModel.theme.collectAsState()
    val options = listOf(
        "Smaragd", "Wolkenlos", "Vintage", "Koralle", "Bernstein"
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Deine Echo-Farbe wählen:")
        Spacer(Modifier.height(16.dp))
        options.forEach { name ->
            // Compose Color aus Ressourcen via ColorManager
            val color = ColorManager.getColor(name)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable {
                        prefsViewModel.setTheme(name)
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = color, shape = RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.width(16.dp))
                Text(text = name, modifier = Modifier.weight(1f))
                RadioButton(
                    selected = name == currentTheme,
                    onClick  = { prefsViewModel.setTheme(name) }
                )
            }
        }
    }
}