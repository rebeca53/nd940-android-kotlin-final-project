package com.rebeca.spacewallpaper.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.rebeca.spacewallpaper.data.local.pictureofday.PictureOfDayDTO
import com.rebeca.spacewallpaper.data.local.pictureofday.PictureOfDayLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class PictureOfDayLocalRepositoryTest {
    private lateinit var pictureOfDayLocalRepository: PictureOfDayLocalRepository
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

        pictureOfDayLocalRepository =
            PictureOfDayLocalRepository(database.spaceImageDAO(), Dispatchers.Main)
    }

    @After
    fun cleanup() {
        database.close()
        stopKoin()
    }

    @Test
    fun refreshPicture_getSamePicture() = runBlocking {
        // given
        val picture = PictureOfDayDTO(
            "Type1",
            "Title1",
            "Url1",
            "HDUrl1",
            "Explanation",
            "")
        database.spaceImageDAO().savePictureOfDay(picture)
        pictureOfDayLocalRepository.savePictureOfDay(picture)

        // when
        pictureOfDayLocalRepository.refreshPictureOfDay()

        // then
        val result = pictureOfDayLocalRepository.getPictureOfDay()
        result as RequestResult.Success
        assertThat(result.data.id, `is`(picture.id))
    }

    @Test
    fun savePicture_getPicture() = runBlocking {
        // given
        val picture = PictureOfDayDTO(
            "Type1",
            "Title1",
            "Url1",
            "HDUrl1",
            "Explanation",
            "")
        database.spaceImageDAO().savePictureOfDay(picture)
        pictureOfDayLocalRepository.savePictureOfDay(picture)

        // when
        val result = pictureOfDayLocalRepository.getPictureOfDay()

        // then
        result as RequestResult.Success
        assertThat(result.data.id, CoreMatchers.`is`(picture.id))
        MatcherAssert.assertThat(result.data.mediaType, CoreMatchers.`is`(picture.mediaType))
        MatcherAssert.assertThat(result.data.title, CoreMatchers.`is`(picture.title))
        MatcherAssert.assertThat(result.data.url, CoreMatchers.`is`(picture.url))
        MatcherAssert.assertThat(result.data.hdurl, CoreMatchers.`is`(picture.hdurl))
        MatcherAssert.assertThat(result.data.explanation, CoreMatchers.`is`(picture.explanation))
        MatcherAssert.assertThat(result.data.filepath, CoreMatchers.`is`(picture.filepath))
    }

    @Test
    fun updatePicture_getPicture() = runBlocking {
        // given
        val picture = PictureOfDayDTO(
            "Type1",
            "Title1",
            "Url1",
            "HDUrl1",
            "Explanation",
            "")
        database.spaceImageDAO().savePictureOfDay(picture)
        pictureOfDayLocalRepository.savePictureOfDay(picture)

        // when
        picture.filepath = "path"
        database.spaceImageDAO().updatePictureOfDay(picture)
        pictureOfDayLocalRepository.updatePictureOfDay(picture)
        val result = pictureOfDayLocalRepository.getPictureOfDay()

        // then
        result as RequestResult.Success
        assertThat(result.data.id, CoreMatchers.`is`(picture.id))
        MatcherAssert.assertThat(result.data.mediaType, CoreMatchers.`is`(picture.mediaType))
        MatcherAssert.assertThat(result.data.title, CoreMatchers.`is`(picture.title))
        MatcherAssert.assertThat(result.data.url, CoreMatchers.`is`(picture.url))
        MatcherAssert.assertThat(result.data.hdurl, CoreMatchers.`is`(picture.hdurl))
        MatcherAssert.assertThat(result.data.explanation, CoreMatchers.`is`(picture.explanation))
        MatcherAssert.assertThat(result.data.filepath, CoreMatchers.`is`(picture.filepath))
    }

    @Test
    fun getPicture_notFound() = runBlocking {
        //when
        val result = pictureOfDayLocalRepository.getPictureOfDay()

        // then
        result as RequestResult.Error
        assertThat(
            result.message,
            CoreMatchers.`is`("Picture Of Day not found!")
        )
    }

    @Test
    fun saveManyPictures_getOnlyOne() = runBlocking {
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
            pictureOfDayLocalRepository.savePictureOfDay(picture)
        }

        // when
        val result = pictureOfDayLocalRepository.getPictureOfDay()

        // then
        result as RequestResult.Success
        assertThat(result.data.id, CoreMatchers.`is`(picture3.id))
        MatcherAssert.assertThat(result.data.mediaType, CoreMatchers.`is`(picture3.mediaType))
        MatcherAssert.assertThat(result.data.title, CoreMatchers.`is`(picture3.title))
        MatcherAssert.assertThat(result.data.url, CoreMatchers.`is`(picture3.url))
        MatcherAssert.assertThat(result.data.hdurl, CoreMatchers.`is`(picture3.hdurl))
        MatcherAssert.assertThat(result.data.explanation, CoreMatchers.`is`(picture3.explanation))
        MatcherAssert.assertThat(result.data.filepath, CoreMatchers.`is`(picture3.filepath))
    }

    @Test
    fun deleteAll_getPicture() = runBlocking {
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
            pictureOfDayLocalRepository.savePictureOfDay(picture)
        }

        // when
        pictureOfDayLocalRepository.deleteAll()

        // then
        val result = pictureOfDayLocalRepository.getPictureOfDay()
        result as RequestResult.Error
        assertThat(
            result.message,
            CoreMatchers.`is`("Picture Of Day not found!")
        )
    }
}