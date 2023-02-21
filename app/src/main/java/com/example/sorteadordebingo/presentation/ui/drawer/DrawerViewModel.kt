package com.example.sorteadordebingo.presentation.ui.drawer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sorteadordebingo.data.Element
import com.example.sorteadordebingo.data.LocalRepository
import com.example.sorteadordebingo.data.Session
import com.example.sorteadordebingo.data.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

sealed class DrawState {
    object NotStarted : DrawState()
    object Loading : DrawState()
    object Starting : DrawState()
    data class Finished(val nextElement: Element) : DrawState()
    data class NextElement(val nextElement: Element) : DrawState()
}

@HiltViewModel
class DrawerViewModel @Inject constructor(
    private val localRepository: LocalRepository
) : ViewModel() {

    /* STATE VARIABLES */
    private var drawState = MutableStateFlow<DrawState>(DrawState.Loading)
    val state = drawState.asStateFlow()


    /* SESSION VARIABLES */
    lateinit var session: Session


    /* THEME VARIABLES */
    val themes = localRepository.getThemes()
    private lateinit var currentTheme: Theme


    /* ELEMENT VARIABLES */
    private val elements = localRepository.getElements()
    private var availableElements = mutableListOf<Element>()
    private var drawnElements = mutableListOf<Element>()

    init {
        checkSavedState()
    }

    fun setDrawThemeAndElements(theme: Theme) {
//        sets the bingo theme
        currentTheme = theme

//        sets the mutable list of not drawn elements (those available to be drawn)
        setAvailableElements()

//        changes the state so the UI can compose the draw screen
        drawState.value = DrawState.Starting
    }

    fun drawNextElement() {
//        shuffling list of available elements and picking the first
        availableElements.shuffle()
        val element = availableElements[0]


//        increasing the count and then updates the drawn element in the db
        count += 1
        localRepository.updateElement(element.element_id, count)


//        passing the element from available to drawn list
        availableElements.remove(element)
        drawnElements.add(0, element)


//        updating the state so the UI can compose the element drawn
        drawState.value = DrawState.NextElement(element)

//        checking if there is any more elements available to be drawn next.
//            if list is empty, time to change drawState to Finished
//            else, keeps the state as NextElement
        if (availableElements.isEmpty()) {
            drawState.value = DrawState.Finished(element)
        } else {
            drawState.value = DrawState.NextElement(element)
        }
    }

    fun resetDraw() {
        /* fill a list with the id of all the already drawn elements and then passes it to
        repository in order to reset their draw status in the db */
        viewModelScope.launch(Dispatchers.IO) {
            val idList = mutableListOf<Long>()
            for (element in drawnElements) {
                idList.add(element.element_id)
            }
            localRepository.resetDraw(idList)
            setAvailableElements()
            setDrawnElements()
        }

        /* updating the drawState to NotStarted so the user can select a new theme and start
        drawing again */
        drawState.value = DrawState.NotStarted
    }

    fun getDrawnElements(): List<Element> {
        return drawnElements.toList()
    }

    fun getAmountOfElements(): Int {
        return availableElements.size + drawnElements.size
    }

    fun getAmountOfAvailableElements(): Int {
        return availableElements.size
    }

    fun getAmountOfDrawnElements(): Int {
        return drawnElements.size
    }

    fun getCurrentTheme(): Theme {
        return currentTheme
    }

    fun setCurrentTheme(theme: Theme) {
        currentTheme = theme
    }

    /* Function to search db and set the availableElements list */
    private fun setAvailableElements() {
        viewModelScope.launch(Dispatchers.IO) {
            elements.collect() { response ->
                val availableList =
                    response.filter { it.element_theme == currentTheme.theme_id && it.element_draw == 0 }
                            as MutableList<Element>

                availableElements = if (availableList.isEmpty()) {
                    mutableListOf()
                } else {
                    availableList.toMutableList()
                }
            }
        }
    }

    /* Function to search db and set the drawnElements list */
    private fun setDrawnElements() {
        viewModelScope.launch(Dispatchers.IO) {
            elements.collect() { response ->
                val drawnList = response
                    .filter { it.element_theme == currentTheme.theme_id && it.element_draw > 0 }
                    .sortedBy { it.element_draw }

                drawnElements = if (drawnList.isEmpty()) {
                    mutableListOf()
                } else {
                    drawnList.toMutableList()
                }
            }
        }
    }

    private fun checkSavedState() {

        viewModelScope.launch(Dispatchers.IO) {
            themes.collect() { response ->
                setCurrentTheme(response.find { t -> t.isCurrent })
                this.coroutineContext.job.cancel()

            }
        }

        /* If any element has been drawn, sets both lists to be manipulated
        * by the viewmodel */
        setAvailableElements()
        setDrawnElements()

        /* Sort drawn elements list descending by element id to put in order of draw
        * and then sets the first one as the counter */
        drawnElements.sortByDescending { it.element_draw }
        count = drawnElements[0].element_draw

        /* Changes app state to compose the screen showing the drawn element */
        drawState.value = DrawState.NextElement(drawnElements[0])
    }
}
}
