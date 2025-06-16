package com.example.echojournal

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.echojournal.ui.theme.EchojournalTheme
import com.example.echojournal.ui.theme.FontStyle

private const val DATASTORE_NAME = "user_prefs"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATASTORE_NAME
)

class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EchojournalTheme(dynamicColor = false, fontStyle = FontStyle.VARELA) {
                    AppStart()
            }
        }
    }
}
