package com.example.sorteadordebingo.presentation.ui.drawer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sorteadordebingo.data.Element
import com.example.sorteadordebingo.data.LocalRepository
import com.example.sorteadordebingo.data.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

sealed class DrawState {
    object NotStarted : DrawState()
    object Finished : DrawState()
    data class NextElement(val nextElement: Element? = null) : DrawState()
}

@HiltViewModel
class DrawerViewModel @Inject constructor(
    private val localRepository: LocalRepository
) : ViewModel() {

//    STATE
    private var drawState = MutableStateFlow<DrawState>(DrawState.NotStarted)
    val state = drawState.asStateFlow()

//    BINGO THEMES
    val themes = localRepository.getThemes()
    private lateinit var currentTheme : Theme

//    BINGO ELEMENTS
    private val elements = localRepository.getElements()
    private var availableElements = mutableListOf<Element>()
    var drawnElements = mutableListOf<Element>()
    private var counter = if (drawnElements.isEmpty()) 0 else drawnElements[0].element_draw

    fun startDraw(theme: Theme) {
//        sets the bingo theme
        currentTheme = theme

//        sets the mutable list of not drawn elements (those available to be drawn)
        setAvailableElements()

//        changes the state so the UI can compose the draw screen
        drawState.value = DrawState.NextElement()
    }

    fun drawNextElement() {
//        picks a random element from available elements list
        val random = Random.nextInt(0, availableElements.size)
        val element = availableElements[random]

//        increases the counter and then updates the last drawn element on the db
        counter += 1
        localRepository.updateElement(element.element_id, counter)

//        sets available elements and drawn elements again
        setAvailableElements()
        setDrawnElements()

//        updates the state so the UI can compose the element drawn
        drawState.value = DrawState.NextElement(element)
    }

    fun resetDraw() {
        val idList = mutableListOf<Long>()
        for (element in drawnElements) {
            idList.add(element.element_id)
        }
        localRepository.resetDraw(idList)
    }

    private fun setAvailableElements() {
        viewModelScope.launch(Dispatchers.IO) {
            elements.collect() { response ->
                availableElements = response.filter { it.element_theme == currentTheme.theme_id && it.element_draw == 0 } as MutableList<Element>
            }
        }
    }

    private fun setDrawnElements() {
        viewModelScope.launch(Dispatchers.IO) {
            elements.collect() { response ->
                drawnElements = response
                    .filter { it.element_theme == currentTheme.theme_id && it.element_draw > 0 }
                    .sortedBy { it.element_draw } as MutableList<Element>
            }
        }
    }
}
