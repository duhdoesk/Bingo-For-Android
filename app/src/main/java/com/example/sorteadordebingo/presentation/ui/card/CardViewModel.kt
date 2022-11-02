package com.example.sorteadordebingo.presentation.ui.card

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.sorteadordebingo.data.ThemeLocalDataSource
import com.example.sorteadordebingo.model.Element
import com.example.sorteadordebingo.model.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class BingoViewModel @Inject constructor(private val themeLocalDataSource: ThemeLocalDataSource) : ViewModel() {

    val themeList = mutableStateOf(themeList())
    var currentTheme = mutableStateOf(themeList.value[0])
    val elementList = mutableStateOf(currentTheme.value.elements)

    fun dealNewList() {
        val drawIndexes = drawIndexes(currentTheme.value.elements.size)
        val newList = mutableListOf<Element>()

        for (element in drawIndexes) {
            newList.add(currentTheme.value.elements[element])
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

}