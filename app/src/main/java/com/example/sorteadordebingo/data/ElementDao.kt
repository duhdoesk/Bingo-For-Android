package com.example.sorteadordebingo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ElementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(element: Element)

    @Query("UPDATE Element SET element_draw = :draw WHERE element_id = :id")
    fun update(id: Long, draw: Boolean)

    @Query("DELETE FROM Element")
    fun delete()

    @Query("SELECT * FROM Element")
    fun getElements() : List<Element>
}