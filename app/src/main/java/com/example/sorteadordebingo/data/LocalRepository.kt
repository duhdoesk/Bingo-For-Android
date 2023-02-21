package com.example.sorteadordebingo.data

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val sessionDao: SessionDao,
    private val themeDao: ThemeDao,
    private val elementDao: ElementDao
) {

    /* SESSION FUNCTIONS */

    suspend fun createNewSession(themeId: Int) =
        sessionDao.createNewSession(themeId)

    suspend fun finishSession(sessionId: Int) =
        sessionDao.finishSession(sessionId)

    suspend fun getActiveSession(): Session? =
        sessionDao.getActiveSession()

    suspend fun getSessionThemeId(sessionId: Int): Int =
        sessionDao.getSessionThemeId(sessionId)

    suspend fun setSessionThemeId(sessionId: Int, themeId: Int) =
        sessionDao.setSessionThemeId(sessionId, themeId)

    suspend fun getDrawnElementsIds(sessionId: Int): String =
        sessionDao.getDrawnElementsIds(sessionId)

    suspend fun setDrawnElementsIds(sessionId: Int, idList: String) =
        sessionDao.setDrawnElementsIds(sessionId, idList)


    /* THEME FUNCTIONS */

    suspend fun getAllThemes(): List<Theme> =
        themeDao.getAllThemes()

    suspend fun getTheme(themeId: Int): Theme =
        themeDao.getTheme(themeId)


    /* ELEMENT FUNCTIONS */

    suspend fun getAllElements(): List<Element> =
        elementDao.getAllElements()

    suspend fun getThemeElements(themeId: Int): List<Element> =
        elementDao.getThemeElements(themeId)

    suspend fun getElement(elementId: Int): Element =
        elementDao.getElement(elementId)


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