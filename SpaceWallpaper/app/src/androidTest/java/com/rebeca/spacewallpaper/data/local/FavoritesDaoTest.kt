package com.rebeca.spacewallpaper.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FavoritesDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var favoritesDatabase: FavoritesDatabase

    @Before
    fun initDb() {
        favoritesDatabase = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            FavoritesDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = favoritesDatabase.close()

    @Test
    fun saveFavoriteAndGetById() = runBlockingTest {
        // given
        val favorite = FavoriteDTO(
            "Type1",
            "Title1",
            "Url1",
            "HDUrl1",
            "Explanation")
        favoritesDatabase.favoriteDao().saveFavorite(favorite)

        // when
        val loaded = favoritesDatabase.favoriteDao().getFavoriteById(favorite.id)

        // then
        assertThat(loaded as FavoriteDTO, notNullValue())
        assertThat(loaded.id, `is`(favorite.id))
        assertThat(loaded.mediaType,`is`(favorite.mediaType))
        assertThat(loaded.title, `is`(favorite.title))
        assertThat(loaded.url, `is`(favorite.url))
        assertThat(loaded.hdurl, `is`(favorite.hdurl))
        assertThat(loaded.explanation, `is`(favorite.explanation))
        assertThat(loaded.filepath, `is`(favorite.filepath))
    }

    @Test
    fun updateFavoriteAndGetById() = runBlockingTest {
        // given
        val favorite = FavoriteDTO(
            "Type1",
            "Title1",
            "Url1",
            "HDUrl1",
            "Explanation")
        favoritesDatabase.favoriteDao().saveFavorite(favorite)

        // when
        favorite.filepath = "path"
        favoritesDatabase.favoriteDao().updateFavorite(favorite)
        val loaded = favoritesDatabase.favoriteDao().getFavoriteById(favorite.id)

        // then
        assertThat(loaded as FavoriteDTO, notNullValue())
        assertThat(loaded.id, `is`(favorite.id))
        assertThat(loaded.mediaType,`is`(favorite.mediaType))
        assertThat(loaded.title, `is`(favorite.title))
        assertThat(loaded.url, `is`(favorite.url))
        assertThat(loaded.hdurl, `is`(favorite.hdurl))
        assertThat(loaded.explanation, `is`(favorite.explanation))
        assertThat(loaded.filepath, `is`("path"))
    }

    @Test
    fun saveManyFavoritesAndGetAll() = runBlockingTest {
        // given
        val favorite1 = FavoriteDTO(
            "Type1",
            "Title1",
            "Url1",
            "HDUrl1",
            "Explanation1")
        val favorite2 = FavoriteDTO(
            "Type2",
            "Title2",
            "Url2",
            "HDUrl2",
            "Explanation2")
        val favorite3 = FavoriteDTO(
            "Type3",
            "Title3",
            "Url3",
            "HDUrl3",
            "Explanation3")
        val listFavorites = arrayListOf(favorite1, favorite2, favorite3)
        for (favorite in listFavorites) {
            favoritesDatabase.favoriteDao().saveFavorite(favorite)
        }

        // when
        val allFavorites = favoritesDatabase.favoriteDao().getFavorites()

        // then
        for (index in 0..allFavorites.lastIndex) {
            val loaded = allFavorites[index]
            val favorite = listFavorites[index]
            assertThat(loaded, notNullValue())
            assertThat(loaded.id, `is`(favorite.id))
            assertThat(loaded.mediaType,`is`(favorite.mediaType))
            assertThat(loaded.title, `is`(favorite.title))
            assertThat(loaded.url, `is`(favorite.url))
            assertThat(loaded.hdurl, `is`(favorite.hdurl))
            assertThat(loaded.explanation, `is`(favorite.explanation))
            assertThat(loaded.filepath, `is`(favorite.filepath))
        }
    }

    @Test
    fun deleteAll() = runBlockingTest {
        // given
        val favorite1 = FavoriteDTO(
            "Type1",
            "Title1",
            "Url1",
            "HDUrl1",
            "Explanation1")
        val favorite2 = FavoriteDTO(
            "Type2",
            "Title2",
            "Url2",
            "HDUrl2",
            "Explanation2")
        val favorite3 = FavoriteDTO(
            "Type3",
            "Title3",
            "Url3",
            "HDUrl3",
            "Explanation3")
        val listFavorites = arrayListOf(favorite1, favorite2, favorite3)
        for (favorite in listFavorites) {
            favoritesDatabase.favoriteDao().saveFavorite(favorite)
        }

        val allFavorites = favoritesDatabase.favoriteDao().getFavorites()
        assertThat(allFavorites, notNullValue())

        // when
        favoritesDatabase.favoriteDao().deleteAllFavorites()

        // then
        val remainingFavorite = favoritesDatabase.favoriteDao().getFavorites()
        assertThat(remainingFavorite.isEmpty(), `is`(true))
    }

    @Test
    fun getFavorites_noData() = runBlockingTest {
        val remainingFavorite = favoritesDatabase.favoriteDao().getFavorites()
        assertThat(remainingFavorite.isEmpty(), `is`(true))
    }
}