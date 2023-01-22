@file:OptIn(ExperimentalMaterialApi::class, ExperimentalCoroutinesApi::class)

package com.example.sorteadordebingo.presentation.ui.drawer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.sorteadordebingo.R
import com.example.sorteadordebingo.model.Element
import com.example.sorteadordebingo.presentation.theme.AppTheme
import com.example.sorteadordebingo.presentation.theme.grid_background
import com.example.sorteadordebingo.util.DEFAULT_IMAGE
import com.example.sorteadordebingo.util.loadPicture
import com.example.sorteadordebingo.util.shareBitmap
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
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

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
        ) {

            /*
            Função responsável pela criação dos componentes para
            informar e selecionar temas
             */
            DropdownMenu(drawingState)

            /*
            Função responsável por criar os itens principais da tela - botões
            de sorteio e exibição do elemento sorteado
             */
            Drawer(drawingState)

            /*
            Função responsável pela criação da lazyrow que exibe
            os elementos sorteados
             */
            ElementsDrawnLazyRow()

        }

    }

//    Função responsável pela criação do menu suspenso
    @Composable
    private fun DropdownMenu(drawingState: DrawingState) {

        val options = viewModel.themeList
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                readOnly = true,
                value = viewModel.currentTheme.value.name,
                onValueChange = { },
                label = { Text(text = "Tema do Bingo") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(backgroundColor = grid_background),
                modifier = Modifier.fillMaxWidth(),
                enabled = (drawingState == DrawingState.NotStarted)
            )

            if (drawingState == DrawingState.NotStarted) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                viewModel.changeTheme(selectionOption)
                            }
                        ) { Text(text = selectionOption.name) }
                    }
                }
            }
        }
    }

//    Função responsável pela criação do sorteador, a depender do estado do sorteio
    @Composable
    private fun Drawer(drawingState: DrawingState) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            when (drawingState) {
                is DrawingState.NotStarted -> { StateNotStarted() }
                is DrawingState.NextElement -> { StateNextElement(drawingState) }
                else -> { StateFinished() }
            }
        }
    }

//    Função responsável pela criação da lazyrow que informa os elementos já sorteados
    @Composable
    private fun ElementsDrawnLazyRow() {
        val elements = viewModel.drawnList

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Elementos Sorteados:",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            LazyRow(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                itemsIndexed(items = elements) { _, element ->
                    Card(
                        shape = MaterialTheme.shapes.medium,
                        elevation = 8.dp,
                        backgroundColor =
                            if (element == elements[0]) MaterialTheme.colors.primaryVariant
                            else Color.Gray,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(4.dp),
                        onClick = { copyToClipboard(stringOfElementsDrawn(elements)) }
                    ) {
                        Text(
                            text = element.name.uppercase(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.onPrimary,
                            modifier = Modifier.padding(12.dp, 8.dp)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun StateNotStarted() {
        Text(
            text = "NADA POR AQUI...",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .fillMaxWidth()
        )

        Text(
            text = "Inicie um novo sorteio clicando no botão abaixo",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .fillMaxWidth()
        )

        Button(
            onClick = { viewModel.drawNewElement() },
            modifier = Modifier.padding(top = 40.dp),
        ) {
            Text(
                text = "NOVO SORTEIO",
                color = MaterialTheme.colors.onPrimary
            )
        }
    }

    @Composable
    private fun StateNextElement(drawingState: DrawingState.NextElement) {

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val captureController = rememberCaptureController()

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Capturable(
                    controller = captureController,
                    onCaptured = { bitmap, error ->
                        if (bitmap != null) {
                            context?.let {
                                shareBitmap(
                                    bitmap,
                                    it,
                                    viewModel.drawnList[0].name
                                )
                            }
                        }

                        if (error != null) {
                            Log.d("CapturingError: ", "$error")
                        }
                    }) {

                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .wrapContentWidth()
                            .background(MaterialTheme.colors.background),
                    ) {
                        drawingState.nextElement.let {

                            Text(
                                text = "${viewModel.drawnList.size} / ${viewModel.currentTheme.value.elements.size}",
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.wrapContentWidth()
                            )

                            it.image.let { url ->
                                val image = loadPicture(url = url, defaultImage = DEFAULT_IMAGE).value
                                image?.let { img ->
                                    Image(
                                        bitmap = img.asImageBitmap(),
                                        contentDescription = "Element image",
                                        Modifier
                                            .wrapContentWidth()
                                            .height(160.dp)
                                            .padding(top = 8.dp)
                                    )
                                }
                            }

                            Text(
                                text = it.name.uppercase(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(top = 16.dp)
                            )
                        }
                    }
                }
            }


            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
            ) {
                Button(
                    onClick = { viewModel.drawNewElement() },
                    modifier = Modifier.width(160.dp)
                ) {
                    Text(text = "PRÓXIMO")
                }

                Button(
                    onClick = { viewModel.restartDrawing() },
                    modifier = Modifier.width(160.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primaryVariant
                    )
                ) {
                    Text(
                        text = "RECOMEÇAR",
                        color = MaterialTheme.colors.onPrimary
                    )
                }

//                Botão responsável por compartilhar a cartela
                Button(
                    onClick = {
                        captureController.capture()
                    },
                    elevation = ButtonDefaults.elevation(4.dp),
                    colors = ButtonDefaults.buttonColors( backgroundColor = Color.Gray ),
                    modifier = Modifier.width(160.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.compartilhar_cartela).uppercase(),
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }

    }

    @Composable
    private fun StateFinished() {
        Text(
            text = "BINGO ENCERRADO!",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier
                .fillMaxWidth()
        )

        Text(
            text = "Você já sorteou todas as pedras.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .fillMaxWidth()
        )

        Button(
            onClick = { viewModel.restartDrawing() },
            modifier = Modifier.padding(top = 40.dp),
        ) {
            Text(
                text = "NOVO SORTEIO",
                color = MaterialTheme.colors.onPrimary
            )
        }
    }

    private fun stringOfElementsDrawn(elements: List<Element>): String {
        var string = "*${viewModel.currentTheme.value.name.uppercase()} SORTEADOS(as):* \n\n"

        for (element in elements) {
            string += element.name.plus("\n")
        }

        return string
    }

    private fun copyToClipboard(string: String) {
        val clipboardManager = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Elementos Sorteados", string)
        clipboardManager.setPrimaryClip(clipData)

        view?.let {
            Snackbar.make(
                it,
                "Lista Copiada",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

}