package com.example.sorteadordebingo.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElementRepository @Inject constructor(private val elementDao: ElementDao) {

    fun insertElement(element: Element) =
        elementDao.insert(element)

    fun updateElement(id: Long, draw: Boolean) =
        elementDao.update(id, draw)

    fun delete() =
        elementDao.delete()

    fun getElements(): Flow<List<Element>> =
        elementDao.getElements()

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: ElementRepository? = null

        fun getInstance(elementDao: ElementDao) =
            instance ?: synchronized(this) {
                instance ?: ElementRepository(elementDao).also { instance = it }
            }
    }
}