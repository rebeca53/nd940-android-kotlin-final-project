package com.rebeca.spacewallpaper.data.local

import androidx.room.*

/**
 * Data Access Object for the favortes table.
 */
@Dao
interface FavoritesDAO {
    /**
     * @return all reminders.
     */
    @Query("SELECT * FROM favorites")
    suspend fun getFavorites(): List<FavoriteDTO>

    /**
     * @param favoriteId the id of the favorite
     * @return the favorite object with the reminderId
     */
    @Query("SELECT * FROM favorites where entry_id = :favoriteId")
    suspend fun getFavoriteById(favoriteId: String): FavoriteDTO?

    /**
     * Insert a favorite space image in the database. If the favorite already exists, replace it.
     *
     * @param favoriteDTO the favorite space image to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavorite(favoriteDTO: FavoriteDTO)

    /**
     * Update a favorite space image in the database.
     *
     * @param favoriteDTO the favorite space image to be updated.
     */
    @Update
    suspend fun updateFavorite(favoriteDTO: FavoriteDTO)

    /**
     * Delete all favorites.
     */
    @Query("DELETE FROM favorites")
    suspend fun deleteAllFavorites()
}