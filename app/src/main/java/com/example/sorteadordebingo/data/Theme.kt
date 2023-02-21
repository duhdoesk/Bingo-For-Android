package com.example.sorteadordebingo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Theme")
data class Theme(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "theme_id") val themeId: Long,
    @ColumnInfo(name = "theme_name") val themeName: String = "",
    @ColumnInfo(name = "theme_picture") val themePicture: String = ""
)