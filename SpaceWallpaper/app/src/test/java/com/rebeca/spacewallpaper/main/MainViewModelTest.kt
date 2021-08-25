package com.rebeca.spacewallpaper.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.rebeca.spacewallpaper.MainCoroutineRule
import com.rebeca.spacewallpaper.data.FakeFavoritesRepository
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class MainViewModelTest : TestCase() {
    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var favoritesRepository: FakeFavoritesRepository
    private lateinit var pictureOfDayRepository: FakeFavoritesRepository

    private lateinit var mainViewModel: MainViewModel

    // todo check if it gets same picture in the same day
    // todo check if it gets different picture to differente day
    // todo check if it returns error if picture is null

    // todo save favorite but picture of day is null
    // todo save favorite but status is LOADING
    // todo save favorite but status is ERROR
    // todo save favorite but is repeated

    // todo download image but picture is null
    // todo download image but picture is LOADING
    // todo download image but picture is ERROR
    // todo donwload success shows toast
}b