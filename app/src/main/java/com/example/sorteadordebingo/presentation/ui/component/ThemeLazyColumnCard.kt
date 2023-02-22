package com.example.sorteadordebingo.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.sorteadordebingo.R
import com.example.sorteadordebingo.data.Theme
import com.example.sorteadordebingo.util.loadPicture
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalMaterialApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun ThemeLazyColumnCard(theme: Theme, onClick: () -> Unit) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = 4.dp,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = theme.themeName,
                modifier = Modifier
                    .padding(start = 16.dp)
            )

            theme.themePicture.let {
                val picture = loadPicture(
                    url = it,
                    defaultImage = R.drawable.default_placeholder
                ).value

                picture?.let { pic ->
                    Image(
                        bitmap = pic.asImageBitmap(),
                        contentDescription = "Theme Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(120.dp, 80.dp)
                    )
                }
            }
        }
    }
}