package com.example.sorteadordebingo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Session")
data class Session(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "session_id") val sessionId: Long = 0L,
    @ColumnInfo(name = "session_theme") val sessionTheme: Int,
    @ColumnInfo(name = "drawn_elements") val drawnElements: String = "",
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false
)