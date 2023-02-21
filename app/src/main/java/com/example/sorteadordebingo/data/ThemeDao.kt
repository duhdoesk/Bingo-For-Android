package com.example.sorteadordebingo.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ThemeDao {
    @Query("SELECT * FROM Theme")
    suspend fun getAllThemes(): List<Theme>

    @Query("SELECT * FROM Theme WHERE theme_id = :themeId")
    suspend fun getTheme(themeId: Int): Theme
}