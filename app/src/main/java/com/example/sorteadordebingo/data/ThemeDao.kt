package com.example.sorteadordebingo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ThemeDao {
    @Query("SELECT * FROM Theme")
    fun getThemes() : Flow<List<Theme>>
}