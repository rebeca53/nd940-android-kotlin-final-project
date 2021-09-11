package com.rebeca.spacewallpaper.data.local.preferences

import androidx.room.ColumnInfo
import androidx.room.Entity

import androidx.room.PrimaryKey


@Entity(tableName = "preferences")
data class PreferencesDTO (

    @ColumnInfo(name = "enable")
    var enable: Boolean = false,

    @ColumnInfo(name = "frequency")
    var frequency: Long = 1L,

    @ColumnInfo(name = "hour")
    var hour: Int = 0,

    @ColumnInfo(name = "minute")
    var minute: Int = 0,

    @ColumnInfo(name = "confirmBeforeApply")
    var confirmBeforeApply: Boolean = false,

    @PrimaryKey
    @ColumnInfo(name = "entry_id")
    val id: Int = 0
)