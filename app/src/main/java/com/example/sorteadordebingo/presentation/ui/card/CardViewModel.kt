package com.example.sorteadordebingo.presentation.ui.card

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.sorteadordebingo.data.ThemeLocalDataSource
import com.example.sorteadordebingo.model.Element
import com.example.sorteadordebingo.model.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CardViewModel @Inject constructor(private val themeLocalDataSource: ThemeLocalDataSource) : ViewModel() {

    val elementList: MutableState<List<Element>?> = mutableStateOf(emptyList())
    val themeList: MutableState<List<Theme>> = mutableStateOf(themeList())
    var themeId: String = "1"

    init {
        dealNewList()
    }

    fun dealNewList() {
        val theme = getTheme(themeId)!!
        val drawIndexes = drawIndexes(theme.elements.size)
        val newList = mutableListOf<Element>()

        for (element in drawIndexes) {
            newList.add(theme.elements[element])
        }

        elementList.value = newList
    }

    private fun drawIndexes(size: Int): List<Int> {
        return generateSequence { Random.nextInt(0, size) }
            .distinct()
            .take(9)
            .toSet()
            .toList()
    }

    private fun themeList() : List<Theme> {
        return themeLocalDataSource.loadThemes()
    }

    private fun getTheme(id: String) : Theme? {
        val theme = themeLocalDataSource.loadThemes().find {
            it.id == id
        } ?: return null

        return theme
    }
}