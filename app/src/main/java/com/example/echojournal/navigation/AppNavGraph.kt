package com.example.echojournal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.echojournal.data.local.JournalEntry
import com.example.echojournal.ui.screens.EntryDetailScreen
import com.example.echojournal.ui.screens.EntryListScreen
import com.example.echojournal.ui.screens.SettingDetailScreen
import com.example.echojournal.ui.screens.SettingsScreen
import com.example.echojournal.util.SettingType

@Composable
fun AppNavGraph(
    navController: NavHostController,
    onLogoutConfirmed: () -> Unit = {},
    onInstagramClick: () -> Unit = {}
) {
    NavHost(
        navController    = navController,
        startDestination = EntryListRoute.route
    ) {
        // 1) Eintragsliste
        typedComposable<EntryListRoute> {
            EntryListScreen(
                onEntryClick = { entry ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("entry", entry)
                    // statische Route via navigateTo
                    navController.navigateTo(EntryDetailRoute)
                },
                onSettingsClick = {
                    navController.navigateTo(SettingsRoute)
                }
            )
        }

        // 2) Settings-Übersicht
        typedComposable<SettingsRoute> {
            SettingsScreen(
                onNavigateToProfile    = { type ->
                    // dynamische Route via Standard-navigate
                    navController.navigate(SettingDetailRoute.createRoute(type))
                },
                onNavigateToAppSetting = { type ->
                    navController.navigate(SettingDetailRoute.createRoute(type))
                },
                onInstagramClick       = onInstagramClick,
                onLogoutConfirmed      = {
                    onLogoutConfirmed()
                    navController.popBackStack()
                }
            )
        }

        // 3) Detail-View für jede Setting-Unterseite für jede Setting-Unterseite
        composable(
            route = SettingDetailRoute.route,
            arguments = listOf(
                navArgument("type") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val typeName = backStackEntry.arguments?.getString("type")!!
            val type = SettingType.valueOf(typeName)
            SettingDetailScreen(
                type   = type,
                onBack = { navController.popBackStack() }
            )
        }

        // 4) JournalEntry-Detail
        typedComposable<EntryDetailRoute> {
            val entry = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<JournalEntry>("entry")
            if (entry != null) {
                EntryDetailScreen(entry = entry)
            }
        }
    }
}
