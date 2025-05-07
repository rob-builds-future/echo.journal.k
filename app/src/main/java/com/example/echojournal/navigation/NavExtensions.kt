package com.example.echojournal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

inline fun <reified R> NavGraphBuilder.composable(
    noinline content: @Composable (R) -> Unit
) where R : NavDestination {
    val routeObj = R::class.objectInstance!!
    composable(routeObj.route) { content(routeObj) }
}

fun NavController.navigate(destination: NavDestination) {
    navigate(destination.route)
}
