package com.example.sorteadordebingo.presentation.ui.cartela

import android.content.res.Configuration
import android.os.Bundle
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.sorteadordebingo.R
import com.example.sorteadordebingo.presentation.theme.*

class CartelaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme() {
                    Surface {
                        SetCartelaFragment()
                    }
                }
            }
        }
    }

    @Preview(name =  "Light Mode")
    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark mode"
    )
    @Composable
    private fun SetCartelaFragment() {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

//            Chamada da função responsável pela criação do menu suspenso
            DropdownMenu()

//            Chamada da função responsável pela criação do grid
            GridMaker()

//            Botão responsável pelo sorteio da cartela
            Button(
                onClick = { /*TODO*/ },
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

        val options = listOf("Bichos", "Flores", "Frutas")
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(options[0]) }

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
                            selectedOptionText = selectionOption
                            expanded = false
                        }
                    ) { Text(text = selectionOption) }
                }
            }
        }
    }

//    Função responsável pela criação do grid
    @Composable
    private fun GridMaker() {
        val bichos = listOf(
            listOf("Cachorro", "Gato", "Lesma"),
            listOf("Pavão", "Raposa", "Tartaruga Voadora"),
            listOf("Capivara", "Leão", "Castor")
        )

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

//                            Imagem do Bicho
                            Image(
                                painter = painterResource(id = R.drawable.ic_baseline_emoji_nature_24),
                                contentDescription = "Animal image",
                                Modifier.size(80.dp)
                            )

//                            Nome do Bicho
                            Text(
                                text = bichos[row][column],
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
