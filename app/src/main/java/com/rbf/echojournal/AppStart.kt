package com.rbf.echojournal

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rbf.echojournal.navigation.AppNavGraph
import com.rbf.echojournal.ui.components.authflow.EchoSplashScreen
import com.rbf.echojournal.ui.theme.ColorManager
import com.rbf.echojournal.ui.viewModel.AuthViewModel
import com.rbf.echojournal.ui.viewModel.PrefsViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppStart() {
    val navController: NavHostController = rememberNavController()
    val authViewModel: AuthViewModel = koinViewModel()
    val prefsViewModel: PrefsViewModel = koinViewModel()

    // States holen
    val user by authViewModel.user.collectAsState()
    val onboarded by prefsViewModel.onboarded.collectAsState()
    val loading by authViewModel.loading.collectAsState()
    val themeName by prefsViewModel.theme.collectAsState()

    // Splash mindestens 2 Sekunden zeigen
    var splashMinTimeReached by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(3000)
        splashMinTimeReached = true
    }

    // Splash solange, bis Daten da und 2 Sek. um!
    val showSplash = !splashMinTimeReached || loading

    if (showSplash) {
        // SplashScreen mit animiertem EchoLogo
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            EchoSplashScreen(
                color = ColorManager.getColor(themeName),
                maxDiameter = 200.dp,
                step = 38.dp,
                text = "e.",
                textColor = MaterialTheme.colorScheme.primary,
                textSize = 40.dp,
            )
        }
    } else {
        AppNavGraph(
            navController = navController,
            authViewModel = authViewModel,
            prefsViewModel = prefsViewModel,
            onInstagramClick = {}
        )
    }
}