package com.rebeca.spacewallpaper.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.rebeca.spacewallpaper.data.local.favorites.FavoriteDTO
import com.rebeca.spacewallpaper.data.local.favorites.FavoritesLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class FavoritesLocalRepositoryTest {
    private lateinit var favoriteLocalRepository: FavoritesLocalRepository
    private lateinit var database: SpaceImageDatabase

    @Before
    fun setup() {
        // using an in-memory database for testing, since it doesn't survive killing the process    }
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SpaceImageDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        favoriteLocalRepository =
            FavoritesLocalRepository(database.spaceImageDAO(), Dispatchers.Main)
    }

    @After
    fun cleanup() {
        database.close()
        stopKoin()
    }

    @Test
    fun saveFavorite_getFavorite() = runBlocking {
        // GIVEN - a new favorite saved in the database
        val favorite = FavoriteDTO(
            "Type1",
            "Title1",
            "Url1",
            "HDUrl1",
            "Explanation")
        database.spaceImageDAO().saveFavorite(favorite)
        favoriteLocalRepository.saveFavorite(favorite)

        // WHEN  - Favorite retrieved by ID
        val result = favoriteLocalRepository.getFavorite(favorite.id)

        // THEN - Same favorite is returned
        result as RequestResult.Success
        assertThat(result.data.id, `is`(favorite.id))
        assertThat(result.data.mediaType,`is`(favorite.mediaType))
        assertThat(result.data.title, `is`(favorite.title))
        assertThat(result.data.url, `is`(favorite.url))
        assertThat(result.data.hdurl, `is`(favorite.hdurl))
        assertThat(result.data.explanation, `is`(favorite.explanation))
        assertThat(result.data.filepath, `is`(favorite.filepath))
    }

    @Test
    fun updateFavorite_getFavorite() = runBlocking {
        // GIVEN - a new favorite saved in the database
        val favorite = FavoriteDTO(
            "Type1",
            "Title1",
            "Url1",
            "HDUrl1",
            "Explanation")
        database.spaceImageDAO().saveFavorite(favorite)
        favoriteLocalRepository.saveFavorite(favorite)

        // WHEN  - Favorite retrieved by ID
        favorite.filepath = "path"
        database.spaceImageDAO().updateFavorite(favorite)
        favoriteLocalRepository.updateFavorite(favorite)
        val result = favoriteLocalRepository.getFavorite(favorite.id)

        // THEN - Same favorite is returned
        result as RequestResult.Success
        assertThat(result.data.id, `is`(favorite.id))
        assertThat(result.data.mediaType,`is`(favorite.mediaType))
        assertThat(result.data.title, `is`(favorite.title))
        assertThat(result.data.url, `is`(favorite.url))
        assertThat(result.data.hdurl, `is`(favorite.hdurl))
        assertThat(result.data.explanation, `is`(favorite.explanation))
        assertThat(result.data.filepath, `is`(favorite.filepath))
    }

    @Test
    fun getFavorite_notFound() = runBlocking {
        // GIVEN - a new favorite saved in the database
        val favorite = FavoriteDTO(
            "Type1",
            "Title1",
            "Url1",
            "HDUrl1",
            "Explanation")

        // WHEN  - Favorite retrieved by ID
        val result = favoriteLocalRepository.getFavorite(favorite.id)

        // THEN - No favorite is returned
        result as RequestResult.Error
        assertThat(result.message, `is`("Favorite space image not found!"))
    }

    @Test
    fun saveManyFavorite_getAll() = runBlocking {
        // GIVEN - a new favorite saved in the database
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
            favoriteLocalRepository.saveFavorite(favorite)
        }

        // WHEN  - all favorites retrieved
        val result = favoriteLocalRepository.getFavorites()

        // THEN - Same favorite is returned
        result as RequestResult.Success
        for (index in 0..result.data.lastIndex) {
            val loaded = result.data[index]
            val favorite = listFavorites[index]
            assertThat(loaded, CoreMatchers.notNullValue())
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
    fun deleteAll_getFavorites() = runBlocking {
        // GIVEN - a new favorite saved in the database
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
            favoriteLocalRepository.saveFavorite(favorite)
        }
        val allFavorites = favoriteLocalRepository.getFavorites()
        assertThat(allFavorites, CoreMatchers.notNullValue())

        // WHEN  - Delete all
        favoriteLocalRepository.deleteAllFavorites()

        // THEN - No favorite is returned
        val result = favoriteLocalRepository.getFavorites()
        result as RequestResult.Success
        assertThat(result.data.isEmpty(), `is`(true))
    }
}