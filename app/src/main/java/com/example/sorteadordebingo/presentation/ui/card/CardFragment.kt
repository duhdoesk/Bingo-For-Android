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
import androidx.compose.material.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.sorteadordebingo.R
import com.example.sorteadordebingo.presentation.theme.*
import com.example.sorteadordebingo.util.*
import dagger.hilt.android.AndroidEntryPoint
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@AndroidEntryPoint
class CardFragment : Fragment() {

    private val viewModel: CardViewModel by viewModels()

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
                            Surface { SetCardView(it) }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SetCardView(state: CardState) {
        when (state) {
            is CardState.Loading -> LoadingScreen()
            else -> DrawnCardScreen(state)
        }
    }

    @Composable
    private fun LoadingScreen() {

    }

    /*
    This function is responsible for composing the whole screen, either composing items itself
    of calling other composable methods
     */
    @Composable
    private fun DrawnCardScreen(state: CardState) {
        val captureController = rememberCaptureController()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Capturable(
                controller = captureController,
                modifier = Modifier.fillMaxHeight(0.70f),
                onCaptured = { bitmap, error ->
                    if (bitmap != null) {
                        context?.let {
                            shareBitmap(
                                bitmap,
                                it,
                                resources.getString(R.string.new_card)
//                                R.string.new_card.toString()
                            )
                        }
                    }

                    if (error != null) {
                        Log.d("CapturingError: ", "$error")
                    }
                }
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.background(MaterialTheme.colors.background)
                ) {
                    DropdownMenu(state)
                    CardMaker(state)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { viewModel.shuffleCard() },
                    elevation = ButtonDefaults.elevation(4.dp),
                    modifier = Modifier.width(200.dp)
                ) {
                    Text(text = stringResource(id = R.string.new_card).uppercase())
                }

                Button(
                    onClick = { captureController.capture() },
                    elevation = ButtonDefaults.elevation(4.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                    modifier = Modifier.width(200.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.share_card).uppercase(),
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    }

    /*
    This function is responsible for composing the dropdown menu where the user
    can select a bingo theme before he draws a card
     */
    @Composable
    private fun DropdownMenu(state: CardState) {

        val options = viewModel.getThemes()
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                readOnly = true,
                value = (state as CardState.DrawnCard).theme.themeName,
                onValueChange = { },
                label = { Text(stringResource(id = R.string.selected_theme)) },
                trailingIcon = { TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(backgroundColor = grid_background),
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            viewModel.setCurrentTheme(selectionOption)
                        }
                    ) { Text(text = selectionOption.themeName) }
                }
            }
        }
    }

    /*
    This function is responsible for composing the drawn card of elements
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Composable
    private fun CardMaker(state: CardState) {
        val drawnElements = (state as CardState.DrawnCard).card
        val elements = listOf(
            listOf(
                drawnElements[0],
                drawnElements[1],
                drawnElements[2]
            ),
            listOf(
                drawnElements[3],
                drawnElements[4],
                drawnElements[5]
            ),
            listOf(
                drawnElements[6],
                drawnElements[7],
                drawnElements[8]
            ),
        )

        Column(
            Modifier
                .padding(vertical = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.background)
        ) {
            repeat(3) { row ->
                Row {
                    repeat(3) { column ->
                        Column(
                            modifier = Modifier
                                .padding(1.dp)
                                .width(80.dp)
                                .height(100.dp)
                                .background(grid_background),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            elements[row][column].elementPicture.let { url ->
                                val image =
                                    loadPicture(url = url, defaultImage = DEFAULT_IMAGE).value
                                image?.let { img ->
                                    Image(
                                        bitmap = img.asImageBitmap(),
                                        contentDescription = "Element image",
                                        Modifier.size(60.dp)
                                    )
                                }
                            }

                            Text(
                                text = elements[row][column].elementName,
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
