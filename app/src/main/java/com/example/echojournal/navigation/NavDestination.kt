package com.example.echojournal.navigation

import com.example.echojournal.ui.components.mainflow.settingsScreen.SettingType
import kotlinx.serialization.Serializable

/** Marker-Interface für alle Routen; jedes Objekt liefert seinen eigenen [route]-String. */
interface NavDestination {
    val route: String
}

@Serializable object MainRootRoute       : NavDestination { override val route = "main_root" }
@Serializable object EntryListRoute      : NavDestination { override val route = "entry_list" }
@Serializable object SettingsRoute       : NavDestination { override val route = "settings" }
@Serializable object SettingDetailRoute  : NavDestination {
    override val route = "setting/{type}"
    fun createRoute(type: SettingType) = "setting/${type.name}"
}
@Serializable object EntryDetailRoute    : NavDestination { override val route = "entry_detail" }

@Serializable object AuthRootRoute       : NavDestination { override val route = "auth_root" }
@Serializable object SignInRoute         : NavDestination { override val route = "sign_in" }
@Serializable object SignUpRoute         : NavDestination { override val route = "sign_up" }

@Serializable object OnboardingRootRoute : NavDestination { override val route = "onboarding_root" }
@Serializable object WelcomeRoute        : NavDestination { override val route = "welcome" }
@Serializable object PrefsRoute          : NavDestination { override val route = "prefs_setup" }
