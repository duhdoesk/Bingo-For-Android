package com.example.sorteadordebingo.presentation.ui.drawer

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.sorteadordebingo.data.ThemeLocalDataSource
import com.example.sorteadordebingo.model.Element
import com.example.sorteadordebingo.model.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.random.Random

sealed class DrawingState {
    object NotStarted : DrawingState()
    object Finished : DrawingState()
    data class NextElement(val nextElement: Element) : DrawingState()
}

@HiltViewModel
class DrawerViewModel @Inject constructor(private val themeLocalDataSource: ThemeLocalDataSource) : ViewModel() {

//    Armazena lista de temas disponíveis, bem como o tema selecionado
    val themeList = themeList()
    var currentTheme = mutableStateOf(themeList[0])

//    Armazena o estado do sorteio
    private var drawingState = MutableStateFlow<DrawingState>(DrawingState.NotStarted)
    val state = drawingState.asStateFlow()

//    Armazena as listas de elementos sorteados e não sorteados
    private var elementList = currentTheme.value.elements.toMutableList()
    private var notDrawn = mutableListOf<Element>()
    var drawnList = mutableListOf<Element>()

    fun drawNewElement() {
        if (drawingState.value == DrawingState.NotStarted) {
            fillNotDrawnList()
        }

        val index = Random.nextInt(0, notDrawn.size)
        drawnList.add(0, notDrawn[index])
        notDrawn.removeAt(index)

        if (notDrawn.isEmpty()) {
            drawingState.value = DrawingState.Finished
        } else {
            drawingState.value = DrawingState.NextElement(drawnList[0])
        }
    }

    fun restartDrawing() {
        notDrawn = mutableListOf()
        drawnList = mutableListOf()

        drawingState.value = DrawingState.NotStarted
    }

    fun changeTheme(theme: Theme) {
        currentTheme.value = theme
        elementList = currentTheme.value.elements.toMutableList()
    }

    private fun fillNotDrawnList() {
        for (element in elementList) {
            notDrawn.add(element)
        }
    }

    private fun themeList() : List<Theme> {
        return themeLocalDataSource.loadThemes()
    }

}
