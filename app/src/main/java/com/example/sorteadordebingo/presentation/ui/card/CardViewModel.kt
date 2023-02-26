package com.example.sorteadordebingo.presentation.ui.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sorteadordebingo.model.Element
import com.example.sorteadordebingo.data.local.LocalRepository
import com.example.sorteadordebingo.model.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CardState() {
    object Loading: CardState()
    data class DrawnCard(
        val theme: Theme,
        val card: List<Element>
        ): CardState()
}

@HiltViewModel
class CardViewModel @Inject constructor(private val localRepository: LocalRepository) : ViewModel() {

    /* STATE VARIABLES */
    private var cardState = MutableStateFlow<CardState>(CardState.Loading)
    val state = cardState.asStateFlow()

    /* THEME VARIABLES */
    private var themes: List<Theme> = emptyList()
    private lateinit var currentTheme: Theme

    /* ELEMENT VARIABLES */
    private var themeElements = mutableListOf<Element>()
    private var cardElements = mutableListOf<Element>()
//    private var cardElements = mutableListOf<Element>()

    init {
        loadData()
    }

    /*
    This function is responsible for populating our theme and elements variables by the
    call of the init method
     */
    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            themes = localRepository.getAllThemes()
            currentTheme = themes[0]
            themeElements = localRepository.getThemeElements(currentTheme.themeId).toMutableList()
            shuffleCard()
            cardState.value = CardState.DrawnCard(currentTheme, cardElements)
        }
    }


    /*
    The functions below are set to be called by the user depending on the actions
    he takes on the view
     */

    fun getThemes() : List<Theme> {
        return themes
    }

    fun setCurrentTheme(theme: Theme) {
        viewModelScope.launch(Dispatchers.IO) {
            currentTheme = theme
            themeElements = localRepository.getThemeElements(currentTheme.themeId).toMutableList()
            shuffleCard()
        }
    }

    fun shuffleCard() {
        cardElements = themeElements.shuffled().subList(0, 9).toMutableList()
        cardState.value = CardState.DrawnCard(currentTheme, cardElements)
    }
}