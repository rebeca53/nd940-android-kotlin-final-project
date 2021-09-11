package com.rebeca.spacewallpaper.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.rebeca.spacewallpaper.data.local.favorites.FavoriteDTO
import com.rebeca.spacewallpaper.data.local.pictureofday.PictureOfDayDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class SpaceImageDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var spaceImageDatabase: SpaceImageDatabase

    @Before
    fun initDb() {
        spaceImageDatabase = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            SpaceImageDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = spaceImageDatabase.close()

    @Test
    fun saveFavoriteAndGetById() = runBlockingTest {
        // given
        val favorite = FavoriteDTO(
            "Type1",
            "Title1",
            "Url1",
            "HDUrl1",
            "Explanation")
        spaceImageDatabase.spaceImageDAO().saveFavorite(favorite)

        // when
        val loaded = spaceImageDatabase.spaceImageDAO().getFavoriteById(favorite.id)

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
        spaceImageDatabase.spaceImageDAO().saveFavorite(favorite)

        // when
        favorite.filepath = "path"
        spaceImageDatabase.spaceImageDAO().updateFavorite(favorite)
        val loaded = spaceImageDatabase.spaceImageDAO().getFavoriteById(favorite.id)

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
            spaceImageDatabase.spaceImageDAO().saveFavorite(favorite)
        }

        // when
        val allFavorites = spaceImageDatabase.spaceImageDAO().getFavorites()

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
            spaceImageDatabase.spaceImageDAO().saveFavorite(favorite)
        }

        val allFavorites = spaceImageDatabase.spaceImageDAO().getFavorites()
        assertThat(allFavorites, notNullValue())

        // when
        spaceImageDatabase.spaceImageDAO().deleteAllFavorites()

        // then
        val remainingFavorite = spaceImageDatabase.spaceImageDAO().getFavorites()
        assertThat(remainingFavorite.isEmpty(), `is`(true))
    }

    @Test
    fun getFavorites_noData() = runBlockingTest {
        val remainingFavorite = spaceImageDatabase.spaceImageDAO().getFavorites()
        assertThat(remainingFavorite.isEmpty(), `is`(true))
    }


    @Test
    fun savePicture_getPicture() = runBlockingTest {
        // given
        val picture = PictureOfDayDTO(
            "Type1",
            "Title1",
            "Url1",
            "HDUrl1",
            "Explanation",
            "")
        spaceImageDatabase.spaceImageDAO().savePictureOfDay(picture)
        // when
        val loaded = spaceImageDatabase.spaceImageDAO().getPictureOfDay()

        // then
        assertThat(loaded as PictureOfDayDTO, notNullValue())
        assertThat(loaded.id, `is`(picture.id))
        assertThat(loaded.mediaType,`is`(picture.mediaType))
        assertThat(loaded.title, `is`(picture.title))
        assertThat(loaded.url, `is`(picture.url))
        assertThat(loaded.hdurl, `is`(picture.hdurl))
        assertThat(loaded.explanation, `is`(picture.explanation))
        assertThat(loaded.filepath, `is`(picture.filepath))
    }

    @Test
    fun updatePicture_getPicture() = runBlockingTest {
        // given
        val picture = PictureOfDayDTO(
            "Type1",
            "Title1",
            "Url1",
            "HDUrl1",
            "Explanation",
            "")
        spaceImageDatabase.spaceImageDAO().savePictureOfDay(picture)

        // when
        picture.filepath = "path"
        spaceImageDatabase.spaceImageDAO().updatePictureOfDay(picture)
        val loaded = spaceImageDatabase.spaceImageDAO().getPictureOfDay()

        // then
        assertThat(loaded as PictureOfDayDTO, notNullValue())
        assertThat(loaded.id, `is`(picture.id))
        assertThat(loaded.mediaType,`is`(picture.mediaType))
        assertThat(loaded.title, `is`(picture.title))
        assertThat(loaded.url, `is`(picture.url))
        assertThat(loaded.hdurl, `is`(picture.hdurl))
        assertThat(loaded.explanation, `is`(picture.explanation))
        assertThat(loaded.filepath, `is`(picture.filepath))
    }

    @Test
    fun getPictures_noData() = runBlockingTest {
        //when
        val result = spaceImageDatabase.spaceImageDAO().getPictureOfDay()

        // then
        assertThat(result, nullValue())
    }

    @Test
    fun saveManyPictures_getOnlyOne() = runBlockingTest {
        // given
        val picture1 = PictureOfDayDTO(
            "Type1",
            "Title1",
            "Url1",
            "HDUrl1",
            "Explanation",
            "")
        val picture2 = PictureOfDayDTO(
            "Type2",
            "Title2",
            "Url2",
            "HDUrl2",
            "Explanation",
            "")
        val picture3 = PictureOfDayDTO(
            "Type3",
            "Title3",
            "Url3",
            "HDUrl3",
            "Explanation",
            "")

        val listPictures = arrayListOf(picture1, picture2, picture3)
        for (picture in listPictures) {
            spaceImageDatabase.spaceImageDAO().savePictureOfDay(picture)
        }

        // when
        val loaded = spaceImageDatabase.spaceImageDAO().getPictureOfDay()

        // then - it will get the first, because it is not deleting previous picture of day like the repository.
        assertThat(loaded as PictureOfDayDTO, notNullValue())
        assertThat(loaded.id, `is`(picture1.id))
        assertThat(loaded.mediaType,`is`(picture1.mediaType))
        assertThat(loaded.title, `is`(picture1.title))
        assertThat(loaded.url, `is`(picture1.url))
        assertThat(loaded.hdurl, `is`(picture1.hdurl))
        assertThat(loaded.explanation, `is`(picture1.explanation))
        assertThat(loaded.filepath, `is`(picture1.filepath))
    }

    @Test
    fun deletePictureOfDay() = runBlockingTest {
        // given
        val picture1 = PictureOfDayDTO(
            "Type1",
            "Title1",
            "Url1",
            "HDUrl1",
            "Explanation",
            "")
        val picture2 = PictureOfDayDTO(
            "Type2",
            "Title2",
            "Url2",
            "HDUrl2",
            "Explanation",
            "")
        val picture3 = PictureOfDayDTO(
            "Type3",
            "Title3",
            "Url3",
            "HDUrl3",
            "Explanation",
            "")

        val listPictures = arrayListOf(picture1, picture2, picture3)
        for (picture in listPictures) {
            spaceImageDatabase.spaceImageDAO().savePictureOfDay(picture)
        }

        // when
        spaceImageDatabase.spaceImageDAO().deletePictureOfDay()

        // then
        val result = spaceImageDatabase.spaceImageDAO().getPictureOfDay()
        assertThat(result, nullValue())
    }

}