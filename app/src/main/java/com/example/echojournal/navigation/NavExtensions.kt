package com.example.echojournal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


/**
 * Fügt ein `typedComposable<SomeRoute> { … }` hinzu, wobei SomeRoute ein Singleton-Objekt ist.
 */
inline fun <reified D> NavGraphBuilder.typedComposable(
    noinline content: @Composable (D) -> Unit
) where D : NavDestination {
    val destination = D::class.objectInstance
        ?: error("Route-Objekt muss als Kotlin object deklariert sein")
    composable(destination.route) {
        content(destination)
    }
}

/**
 * Extension, die ein NavDestination-Objekt in seinen .route-String auflöst.
 * Um Konflikte mit der Standard-API zu vermeiden, wurde sie `navigateTo` genannt.
 */
fun NavController.navigateTo(destination: NavDestination) {
    navigate(destination.route)
}