package com.example.echojournal.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.echojournal.data.remote.model.JournalEntry
import com.example.echojournal.ui.components.mainflow.settingsScreen.SettingType
import com.example.echojournal.ui.screens.authflow.SignInScreen
import com.example.echojournal.ui.screens.authflow.SignUpScreen
import com.example.echojournal.ui.screens.mainflow.AddEntryScreen
import com.example.echojournal.ui.screens.mainflow.EntryDetailScreen
import com.example.echojournal.ui.screens.mainflow.EntryListScreen
import com.example.echojournal.ui.screens.mainflow.SettingDetailScreen
import com.example.echojournal.ui.screens.mainflow.SettingsScreen
import com.example.echojournal.ui.screens.mainflow.StatisticsScreen
import com.example.echojournal.ui.screens.onboardingflow.OnboardingFlow
import com.example.echojournal.ui.viewModel.AuthViewModel
import com.example.echojournal.ui.viewModel.PrefsViewModel

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    prefsViewModel: PrefsViewModel,
    onInstagramClick: () -> Unit = {}
) {
    // User- und Onboard-State abholen
    val user by authViewModel.user.collectAsState()
    val userLoading by authViewModel.loading.collectAsState()
    val onboarded by prefsViewModel.onboarded.collectAsState()
    val prefsLoading by prefsViewModel.loading.collectAsState()

    // Einmal-Flag, damit wir den Routing-Code nur beim ersten Composable-Aufbau ausführen
    var hasNavigatedOnce by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(user, onboarded, userLoading, prefsLoading) {
        if (!hasNavigatedOnce && !userLoading && !prefsLoading && onboarded != null) {
            when {
                user == null -> {
                    navController.navigate(AuthRootRoute.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
                onboarded == false -> {
                    navController.navigate(OnboardingFlowRoute.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
                else -> {
                    navController.navigate(MainRootRoute.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            hasNavigatedOnce = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = AuthRootRoute.route
    ) {
        // AUTH FLOW
        navigation(
            startDestination = SignInRoute.route,
            route = AuthRootRoute.route
        ) {
            composable(SignInRoute.route) {
                SignInScreen(
                    onSignedIn = {
                        navController.navigate(EntryListRoute.route) {
                            popUpTo(AuthRootRoute.route) { inclusive = true }
                        }
                    },
                    onSignUpClick = { navController.navigate(SignUpRoute.route) }
                )
            }
            composable(SignUpRoute.route) {
                SignUpScreen(
                    onSignedUp = {
                        // nach Signup flaggen, dass Onboarding nötig ist
                        prefsViewModel.setOnboarded(false)
                        navController.navigate(OnboardingFlowRoute.route) {
                            popUpTo(AuthRootRoute.route) { inclusive = true }
                        }
                    },
                    onSignInClick = { navController.navigate(SignInRoute.route) }
                )
            }
        }
        // ONBOARDING FLOW

        composable(OnboardingFlowRoute.route) {
            OnboardingFlow(
                prefsViewModel = prefsViewModel,
                onComplete = {
                    navController.navigate(EntryListRoute.route) {
                        popUpTo(OnboardingFlowRoute.route) { inclusive = true }
                    }
                }
            )
        }

        // MAIN FLOW
        navigation(
            startDestination = EntryListRoute.route,
            route = MainRootRoute.route         // ← hier definieren
        ) {
            // Eintragsliste
            typedComposable<EntryListRoute> {
                EntryListScreen(
                    onEntryClick = { entry ->
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("entry", entry)
                        navController.navigateTo(EntryDetailRoute)
                    },
                    onAddClick = { navController.navigate(AddEntryRoute.route) },
                    onSettingsClick = {
                        navController.navigateTo(SettingsRoute)
                    },
                    onStatsClick = {
                        navController.navigate(StatisticsRoute.route)
                    },
                    navController = navController
                )
            }

            // Add-Entry-Screen
            composable(AddEntryRoute.route) {
                AddEntryScreen(
                    onDismiss = { navController.popBackStack() },
                    navController = navController
                )
            }

            // JournalEntry-Detail
            typedComposable<EntryDetailRoute> {
                val entry = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<JournalEntry>("entry")
                if (entry != null) {
                    EntryDetailScreen(
                        entry = entry,
                        onDismiss = { navController.popBackStack() })
                }
            }

            // Settings-Übersicht
            typedComposable<SettingsRoute> {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onNavigateToProfile = { type ->
                        navController.navigate(SettingDetailRoute.createRoute(type))
                    },
                    onNavigateToAppSetting = { type ->
                        navController.navigate(SettingDetailRoute.createRoute(type))
                    },
                    onInstagramClick = onInstagramClick,
                    onLogoutConfirmed = {
                        navController.navigate(AuthRootRoute.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            // Detail-View für jede Setting-Unterseite für jede Setting-Unterseite
            composable(
                route = SettingDetailRoute.route,
                arguments = listOf(navArgument("type") { type = NavType.StringType })
            ) { backStackEntry ->
                val typeName = backStackEntry.arguments?.getString("type")!!
                val type = SettingType.valueOf(typeName)
                SettingDetailScreen(
                    type = type,
                    onBack = { navController.popBackStack() }
                )
            }

            // Statistics-Screen
            typedComposable<StatisticsRoute> {
                StatisticsScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
