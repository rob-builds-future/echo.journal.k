package com.example.echojournal.di

import LanguageViewModel
import com.example.echojournal.data.remote.BASE_URL
import com.example.echojournal.data.remote.LibreTranslateService
import com.example.echojournal.data.repository.JournalRepo
import com.example.echojournal.data.repository.JournalRepoImpl
import com.example.echojournal.data.repository.LanguageRepo
import com.example.echojournal.data.repository.LanguageRepoImpl
import com.example.echojournal.data.repository.PrefsRepo
import com.example.echojournal.data.repository.PrefsRepoImpl
import com.example.echojournal.data.repository.TranslationApiRepo
import com.example.echojournal.data.repository.TranslationApiRepoImpl
import com.example.echojournal.data.repository.UserAuthRepo
import com.example.echojournal.data.repository.UserAuthRepoImpl
import com.example.echojournal.ui.viewModel.AuthViewModel
import com.example.echojournal.ui.viewModel.EntryViewModel
import com.example.echojournal.ui.viewModel.PrefsViewModel
import com.example.echojournal.ui.viewModel.TranslationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.Inet4Address
import java.net.InetAddress

/*
Dependency Injection mit Koin
1. Setup (Gradle und Manifest)
2. In Screens koinViewModel() verwenden
3. In den ViewModels die Repos als Parameter einsetzen, damit sie injectet werden können
4. Dependencies in appModule definieren -> hier werden Dependencies aufgelöst
 */

// hier werden alle dependencies verwaltet
val appModule = module {
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single<Retrofit> {
        // HTTP-Logging
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // OkHttpClient mit IPv4-only DNS-Override
        val client = OkHttpClient.Builder()
            .dns(object : Dns {
                override fun lookup(hostname: String): List<InetAddress> {
                    // Standard-Lookup
                    val all = Dns.SYSTEM.lookup(hostname)
                    // nur IPv4-Adressen filtern
                    val ipv4Only = all.filterIsInstance<Inet4Address>()
                    // zurückgeben als List<InetAddress>
                    return ipv4Only.map { it }
                }
            })
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }

    single<LibreTranslateService> { get<Retrofit>().create(LibreTranslateService::class.java) }
    single<TranslationApiRepo> { TranslationApiRepoImpl(get()) }
    viewModel { TranslationViewModel(
        translationRepository = get(),
        prefsViewModel        = get()
    ) }

    single<LanguageRepo> { LanguageRepoImpl(get()) }
    viewModel { LanguageViewModel(get()) }

    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<UserAuthRepo> { UserAuthRepoImpl(get(), get()) }
    viewModel { AuthViewModel(get(), get(), prefsViewModel = get())}

    single<PrefsRepo> { PrefsRepoImpl(androidContext()) }
    viewModel { PrefsViewModel(get()) }

    single<FirebaseFirestore> { FirebaseFirestore.getInstance() }
    single<JournalRepo> { JournalRepoImpl(db = get()) }
    viewModel { EntryViewModel(
        authViewModel       = get(),
        journalRepo         = get(),
        translationApiRepo  = get()
    ) }
}