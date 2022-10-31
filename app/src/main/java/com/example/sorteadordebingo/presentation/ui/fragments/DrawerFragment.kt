package com.example.sorteadordebingo.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sorteadordebingo.R
import com.example.sorteadordebingo.model.Element
import com.example.sorteadordebingo.presentation.theme.AppTheme
import com.example.sorteadordebingo.presentation.theme.grid_background
import com.example.sorteadordebingo.presentation.ui.model.BingoViewModel
import com.example.sorteadordebingo.util.DEFAULT_IMAGE
import com.example.sorteadordebingo.util.loadPicture
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DrawerFragment : Fragment() {

    private val viewModel: BingoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    Surface {
                        SetDrawerView() }
                }
            }
        }
    }

    @Preview
    @Composable
    private fun SetDrawerView() {

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
        ) {

            /*
            Função responsável pela criação dos componentes para
            informar e selecionar temas
             */
            ThemePicker()

            /*
            Função responsável por criar os itens principais da tela - botões
            de sorteio e exibição do elemento sorteado
             */
            Drawer()

            /*
            Função responsável pela criação da lazyrow que exibe
            os elementos sorteados
             */
            ElementsDrawnLazyRow()

        }

    }
    
    @Composable
    private fun ThemePicker() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(grid_background)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(16.dp)
            ) {
                Text(
                    text = "Tema do Bingo",
                    color = MaterialTheme.colors.primaryVariant
                )
                Text(
                    text = "Frutas",
                    style = MaterialTheme.typography.h6
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primaryVariant
                    )
                ) {
                    Text(text = "Trocar")
                }
            }
        }

    }

    @Composable
    private fun Drawer() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            val element: Element = Element(
                "1",
                "Carangueijo",
                "https://cdn-icons-png.flaticon.com/512/6498/6498613.png"
            )

            Image(
                painter = painterResource(id = R.drawable.ic_baseline_emoji_nature_24),
                contentDescription = "Elemento",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )

            Text(
                text = element.name.uppercase(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Gray
                    )
                ) {
                    Text(
                        text = "Recomeçar",
                        color = MaterialTheme.colors.onPrimary
                    )
                }

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "SORTEAR PRÓXIMO")
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun ElementsDrawnLazyRow() {
        val elements: List<Element> = listOf(
            Element(
                "1",
                "Carangueijo",
                "https://cdn-icons-png.flaticon.com/512/6498/6498613.png"
            ),
            Element("1", "Jiboia", "https://cdn-icons-png.flaticon.com/512/6498/6498613.png"),
            Element("1", "Libélula", "https://cdn-icons-png.flaticon.com/512/6498/6498613.png"),
            Element("1", "Jacaré", "https://cdn-icons-png.flaticon.com/512/6498/6498613.png"),
            Element(
                "1",
                "Orangotangos",
                "https://cdn-icons-png.flaticon.com/512/6498/6498613.png"
            ),
            Element("2", "Baleia", "https://cdn-icons-png.flaticon.com/512/6498/6498613.png")
        )
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
                        backgroundColor = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(4.dp),
                        onClick = { }
                    ) {
                        Text(
                            text = element.name.uppercase(),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colors.onPrimary,
                            modifier = Modifier.padding(12.dp, 8.dp)
                        )
                    }
                }
            }
        }
    }
}