package com.example.sorteadordebingo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewSession(session: Session) : Long

    @Query("UPDATE Session SET is_completed = 1 WHERE session_id = :sessionId")
    suspend fun finishSession(sessionId: Long)

    @Query("SELECT * FROM Session WHERE is_completed = 0 LIMIT 1")
    suspend fun getActiveSession(): Session?

    @Query("SELECT session_theme FROM Session WHERE session_id = :sessionId")
    suspend fun getSessionThemeId(sessionId: Long): Int

    @Query("UPDATE Session SET session_theme = :themeId WHERE session_id = :sessionId")
    suspend fun setSessionThemeId(sessionId: Long, themeId: Int)

    @Query("SELECT drawn_elements FROM Session WHERE session_id = :sessionId")
    suspend fun getDrawnElementsIds(sessionId: Long): String

    @Query("UPDATE Session SET drawn_elements = :idList WHERE session_id = :sessionId")
    suspend fun setDrawnElementsIds(sessionId: Long, idList: String)
}
