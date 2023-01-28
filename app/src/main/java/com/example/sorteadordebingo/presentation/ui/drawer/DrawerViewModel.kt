package com.example.sorteadordebingo.presentation.ui.drawer

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.sorteadordebingo.data.Element
import com.example.sorteadordebingo.data.LocalRepository
import com.example.sorteadordebingo.data.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

sealed class DrawingState {
    object NotStarted : DrawingState()
    object Finished : DrawingState()
    data class NextElement(val nextElement: Element) : DrawingState()
}

@HiltViewModel
class DrawerViewModel @Inject constructor(
    private val localRepository: LocalRepository
    ) : ViewModel() {

    private var drawingState = MutableStateFlow<DrawingState>(DrawingState.NotStarted)
    val state = drawingState.asStateFlow()

    val themes = localRepository.themes
    val currentTheme = mutableStateOf<Theme?>(themes.value?.get(0))

//
//    val elements = localRepository.getElements().asLiveData()
//    val themeElements = mutableStateOf(elements.value?.filter { it.element_theme == currentTheme.value?.theme_id })
//    val drawElements = mutableStateOf(elements.value?.filter { it.element_draw == 1 })
//    val notDrawElements = mutableStateOf(elements.value?.filter  { it.element_draw == 0 })
//    val drawHistory = mutableListOf<Element>()

    fun getThemes() {
        localRepository.getThemes()
    }
    fun changeTheme(theme: Theme) {
        currentTheme.value = theme
    }

}
