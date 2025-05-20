package com.example.echojournal

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.echojournal.navigation.AppNavGraph
import com.example.echojournal.ui.viewModel.AuthViewModel
import com.example.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppStart() {
    val navController: NavHostController = rememberNavController()

    val authViewModel: AuthViewModel = koinViewModel()
    val prefsViewModel: PrefsViewModel = koinViewModel()

    AppNavGraph(
        navController = navController,
        authViewModel     = authViewModel,
        prefsViewModel    = prefsViewModel,
        onInstagramClick = {}
    )
}