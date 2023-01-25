package com.example.sorteadordebingo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "element",
    foreignKeys = [ForeignKey(
        entity = Theme::class,
        childColumns = ["theme"],
        parentColumns = ["id"]
    )]
)
data class Element (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "picture") val picture: String,
    @ColumnInfo(name = "draw") val draw: Boolean = false,
    @ColumnInfo(name = "theme") val theme: Long
)