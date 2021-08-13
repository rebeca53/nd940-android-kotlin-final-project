package com.rebeca.spacewallpaper.data

import com.rebeca.spacewallpaper.data.local.favorites.FavoriteDTO
import com.rebeca.spacewallpaper.data.local.RequestResult

/**
 * Main entry point for accessing favorites data.
 */
interface FavoritesRepository {
    suspend fun getFavorites(): RequestResult<List<FavoriteDTO>>
    suspend fun saveFavorite(favorite: FavoriteDTO)
    suspend fun updateFavorite(favorite: FavoriteDTO)
    suspend fun getFavorite(id: String): RequestResult<FavoriteDTO>
    suspend fun deleteAllFavorites()
}