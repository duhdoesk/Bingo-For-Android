package com.example.sorteadordebingo.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ElementDao {

    @Query("SELECT * FROM Element")
    suspend fun getAllElements(): List<Element>

    @Query("SELECT * FROM Element WHERE element_theme = :themeId")
    suspend fun getThemeElements(themeId: Int): List<Element>

    @Query("SELECT * FROM Element WHERE element_id = :elementId")
    suspend fun getElement(elementId: Int): Element

}