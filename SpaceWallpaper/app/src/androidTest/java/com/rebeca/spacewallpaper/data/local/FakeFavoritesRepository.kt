package com.rebeca.spacewallpaper.data.local

import androidx.lifecycle.MutableLiveData
import com.rebeca.spacewallpaper.data.FavoritesRepository
import com.rebeca.spacewallpaper.data.local.favorites.FavoriteDTO

class FakeFavoritesRepository: FavoritesRepository {
    private var shouldReturnError = false
    var favoriteData: LinkedHashMap<String, FavoriteDTO> = LinkedHashMap()
    private val observableFavorite = MutableLiveData<RequestResult<List<FavoriteDTO>>>()

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getFavorites(): RequestResult<List<FavoriteDTO>> {
        if (shouldReturnError) {
            return RequestResult.Error("Test Exception")
        }
        return RequestResult.Success(favoriteData.values.toList())
    }

    override suspend fun saveFavorite(favorite: FavoriteDTO) {
        favoriteData[favorite.id] = favorite
    }

    override suspend fun updateFavorite(favorite: FavoriteDTO) {
        favoriteData[favorite.id] = favorite
    }

    override suspend fun getFavorite(id: String): RequestResult<FavoriteDTO> {
        if (shouldReturnError) {
            return RequestResult.Error("Test Exception")
        }
        favoriteData[id]?.let {
            return RequestResult.Success(it)
        }
        return RequestResult.Error("Could not find favorite")
    }

    override suspend fun deleteAllFavorites() {
        favoriteData.clear()
        observableFavorite.value = getFavorites()
    }

}