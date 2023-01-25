package com.example.sorteadordebingo.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepository @Inject constructor(private val themeDao: ThemeDao) {

    fun insertTheme(theme: Theme) =
        themeDao.insert(theme = theme)

    fun deleteTheme() =
        themeDao.delete()

    fun getThemes() : Flow<List<Theme>> =
        themeDao.getThemes()
}