@file:OptIn(ExperimentalMaterialApi::class, ExperimentalCoroutinesApi::class)

package com.example.sorteadordebingo.presentation.ui.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.sorteadordebingo.R
import com.example.sorteadordebingo.data.Theme
import com.example.sorteadordebingo.presentation.ui.component.ThemeLazyColumnCard
import com.example.sorteadordebingo.presentation.theme.AppTheme
import com.example.sorteadordebingo.presentation.theme.grid_background
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DrawerFragment : Fragment() {

    private val viewModel: DrawerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getThemes()

        return ComposeView(requireContext()).apply {
            lifecycleScope.launch {
                viewModel.state.collect {
                    setContent {
                        AppTheme {
                            Surface {
                                SetDrawerView(it)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SetDrawerView(drawingState: DrawingState) {
        val themes: List<Theme> by viewModel.themes.observeAsState(initial = listOf())

        if (themes.isNotEmpty()) {
            when (drawingState) {
                is DrawingState.NotStarted -> {
                    StateNotStarted(themes)
                }
                is DrawingState.NextElement -> {
                    TODO()
//                    StateNextElement(drawingState)
                }
                else -> {
                    TODO()
//                    StateFinished()
                }
            }
        }
    }


    @Composable
    private fun StateNotStarted(themes: List<Theme>) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(id = R.string.pick_theme),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 32.dp)
                ) {
                    itemsIndexed(items = themes) { _, theme ->
                        ThemeLazyColumnCard(
                            theme = theme,
                            onClick = {
                                TODO()
                            }
                        )
                    }
                }
            }
        }
    }

//    //    Função responsável pela criação do menu suspenso
//    @Composable
//    private fun DropdownMenu(drawingState: DrawingState, themes: List<Theme>) {
//
//        var expanded by remember { mutableStateOf(false) }
//        val options = themes
//
//        ExposedDropdownMenuBox(
//            expanded = expanded,
//            onExpandedChange = { expanded = !expanded }
//        ) {
//            TextField(
//                readOnly = true,
//                value = viewModel.currentTheme.value?.theme_name ?: themes[0].theme_name,
//                onValueChange = { },
//                label = { Text(text = "Tema do Bingo") },
//                trailingIcon = {
//                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
//                },
//                colors = ExposedDropdownMenuDefaults.textFieldColors(backgroundColor = grid_background),
//                modifier = Modifier.fillMaxWidth(),
//                enabled = (drawingState == DrawingState.NotStarted)
//            )
//
//            if (drawingState == DrawingState.NotStarted) {
//                ExposedDropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = { expanded = false }
//                ) {
//                    options.forEach { selectionOption ->
//                        DropdownMenuItem(
//                            onClick = {
//                                expanded = false
//                                viewModel.changeTheme(selectionOption)
//                            }
//                        ) { Text(text = selectionOption.theme_name) }
//                    }
//                }
//            }
//        }
//    }

    //    Função responsável pela criação do sorteador, a depender do estado do sorteio
//    @Composable
//    private fun Drawer(drawingState: DrawingState) {
//
//        Column(
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier
//                .padding(horizontal = 8.dp)
//        ) {
//            when (drawingState) {
//                is DrawingState.NotStarted -> {
//                    StateNotStarted()
//                }
//                is DrawingState.NextElement -> {
//                    StateNextElement(drawingState)
//                }
//                else -> {
//                    StateFinished()
//                }
//            }
//        }
//    }
//
//    //    Função responsável pela criação da lazyrow que informa os elementos já sorteados
//    @Composable
//    private fun ElementsDrawnLazyRow() {
//        val elements = viewModel.elements.value?.filter { it.element_theme == viewModel.currentTheme.value?.theme_id }
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .wrapContentSize()
//                .padding(8.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            Text(
//                text = "Elementos Sorteados:",
//                textAlign = TextAlign.Center,
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            LazyRow(
//                Modifier
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                itemsIndexed(items = elements!!) { _, element ->
//                    Card(
//                        shape = MaterialTheme.shapes.medium,
//                        elevation = 8.dp,
//                        backgroundColor =
//                        if (element == elements[0]) MaterialTheme.colors.primaryVariant
//                        else Color.Gray,
//                        modifier = Modifier
//                            .wrapContentSize()
//                            .padding(4.dp),
//                        onClick = { copyToClipboard(stringOfElementsDrawn(elements)) }
//                    ) {
//                        Text(
//                            text = element.element_name.uppercase(),
//                            fontWeight = FontWeight.SemiBold,
//                            fontSize = 12.sp,
//                            color = MaterialTheme.colors.onPrimary,
//                            modifier = Modifier.padding(12.dp, 8.dp)
//                        )
//                    }
//                }
//            }
//        }
//    }

//    @Composable
//    private fun StateNextElement(drawingState: DrawingState.NextElement) {
//
//        Column(
//            verticalArrangement = Arrangement.SpaceEvenly,
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier
//                .fillMaxWidth()
//        ) {
//            val captureController = rememberCaptureController()
//
//            Column(
//                verticalArrangement = Arrangement.SpaceEvenly,
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Capturable(
//                    controller = captureController,
//                    onCaptured = { bitmap, error ->
//                        if (bitmap != null) {
//                            context?.let {
//                                shareBitmap(
//                                    bitmap,
//                                    it,
//                                    viewModel.drawElements.value!![0].element_name
//                                )
//                            }
//                        }
//
//                        if (error != null) {
//                            Log.d("CapturingError: ", "$error")
//                        }
//                    }) {
//
//                    Column(
//                        verticalArrangement = Arrangement.SpaceEvenly,
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier
//                            .wrapContentWidth()
//                            .background(MaterialTheme.colors.background),
//                    ) {
//                        drawingState.nextElement.let {
//
//                            Text(
//                                text = "${viewModel.drawElements.value?.size} / ${viewModel.themeElements.value?.size}",
//                                textAlign = TextAlign.Center,
//                                fontWeight = FontWeight.Bold,
//                                color = MaterialTheme.colors.primary,
//                                modifier = Modifier.wrapContentWidth()
//                            )
//
//                            it.element_picture.let { url ->
//                                val image =
//                                    loadPicture(url = url, defaultImage = DEFAULT_IMAGE).value
//                                image?.let { img ->
//                                    Image(
//                                        bitmap = img.asImageBitmap(),
//                                        contentDescription = "Element image",
//                                        Modifier
//                                            .wrapContentWidth()
//                                            .height(160.dp)
//                                            .padding(top = 8.dp)
//                                    )
//                                }
//                            }
//
//                            Text(
//                                text = it.element_name.uppercase(),
//                                textAlign = TextAlign.Center,
//                                fontWeight = FontWeight.Bold,
//                                modifier = Modifier
//                                    .wrapContentWidth()
//                                    .padding(top = 16.dp)
//                            )
//                        }
//                    }
//                }
//            }
//
//
//            Column(
//                verticalArrangement = Arrangement.SpaceEvenly,
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 40.dp)
//            ) {
//                Button(
////                    onClick = { viewModel.drawNewElement() },
//                    onClick = { TODO() },
//                    modifier = Modifier.width(160.dp)
//                ) {
//                    Text(text = "PRÓXIMO")
//                }
//
//                Button(
////                    onClick = { viewModel.restartDrawing() },
//                    onClick = { TODO() },
//                    modifier = Modifier.width(160.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        backgroundColor = MaterialTheme.colors.primaryVariant
//                    )
//                ) {
//                    Text(
//                        text = "RECOMEÇAR",
//                        color = MaterialTheme.colors.onPrimary
//                    )
//                }
//
////                Botão responsável por compartilhar a cartela
//                Button(
//                    onClick = {
//                        captureController.capture()
//                    },
//                    elevation = ButtonDefaults.elevation(4.dp),
//                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
//                    modifier = Modifier.width(160.dp)
//                ) {
//                    Text(
//                        text = stringResource(id = R.string.compartilhar_cartela).uppercase(),
//                        color = MaterialTheme.colors.onPrimary
//                    )
//                }
//            }
//        }
//
//    }
//
//    @Composable
//    private fun StateFinished() {
//        Text(
//            text = "BINGO ENCERRADO!",
//            textAlign = TextAlign.Center,
//            style = MaterialTheme.typography.h4,
//            fontStyle = FontStyle.Italic,
//            fontWeight = FontWeight.Bold,
//            color = MaterialTheme.colors.primaryVariant,
//            modifier = Modifier
//                .fillMaxWidth()
//        )
//
//        Text(
//            text = "Você já sorteou todas as pedras.",
//            textAlign = TextAlign.Center,
//            style = MaterialTheme.typography.subtitle1,
//            modifier = Modifier
//                .fillMaxWidth()
//        )
//
//        Button(
////            onClick = { viewModel.restartDrawing() },
//            onClick = { TODO() },
//            modifier = Modifier.padding(top = 40.dp),
//        ) {
//            Text(
//                text = "NOVO SORTEIO",
//                color = MaterialTheme.colors.onPrimary
//            )
//        }
//    }
//
//    private fun stringOfElementsDrawn(elements: List<com.example.sorteadordebingo.data.Element>?): String {
//        var string = "*${viewModel.currentTheme.value?.theme_name?.uppercase()} SORTEADOS(as):* \n\n"
//
//        if (elements != null) {
//            for (element in elements) {
//                string += element.element_name.plus("\n")
//            }
//        }
//
//        return string
//    }
//
//    private fun copyToClipboard(string: String) {
//        val clipboardManager =
//            activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//        val clipData = ClipData.newPlainText("Elementos Sorteados", string)
//        clipboardManager.setPrimaryClip(clipData)
//
//        view?.let {
//            Snackbar.make(
//                it,
//                "Lista Copiada",
//                Snackbar.LENGTH_SHORT
//            ).show()
//        }
//    }

}