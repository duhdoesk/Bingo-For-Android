package com.example.sorteadordebingo.ui.cartela

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.sorteadordebingo.R
import com.example.sorteadordebingo.ui.theme.*

class CartelaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ProjectTheme {
                    Surface() {
                        SetCartelaFragment()
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    private fun SetCartelaFragment() {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to PrimaryLight,
                        1000f to PrimaryDark
                    )
                ),
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
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = SecondaryLight,
                    contentColor = PrimaryText
                )
            ) {
                Text(text = stringResource(id = R.string.sortear_cartela).uppercase())
            }

        }
    }

//    Função responsável pela criação do menu suspenso
    @Composable
    fun DropdownMenu() {
        var expanded by remember { mutableStateOf(false) }
        val items = listOf("Bichos", "Flores")
        var selectedIndex by remember { mutableStateOf(0) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .width(200.dp)
                .height(32.dp)
                .background(gridBackgroud, RoundedCornerShape(8.dp))
                .border(1.dp, PrimaryText, RoundedCornerShape(8.dp))
                .clickable(onClick = { expanded = true })
        ) {
            Text(
                items[selectedIndex],
                color = PrimaryText,
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .padding(start = 12.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
                contentDescription = "Dropdown Arrow",
                Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(200.dp)
            ) {
                items.forEachIndexed { index, option ->
                    DropdownMenuItem(onClick = {
                        selectedIndex = index
                        expanded = false
                    }) {
                        Text(text = option)
                    }
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
                Row() {
                    repeat(3) { column ->
                        Column(
                            modifier = Modifier
                                .padding(1.dp)
                                .width(100.dp)
                                .height(120.dp)
                                .background(gridBackgroud),
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
                                textAlign = TextAlign.Center,
                                color = PrimaryText
                            )

                        }
                    }
                }
            }
        }
    }
}