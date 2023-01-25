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

    @Query("UPDATE element SET draw = :draw WHERE id = :id")
    fun update(id: Long, draw: Boolean)

    @Query("DELETE FROM element")
    fun delete()

    @Query("SELECT * FROM element")
    fun getElements() : Flow<List<Element>>
}