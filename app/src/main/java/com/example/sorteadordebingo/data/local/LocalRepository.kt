package com.example.sorteadordebingo.data.local

import com.example.sorteadordebingo.data.ElementDao
import com.example.sorteadordebingo.data.SessionDao
import com.example.sorteadordebingo.data.ThemeDao
import com.example.sorteadordebingo.model.Element
import com.example.sorteadordebingo.model.Session
import com.example.sorteadordebingo.model.Theme
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val sessionDao: SessionDao,
    private val themeDao: ThemeDao,
    private val elementDao: ElementDao
) {

    /* SESSION FUNCTIONS */

    suspend fun createNewSession(session: Session) =
        sessionDao.createNewSession(session)

    suspend fun finishSession(sessionId: Long) =
        sessionDao.finishSession(sessionId)

    suspend fun getActiveSession(): Session? =
        sessionDao.getActiveSession()

    suspend fun setDrawnElementsIds(sessionId: Long, idList: String) =
        sessionDao.setDrawnElementsIds(sessionId, idList)


    /* THEME FUNCTIONS */

    suspend fun getAllThemes(): List<Theme> =
        themeDao.getAllThemes()


    /* ELEMENT FUNCTIONS */

    suspend fun getThemeElements(themeId: Int): List<Element> =
        elementDao.getThemeElements(themeId)


    /* REPOSITORY INSTANCE */

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: LocalRepository? = null

        fun getInstance(
            sessionDao: SessionDao,
            themeDao: ThemeDao,
            elementDao: ElementDao
        ) =
            instance ?: synchronized(this) {
                instance ?: LocalRepository(sessionDao, themeDao, elementDao).also { instance = it }
            }
    }
}