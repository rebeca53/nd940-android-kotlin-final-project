package com.rebeca.spacewallpaper.data

import androidx.lifecycle.MutableLiveData
import com.rebeca.spacewallpaper.R
import com.rebeca.spacewallpaper.data.local.RequestResult
import com.rebeca.spacewallpaper.data.local.favorites.FavoriteDTO
import kotlinx.coroutines.runBlocking

class FakeFavoritesRepository: FavoritesRepository {
    companion object {
        const val EXCEPTION_MESSAGE = "Test Exception"
    }
    private var shouldReturnError = false
    var favoritesData: LinkedHashMap<String, FavoriteDTO> = LinkedHashMap()
    private val observableFavorites = MutableLiveData<RequestResult<List<FavoriteDTO>>>()

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getFavorites(): RequestResult<List<FavoriteDTO>> {
        if (shouldReturnError) {
            return RequestResult.Error(EXCEPTION_MESSAGE)
        }
        return RequestResult.Success(favoritesData.values.toList())
    }

    override suspend fun saveFavorite(favorite: FavoriteDTO) {
        favoritesData[favorite.id] = favorite
    }

    override suspend fun updateFavorite(favorite: FavoriteDTO) {
        favoritesData[favorite.id] = favorite
    }

    override suspend fun getFavorite(id: String): RequestResult<FavoriteDTO> {
        if (shouldReturnError) {
            return RequestResult.Error(EXCEPTION_MESSAGE)
        }
        favoritesData[id]?.let {
            return RequestResult.Success(it)
        }
        return RequestResult.Error(FavoritesRepository.NOT_FOUND)
    }

    override suspend fun deleteAllFavorites() {
        favoritesData.clear()
        observableFavorites.value = getFavorites()
    }

    fun addFavorites(vararg favorites: FavoriteDTO) {
        for (favorite in favorites) {
            favoritesData[favorite.id] = favorite
        }
        runBlocking {
            observableFavorites.value = getFavorites()
        }
    }

}