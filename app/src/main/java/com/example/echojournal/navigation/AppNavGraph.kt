package com.example.echojournal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.echojournal.data.remote.model.JournalEntry
import com.example.echojournal.ui.screens.authflow.SignInScreen
import com.example.echojournal.ui.screens.authflow.SignUpScreen
import com.example.echojournal.ui.screens.mainflow.EntryDetailScreen
import com.example.echojournal.ui.screens.mainflow.EntryListScreen
import com.example.echojournal.ui.screens.mainflow.SettingDetailScreen
import com.example.echojournal.ui.screens.mainflow.SettingsScreen
import com.example.echojournal.ui.screens.onboardingflow.PrefsSetupScreen
import com.example.echojournal.ui.screens.onboardingflow.WelcomeScreen
import com.example.echojournal.ui.viewModel.AuthViewModel
import com.example.echojournal.ui.viewModel.PrefsViewModel
import com.example.echojournal.util.SettingType

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    prefsViewModel: PrefsViewModel,
    onLogoutConfirmed: () -> Unit = {},
    onInstagramClick: () -> Unit = {}
) {
    val user by authViewModel.user.collectAsState()
    val onboarded by prefsViewModel.onboarded.collectAsState()

    NavHost(
        navController    = navController,
        startDestination = when {
            user == null        -> AuthRootRoute.route
            !onboarded          -> OnboardingRootRoute.route
            else                -> EntryListRoute.route
        }
    ) {
        // AUTH FLOW
        navigation(
            startDestination = SignInRoute.route,
            route           = AuthRootRoute.route
        ) {
            composable(SignInRoute.route) {
                SignInScreen(onSignedIn = {
                    navController.navigate(SignUpRoute.route)
                })
            }
            composable(SignUpRoute.route) {
                SignUpScreen(onSignedUp = {
                    // nach Signup flaggen, dass Onboarding nötig ist
                    prefsViewModel.setOnboarded(false)
                    navController.navigate(OnboardingRootRoute.route) {
                        popUpTo(AuthRootRoute.route) { inclusive = true }
                    }
                })
            }
        }
        // ONBOARDING FLOW
        navigation(
            startDestination = WelcomeRoute.route,
            route            = OnboardingRootRoute.route
        ) {
            composable(WelcomeRoute.route) {
                WelcomeScreen(onNext = {
                    navController.navigate(PrefsRoute.route)
                })
            }
            composable(PrefsRoute.route) {
                PrefsSetupScreen(onComplete = {
                    prefsViewModel.setOnboarded(true)
                    navController.navigate(EntryListRoute.route) {
                        popUpTo(OnboardingRootRoute.route) { inclusive = true }
                    }
                })
            }
        }
        // MAIN FLOW
        // 1) Eintragsliste
        typedComposable<EntryListRoute> {
            EntryListScreen(
                onEntryClick = { entry ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("entry", entry)
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
