package com.example.sorteadordebingo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Element",
    foreignKeys = [ForeignKey(
        entity = Theme::class,
        childColumns = ["element_theme"],
        parentColumns = ["theme_id"]
    )]
)
data class Element (
    @PrimaryKey(autoGenerate = false) val element_id: Long,
    @ColumnInfo val element_name: String = "",
    @ColumnInfo val element_picture: String = "",
    @ColumnInfo val element_draw: Int = 0,
    @ColumnInfo val element_theme: Long = 0
)