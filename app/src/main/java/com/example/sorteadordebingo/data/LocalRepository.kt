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

    val themes = MutableLiveData<List<Theme>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun updateElement(id: Long, draw: Boolean) =
        elementDao.update(id, draw)

    fun resetDraw(id: List<Long>) {
        for (i in id) {
            elementDao.update(i, false)
        }
    }

    fun getElements(): List<Element> =
        elementDao.getElements()

    fun getThemes() {
        coroutineScope.launch(Dispatchers.IO) {
            themes.postValue(themeDao.getThemes())
        }
    }

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