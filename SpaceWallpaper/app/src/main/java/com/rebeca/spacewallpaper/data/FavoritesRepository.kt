package com.rebeca.spacewallpaper.data

import com.rebeca.spacewallpaper.data.local.favorites.FavoriteDTO
import com.rebeca.spacewallpaper.data.local.RequestResult

/**
 * Main entry point for accessing favorites data.
 */
interface FavoritesRepository {
    companion object {
        const val NOT_FOUND = "Favorite space image not found!"
        const val ALREADY_IN_DATABASE = "Favorite is already in base! Don't save."
    }
    suspend fun getFavorites(): RequestResult<List<FavoriteDTO>>
    suspend fun saveFavorite(favorite: FavoriteDTO)
    suspend fun updateFavorite(favorite: FavoriteDTO)
    suspend fun getFavorite(id: String): RequestResult<FavoriteDTO>
    suspend fun deleteAllFavorites()
}