package com.example.sorteadordebingo.presentation.ui.card

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.sorteadordebingo.R
import com.example.sorteadordebingo.model.Element
import com.example.sorteadordebingo.presentation.theme.*
import com.example.sorteadordebingo.util.DEFAULT_IMAGE
import com.example.sorteadordebingo.util.loadPicture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CardFragment : Fragment() {

    private val viewModel: CardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    Surface { viewModel.elementList.value?.let { SetCardFragment(it) } }
                }
            }
        }
    }

    @Composable
    private fun SetCardFragment(elementList: List<Element>) {

        Log.d("SetCardFragment() ", "called")

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

//            Chamada da função responsável pela criação do menu suspenso
            DropdownMenu()

//            Chamada da função responsável pela criação do grid
            GridMaker(elementList)

//            Botão responsável pelo sorteio da cartela
            Button(
                onClick = { viewModel.dealNewList() },
                elevation = ButtonDefaults.elevation(4.dp)
            ) {
                Text(text = stringResource(id = R.string.sortear_cartela).uppercase())
            }

        }
    }

//    Função responsável pela criação do menu suspenso
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun DropdownMenu() {

//        val options = listOf("Bichos", "Flores", "Frutas")
        val options = viewModel.themeList.value
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(options[0].name) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                readOnly = true,
                value = selectedOptionText,
                onValueChange = { },
                label = { Text(text = "Tema do Bingo") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(backgroundColor = grid_background)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOptionText = selectionOption.name
                            expanded = false
                            viewModel.themeId = selectionOption.id
                            viewModel.dealNewList()
                        }
                    ) { Text(text = selectionOption.name) }
                }
            }
        }
    }

//    Função responsável pela criação do grid
    @OptIn(ExperimentalCoroutinesApi::class)
    @Composable
    private fun GridMaker(elementList: List<Element>) {
        val elements = listOf(
            listOf(elementList[0], elementList[1], elementList[2]),
            listOf(elementList[3], elementList[4], elementList[5]),
            listOf(elementList[6], elementList[7], elementList[8]),
        )

        Log.d("GridMaker() ", "called")

        Column(
            Modifier
                .padding(vertical = 16.dp)
                .clip(RoundedCornerShape(12.dp))) {
            repeat(3) { row ->
                Row {
                    repeat(3) { column ->
                        Column(
                            modifier = Modifier
                                .padding(1.dp)
                                .width(100.dp)
                                .height(124.dp)
                                .background(grid_background),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

//                            Imagem do elemento
                            elements[row][column].image.let { url ->
                                val image = loadPicture(url = url, defaultImage = DEFAULT_IMAGE).value
                                image?.let { img ->
                                    Image(
                                        bitmap = img.asImageBitmap(),
                                        contentDescription = "Element image",
                                        Modifier.size(70.dp)
                                    )
                                }
                            }

//                            Nome do Elemento
                            Text(
                                text = elements[row][column].name,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
