package com.rbf.echojournal.di

import com.rbf.echojournal.data.remote.BASE_URL
import com.rbf.echojournal.data.remote.LibreTranslateService
import com.rbf.echojournal.data.repository.JournalRepo
import com.rbf.echojournal.data.repository.JournalRepoImpl
import com.rbf.echojournal.data.repository.LanguageRepo
import com.rbf.echojournal.data.repository.LanguageRepoImpl
import com.rbf.echojournal.data.repository.PrefsRepo
import com.rbf.echojournal.data.repository.PrefsRepoImpl
import com.rbf.echojournal.data.repository.TranslationApiRepo
import com.rbf.echojournal.data.repository.TranslationApiRepoImpl
import com.rbf.echojournal.data.repository.UserAuthRepo
import com.rbf.echojournal.data.repository.UserAuthRepoImpl
import com.rbf.echojournal.ui.viewModel.AuthViewModel
import com.rbf.echojournal.ui.viewModel.EntryViewModel
import com.rbf.echojournal.ui.viewModel.LanguageViewModel
import com.rbf.echojournal.ui.viewModel.PrefsViewModel
import com.rbf.echojournal.ui.viewModel.StatisticsViewModel
import com.rbf.echojournal.ui.viewModel.TranslationViewModel
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
    viewModel { TranslationViewModel(get(), get()) }

    single<LanguageRepo> { LanguageRepoImpl(get()) }
    viewModel { LanguageViewModel(get()) }

    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<UserAuthRepo> { UserAuthRepoImpl(get(), get()) }
    viewModel { AuthViewModel(get(), get(), get())}

    single<PrefsRepo> { PrefsRepoImpl(androidContext(), get()) }
    viewModel { PrefsViewModel(androidContext(), get()) }

    single<FirebaseFirestore> { FirebaseFirestore.getInstance() }
    single<JournalRepo> { JournalRepoImpl(get()) }
    viewModel { EntryViewModel(get(), get(), get()) }

    viewModel { StatisticsViewModel(get()) }

}