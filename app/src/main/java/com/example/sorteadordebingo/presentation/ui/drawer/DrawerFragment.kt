@file:OptIn(ExperimentalMaterialApi::class, ExperimentalCoroutinesApi::class)

package com.example.sorteadordebingo.presentation.ui.drawer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.sorteadordebingo.R
import com.example.sorteadordebingo.data.Theme
import com.example.sorteadordebingo.presentation.ui.component.ThemeLazyColumnCard
import com.example.sorteadordebingo.presentation.theme.AppTheme
import com.example.sorteadordebingo.util.DEFAULT_IMAGE
import com.example.sorteadordebingo.util.loadPicture
import com.google.android.material.snackbar.Snackbar
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
    private fun SetDrawerView(drawState: DrawState) {
        val themes = viewModel.getThemes()

        if (themes.isNotEmpty()) {
            when (drawState) {
                is DrawState.NotStarted -> {
                    StateNotStarted(themes)
                }
                is DrawState.Loading -> {
                    Loading()
                }
                is DrawState.Waiting -> {
                    Waiting()
                }
                else -> {
                    StateNextElement(drawState)
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
                                viewModel.waitingForStart(theme)
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun Loading() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.please_wait),
                textAlign = TextAlign.Center
            )
        }
    }

    @Preview
    @Composable
    fun Waiting() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 8.dp)
        ) {
            val theme = viewModel.getCurrentTheme()
            val image = loadPicture(url = theme.themePicture, defaultImage = DEFAULT_IMAGE).value

            image?.let { img ->
                Image(
                    bitmap = img.asImageBitmap(),
                    contentDescription = "Theme Picture.",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .padding(top = 16.dp)
                        .clip(MaterialTheme.shapes.small)
                )
            }

            Text(
                text = "${stringResource(id = R.string.selected_theme)} ${theme.themeName.uppercase()}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 2.dp)
            )

            Button(
                onClick = { viewModel.startDraw(theme) },
                Modifier
                    .fillMaxWidth(0.5f)
                    .padding(top = 64.dp)
            ) {
                Text(text = stringResource(id = R.string.start_draw_button).uppercase())
            }

            Button(
                onClick = { viewModel.resetDraw() },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondaryVariant),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            ) {
                Text(text = stringResource(id = R.string.change_theme).uppercase())
            }
        }
    }

    @Composable
    fun StateNextElement(drawState: DrawState) {
        val element = when (drawState) {
            is DrawState.Drawing -> drawState.nextElement
            else -> {
                (drawState as DrawState.Finished).nextElement
            }
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 8.dp)
        ) {
            Text(
                text = "${stringResource(id = R.string.selected_theme)} ${viewModel.getCurrentTheme().themeName}",
                color = MaterialTheme.colors.secondaryVariant
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${viewModel.getAmountOfDrawnElements()} / ${viewModel.getAmountOfElements()}",
                    color = MaterialTheme.colors.primary
                )

                element.let {
                    val image =
                        loadPicture(url = it.elementPicture, defaultImage = DEFAULT_IMAGE).value

                    image?.let { img ->
                        Image(
                            bitmap = img.asImageBitmap(),
                            contentDescription = "Element picture.",
                            modifier = Modifier
                                .size(200.dp)
                                .padding(top = 16.dp, bottom = 8.dp)
                        )
                    }

                    Text(
                        text = it.elementName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                when (drawState) {
                    is DrawState.Drawing -> {
                        StillDrawing()
                    }
                    else -> {
                        DrawingDone()
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.drawn),
                    color = MaterialTheme.colors.secondaryVariant
                )

                ElementsDrawnLazyRow()
            }
        }
    }

    @Composable
    fun StillDrawing() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { viewModel.drawNextElement() },
                Modifier.fillMaxWidth(0.5f)
            ) {
                Text(text = stringResource(id = R.string.next_element_button).uppercase())
            }

            Button(
                onClick = { viewModel.resetDraw() },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondaryVariant),
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text(text = stringResource(id = R.string.finish_draw_button).uppercase())
            }
        }
    }

    @Composable
    fun DrawingDone() {
        Text(
            text = stringResource(id = R.string.all_elements_were_drawn),
            textAlign = TextAlign.Center
        )

        Button(
            onClick = { viewModel.resetDraw() },
            Modifier
                .fillMaxWidth(0.5f)
                .padding(top = 12.dp)
        ) {
            Text(text = stringResource(id = R.string.new_draw_button).uppercase())
        }
    }

    /*
    This function is responsible for composing the lazy row that shows all the
    already drawn elements of the session
     */
    @Composable
    private fun ElementsDrawnLazyRow() {
        val elements = viewModel.getDrawnElements().reversed()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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
                            text = element.elementName.uppercase(),
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

    private fun stringOfElementsDrawn(elements: List<com.example.sorteadordebingo.data.Element>?): String {
        var string = ("*${viewModel.getCurrentTheme().themeName} ${R.string.drawn_ptbr} \n\n").uppercase()

        if (elements != null) {
            for (element in elements) {
                string += element.elementName.plus("\n")
            }
        }

        return string
    }

    private fun copyToClipboard(string: String) {
        val clipboardManager =
            activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("${R.string.drawn_elements}", string)
        clipboardManager.setPrimaryClip(clipData)

        view?.let {
            Snackbar.make(
                it,
                "${R.string.list_copied}",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

}