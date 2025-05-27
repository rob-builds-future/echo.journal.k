package com.example.echojournal.ui.components.mainflow.settingsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun InstaButton() {
    // Der URI‐Handler für In‐App Links
    val uriHandler = LocalUriHandler.current
    // Deine Instagram‐Profile-URL
    val instagramUrl = "https://www.instagram.com/yourecho.app/"

    AsyncImage(
        model = "https://i.pinimg.com/736x/52/96/e9/5296e9c8db60be700e77d029fe9bfe8b.jpg",
        contentDescription = "Instagram Button",
        modifier = Modifier
            .size(40.dp)
            .clickable {
                // beim Klick öffnet sich der Browser bzw. die Instagram App
                uriHandler.openUri(instagramUrl)
            }
    )
}