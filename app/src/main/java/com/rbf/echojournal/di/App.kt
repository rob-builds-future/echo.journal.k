package com.rbf.echojournal.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

// wir überschreiben die Application
// und vermerken das auch im Manifest, damit unsere überschriebene Application verwendet wird
class App: Application() {

    override fun onCreate() {
        super.onCreate()

        // wir überschreiben Application um Code hinzuzufügen:
        // Wir starten koin, koin kümmert sich für die App um die Dependencies
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}