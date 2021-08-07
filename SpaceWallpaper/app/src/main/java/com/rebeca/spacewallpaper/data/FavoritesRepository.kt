package com.rebeca.spacewallpaper.data

import com.rebeca.spacewallpaper.data.local.FavoriteDTO
import com.rebeca.spacewallpaper.data.local.Result

/**
 * Main entry point for accessing favorites data.
 */
interface FavoritesRepository {
    suspend fun getFavorites(): Result<List<FavoriteDTO>>
    suspend fun saveFavorite(favorite: FavoriteDTO)
    suspend fun updateFavorite(favorite: FavoriteDTO)
    suspend fun getFavorite(id: String): Result<FavoriteDTO>
    suspend fun deleteAllFavorites()
}