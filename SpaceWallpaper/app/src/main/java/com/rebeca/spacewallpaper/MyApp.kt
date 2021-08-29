package com.rebeca.spacewallpaper

import android.app.Application
import com.rebeca.spacewallpaper.data.FavoritesRepository
import com.rebeca.spacewallpaper.data.PictureOfDayRepository
import com.rebeca.spacewallpaper.data.PreferencesRepository
import com.rebeca.spacewallpaper.data.local.LocalDB
import com.rebeca.spacewallpaper.main.MainViewModel
import com.rebeca.spacewallpaper.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp : Application() {
    private val favoritesRepository: FavoritesRepository
        get() = ServiceLocator.provideFavoritesRepository(this)

    private val pictureOfDayRepository: PictureOfDayRepository
        get() = ServiceLocator.providePictureOfDayRepository(this)

    private val preferencesRepository: PreferencesRepository
        get() = ServiceLocator.providePreferencesRepository(this)

    override fun onCreate() {
        super.onCreate()

        val myModule = module {
            viewModel {
                MainViewModel(
                    get(),
                    favoritesRepository,
                    pictureOfDayRepository
                )
            }
            single {
                SettingsViewModel(
                    get(),
                    preferencesRepository
                )
            }
            single { preferencesRepository }
            single { favoritesRepository }
            single { LocalDB.getSpaceImageDatabase(this@MyApp)}
            single { LocalDB.getPreferencesDatabase(this@MyApp)}
        }

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(myModule))
        }
    }
}