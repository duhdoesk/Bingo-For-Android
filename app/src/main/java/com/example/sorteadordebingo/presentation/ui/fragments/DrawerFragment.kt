package com.example.sorteadordebingo.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sorteadordebingo.presentation.theme.AppTheme
import com.example.sorteadordebingo.presentation.ui.model.BingoViewModel
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
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }

}