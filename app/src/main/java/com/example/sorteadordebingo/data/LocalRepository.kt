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
    private val elementDao: ElementDao,
    private val themeDao: ThemeDao
    ) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun updateElement(id: Long, draw: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            elementDao.update(id, draw)
        }
    }

    fun resetDraw(id: List<Long>) {
        for (i in id) {
            elementDao.update(i, 0)
        }
    }

    fun getThemes() = themeDao.getThemes()

    fun getElements() = elementDao.getElements()

    fun getAvailableElements(themeId : Long) = elementDao.getAvailableElements(themeId)

    fun getDrawnElements(themeId : Long) = elementDao.getDrawnElements(themeId)



    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: LocalRepository? = null

        fun getInstance(elementDao: ElementDao, themeDao: ThemeDao) =
            instance ?: synchronized(this) {
                instance ?: LocalRepository(elementDao, themeDao).also { instance = it }
            }
    }
}