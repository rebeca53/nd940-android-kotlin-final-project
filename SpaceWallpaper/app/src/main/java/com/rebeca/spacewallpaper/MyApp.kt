package com.rebeca.spacewallpaper

import android.app.Application
import com.rebeca.spacewallpaper.data.FavoritesRepository
import com.rebeca.spacewallpaper.data.PictureOfDayRepository
import com.rebeca.spacewallpaper.data.local.LocalDB
import com.rebeca.spacewallpaper.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp : Application() {
    private val favoritesRepository: FavoritesRepository
        get() = ServiceLocator.provideFavoritesRepository(this)

    private val pictureOfDayRepository: PictureOfDayRepository
        get() = ServiceLocator.providePictureOfDayRepository(this)

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
            single { favoritesRepository }
            single { LocalDB.getSpaceImageDatabase(this@MyApp)}
        }

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(myModule))
        }
    }
}