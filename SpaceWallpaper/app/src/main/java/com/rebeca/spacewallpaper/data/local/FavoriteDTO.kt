package com.rebeca.spacewallpaper.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Immutable model class for a Favorite space image. In order to compile with Room
 *
 * @param mediaType     media type of the favorite space image
 * @param title         title of the favorite space image
 * @param url           url of the favorite space image
 * @param hdurl         hdurl name of the favorite space image
 * @param explanation   explanation of the favorite space image
 * @param filepath      internal storage path of the favorite space image
 * @param id            id of the favorite space image
 */@Entity(tableName = "favorites")
data class
FavoriteDTO (
    @ColumnInfo(name = "mediaType") var mediaType: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "url") var url: String,
    @ColumnInfo(name = "hdurl") var hdurl: String,
    @ColumnInfo(name = "explanation") var explanation: String,
    @ColumnInfo(name = "filepath") var filepath: String = "",
    @PrimaryKey @ColumnInfo(name = "entry_id") val id: String = UUID.randomUUID().toString()
)