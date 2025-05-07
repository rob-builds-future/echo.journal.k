package com.example.echojournal

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.echojournal.navigation.AppNavGraph

@Composable
fun AppStart() {
    val navController: NavHostController = rememberNavController()
    AppNavGraph(
        navController = navController,
        onLogoutConfirmed = {},
        onInstagramClick = {}
    )
}