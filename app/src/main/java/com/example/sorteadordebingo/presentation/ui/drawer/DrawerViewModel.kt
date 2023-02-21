package com.example.sorteadordebingo.presentation.ui.drawer

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
import javax.inject.Inject

sealed class DrawState {
    object NotStarted : DrawState()
    object Loading : DrawState()
    object Starting : DrawState()
    data class Finished(val nextElement: Element) : DrawState()
    data class Drawing(val nextElement: Element) : DrawState()
}

@HiltViewModel
class DrawerViewModel @Inject constructor(
    private val localRepository: LocalRepository
) : ViewModel() {

    /* STATE VARIABLES */
    private var drawState = MutableStateFlow<DrawState>(DrawState.Loading)
    val state = drawState.asStateFlow()


    /* SESSION VARIABLES */
    private var session: Session? = null


    /* THEME VARIABLES */
    private lateinit var themes: List<Theme>
    private lateinit var currentTheme: Theme


    /* ELEMENT VARIABLES */
    private var themeElements = mutableListOf<Element>()
    private var availableElements = mutableListOf<Element>()
    private var drawnElements = mutableListOf<Element>()


    init {
        checkSavedState()
    }

    /*
    This function checks if there was an unfinished draw when the user left the app and
    if yes, it resumes the app state and screen from the point that draw has stopped.
    If not, it composes the screen for theme picking.
     */
    private fun checkSavedState() {
        viewModelScope.launch(Dispatchers.IO) {
            session = localRepository.getActiveSession()
            themes = localRepository.getAllThemes()

            if (session == null) {
                drawState.value = DrawState.NotStarted
            } else {
                session?.let { s ->

                    setCurrentTheme(themes.find { t -> t.themeId == s.sessionTheme }!!)

                    searchThemeElements()

                    val elementsIds =
                        s.drawnElements.split(",").toMutableList()

                    elementsIds.forEach() { it ->
                        drawnElements.add(
                            themeElements.find { e -> e.elementId == it.toInt() }!!
                        )
                    }

                    availableElements =
                        themeElements.filter { it.elementId.toString() !in elementsIds }
                            .toMutableList()

                    drawState.value = DrawState.Drawing(drawnElements.last())
                }
            }
        }
    }

    /*
    This function is responsible for search the database looking for all the elements
    that are part of the current theme selected by the user.
     */
    private suspend fun searchThemeElements() {
        themeElements =
            localRepository.getThemeElements(currentTheme.themeId).toMutableList()
    }

    /*
    This function is responsible for updating the "string/list" of the drawn elements
    in the database.
     */
    private suspend fun updateDrawnElementsIds(elementId: String) {
        var idString = localRepository.getDrawnElementsIds(session!!.sessionId)
    }

    /*
    This function is gonna be called when the user selects the theme and decides to
    start the draw.
    It must set the app state to Drawing and prepare the available and drawn elements lists.
    It also assumes that all the variables have been cleared in the reset method.
     */
    fun startDraw(theme: Theme) {
        setCurrentTheme(theme)

        viewModelScope.launch(Dispatchers.IO) { searchThemeElements() }
            .invokeOnCompletion {
                availableElements = themeElements
                drawNextElement()
            }
    }

    /*
    This function is called at the start of the draw and everytime the user needs to
    draw a new element.
    It shuffles the available elements list, picks the first element there, manipulate both
    element lists and updates the draw state.
     */
    fun drawNextElement() {
        availableElements.shuffle()

        val element = availableElements.first()

        drawnElements.add(element)

        availableElements.removeFirst()

        drawState.value = DrawState.Drawing(element)

        viewModelScope.launch(Dispatchers.IO) {
            updateDrawnElementsIds(element.elementId.toString())
        }
    }

    /*
    This function is called when the draw is over or when the user wants to end the draw.
    It must clear all the lists and variables.
     */
    fun resetDraw() {
    }

    /*
    This function is called by the view to compose the drawn elements lazy row on the bottom
    of the screen.
     */
    fun getDrawnElements(): List<Element> {
        return drawnElements.toList()
    }

    /*
    These two functions are called by the view to help on composing the text that shows the amount
    of elements of the theme and the amount that has already been drawn.
     */
    fun getAmountOfElements(): Int {
        return themeElements.size
    }

    fun getAmountOfDrawnElements(): Int {
        return drawnElements.size
    }

    /*
    This function, obviously, returns the current theme selected by the user.
     */
    fun getCurrentTheme(): Theme {
        return currentTheme
    }

    /*
    And this one sets as current the theme selected by the user (a masterpiece, uh?)
     */
    private fun setCurrentTheme(theme: Theme) {
        currentTheme = theme
    }
}
