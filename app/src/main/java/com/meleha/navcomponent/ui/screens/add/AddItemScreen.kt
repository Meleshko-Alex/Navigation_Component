package com.meleha.navcomponent.ui.screens.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meleha.navcomponent.R
import com.meleha.navcomponent.ui.screens.AddItemRoute
import com.meleha.navcomponent.ui.screens.EventConsumer
import com.meleha.navcomponent.ui.screens.LocalNavController
import com.meleha.navcomponent.ui.screens.add.AddItemViewModel.ScreenState
import com.meleha.navcomponent.ui.screens.routeClass

@Composable
fun AddItemScreen() {
    val viewModel: AddItemViewModel = hiltViewModel()
    val screenState by viewModel.stateFlow.collectAsState()
    AddItemContent(
        screenState = screenState,
        onAddButtonClicked = viewModel::add,
    )
    val navController = LocalNavController.current
    EventConsumer(viewModel.exitChannel) {
        if (navController.currentBackStackEntry.routeClass() == AddItemRoute::class) {
            navController.popBackStack()
        }
    }
}

@Composable
fun AddItemContent(
    screenState: ScreenState,
    onAddButtonClicked: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        var inputText by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            placeholder = { Text(text = stringResource(R.string.enter_new_item)) },
            enabled = screenState.isTextInputEnabled,
        )
        Button(
            onClick = { onAddButtonClicked(inputText) },
            enabled = screenState.isAddButtonEnabled(inputText)
        ) {
            Text(text = stringResource(R.string.add))
        }
        Box(modifier = Modifier.size(32.dp)) {
            if (screenState.isProgressVisible) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }
        }
    }
}