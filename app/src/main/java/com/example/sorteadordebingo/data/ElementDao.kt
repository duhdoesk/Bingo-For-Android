package com.example.sorteadordebingo.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ElementDao {

    @Query("UPDATE Element SET element_draw = :draw WHERE element_id = :id")
    fun update(id: Long, draw: Int)

    @Query("SELECT * FROM Element")
    fun getElements() : Flow<List<Element>>

    @Query("SELECT * FROM Element WHERE element_theme = :themeId AND element_draw = 0")
    fun getAvailableElements(themeId: Long) : Flow<List<Element>>

    @Query("SELECT * FROM Element WHERE element_theme = :themeId AND element_draw > 0")
    fun getDrawnElements(themeId: Long) : Flow<List<Element>>
}