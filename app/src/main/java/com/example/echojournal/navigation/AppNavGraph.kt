package com.example.echojournal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.echojournal.data.local.JournalEntry
import com.example.echojournal.ui.screens.EntryDetailScreen
import com.example.echojournal.ui.screens.EntryListScreen
import com.example.echojournal.ui.screens.SettingsScreen
import com.example.echojournal.navigation.navigate


@Composable
fun AppNavGraph(
    navController: NavHostController,
    onLogoutConfirmed: () -> Unit = {},
    onInstagramClick: () -> Unit = {}
) {
    NavHost(navController, startDestination = EntryListRoute.route) {
        composable<EntryListRoute> {
            EntryListScreen(
                onEntryClick = { entry ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("entry", entry)
                    navController.navigate(EntryDetailRoute.route)
                },
                onSettingsClick = {
                    navController.navigate(SettingsRoute.route)
                }
            )
        }
        composable<SettingsRoute> {
            SettingsScreen(
                onNavigateToProfile    = { /*…*/ },
                onNavigateToAppSetting = { /*…*/ },
                onInstagramClick       = onInstagramClick,
                onLogoutConfirmed      = {
                    onLogoutConfirmed()
                    navController.popBackStack()
                }
            )
        }
        composable<EntryDetailRoute> {
            val entry = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<JournalEntry>("entry")
            if (entry != null) {
                EntryDetailScreen(entry = entry)
            }
        }
    }
}
