package com.rebeca.spacewallpaper.data.local.pictureofday

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Immutable model class for a space image of the day. In order to compile with Room
 *
 * @param mediaType     media type of the space image
 * @param title         title of the space image
 * @param url           url of the space image
 * @param hdurl         hdurl name of the space image
 * @param explanation   explanation of the space image
 * @param filepath      internal storage path of the space image
 * @param id            id of the space image
 */@Entity(tableName = "pictureofday")
data class
PictureOfDayDTO (
    @ColumnInfo(name = "mediaType") var mediaType: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "url") var url: String,
    @ColumnInfo(name = "hdurl") var hdurl: String,
    @ColumnInfo(name = "explanation") var explanation: String,
    @ColumnInfo(name = "filepath") var filepath: String = "",
    @PrimaryKey @ColumnInfo(name = "entry_id") val id: String = UUID.randomUUID().toString()
)