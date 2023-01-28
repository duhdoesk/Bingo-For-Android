package com.example.sorteadordebingo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ThemeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(theme: Theme)

    @Query("DELETE FROM Theme")
    fun delete()

    @Query("SELECT * FROM Theme")
    fun getThemes() : List<Theme>
}