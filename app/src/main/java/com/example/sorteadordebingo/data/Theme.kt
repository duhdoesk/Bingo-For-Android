package com.example.sorteadordebingo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import javax.annotation.Nonnull

@Entity(tableName = "Theme")
data class Theme (
    @PrimaryKey(autoGenerate = false) val theme_id: Long,
    @ColumnInfo val theme_name: String = "",
    @ColumnInfo val theme_picture: String = ""
)