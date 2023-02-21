package com.example.sorteadordebingo.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface SessionDao {

    @Query("INSERT INTO Session (session_theme) VALUES (:themeId)")
    suspend fun createNewSession(themeId: Int)

    @Query("UPDATE Session SET is_completed = 1 WHERE session_id = :sessionId")
    suspend fun finishSession(sessionId: Int)

    @Query("SELECT * FROM Session WHERE is_completed = 0 LIMIT = 1")
    suspend fun getActiveSession(): Session?

    @Query("SELECT session_theme FROM Session WHERE session_id = :sessionId")
    suspend fun getSessionThemeId(sessionId: Int): Int

    @Query("UPDATE Session SET session_theme = :themeId WHERE session_id = :sessionId")
    suspend fun setSessionThemeId(sessionId: Int, themeId: Int)

    @Query("SELECT drawn_elements FROM Session WHERE session_id = :sessionId")
    suspend fun getDrawnElementsIds(sessionId: Int): String

    @Query("UPDATE Session SET drawn_elements = :idList WHERE session_id = :sessionId")
    suspend fun setDrawnElementsIds(sessionId: Int, idList: String)
}
