package com.example.echojournal.navigation

import com.example.echojournal.util.SettingType
import kotlinx.serialization.Serializable

/** Marker-Interface für alle Routen; jedes Objekt liefert seinen eigenen [route]-String. */
interface NavDestination {
    val route: String
}

@Serializable
object EntryListRoute : NavDestination {
    override val route = "entry_list"
}

@Serializable
object SettingsRoute : NavDestination {
    override val route = "settings"
}

@Serializable
object SettingDetailRoute : NavDestination {
    override val route = "setting/{type}"

    fun createRoute(type: SettingType) = "setting/${type.name}"
}

@Serializable
object EntryDetailRoute : NavDestination {
    override val route = "entry_detail"
}
