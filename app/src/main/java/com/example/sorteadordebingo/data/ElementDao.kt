package com.example.sorteadordebingo.data

import androidx.room.Dao
import androidx.room.Query
import com.example.sorteadordebingo.model.Element

@Dao
interface ElementDao {

    @Query("SELECT * FROM Element WHERE element_theme = :themeId")
    suspend fun getThemeElements(themeId: Int): List<Element>

    @Query("SELECT * FROM Element WHERE element_id = :elementId")
    suspend fun getElement(elementId: Int): Element

}