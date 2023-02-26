package com.example.sorteadordebingo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Element")
data class Element(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "element_id") val elementId: Int,
    @ColumnInfo(name = "element_name") val elementName: String = "",
    @ColumnInfo(name = "element_picture") val elementPicture: String = "",
    @ColumnInfo(name = "element_theme") val elementTheme: Int = 0
)