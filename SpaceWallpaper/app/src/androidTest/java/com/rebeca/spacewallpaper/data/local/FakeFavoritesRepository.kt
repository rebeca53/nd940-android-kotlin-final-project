package com.rebeca.spacewallpaper.data.local

import androidx.lifecycle.MutableLiveData
import com.rebeca.spacewallpaper.data.FavoritesRepository
import com.rebeca.spacewallpaper.data.local.favorites.FavoriteDTO

class FakeFavoritesRepository: FavoritesRepository {
    private var shouldReturnError = false
    var favoriteData: LinkedHashMap<String, FavoriteDTO> = LinkedHashMap()
    private val observableFavorite = MutableLiveData<Result<List<FavoriteDTO>>>()

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getFavorites(): Result<List<FavoriteDTO>> {
        if (shouldReturnError) {
            return Result.Error("Test Exception")
        }
        return Result.Success(favoriteData.values.toList())
    }

    override suspend fun saveFavorite(favorite: FavoriteDTO) {
        favoriteData[favorite.id] = favorite
    }

    override suspend fun updateFavorite(favorite: FavoriteDTO) {
        favoriteData[favorite.id] = favorite
    }

    override suspend fun getFavorite(id: String): Result<FavoriteDTO> {
        if (shouldReturnError) {
            return Result.Error("Test Exception")
        }
        favoriteData[id]?.let {
            return Result.Success(it)
        }
        return Result.Error("Could not find favorite")
    }

    override suspend fun deleteAllFavorites() {
        favoriteData.clear()
        observableFavorite.value = getFavorites()
    }

}