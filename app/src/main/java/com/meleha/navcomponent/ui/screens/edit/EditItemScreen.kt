package com.meleha.navcomponent.ui.screens.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.meleha.navcomponent.R
import com.meleha.navcomponent.model.LoadResult
import com.meleha.navcomponent.ui.components.ItemDetailState
import com.meleha.navcomponent.ui.components.ItemDetails
import com.meleha.navcomponent.ui.components.LoadResultContent
import com.meleha.navcomponent.ui.screens.EditItemRoute
import com.meleha.navcomponent.ui.screens.EventConsumer
import com.meleha.navcomponent.ui.screens.LocalNavController
import com.meleha.navcomponent.ui.screens.edit.EditItemViewModel.ScreenState
import com.meleha.navcomponent.ui.screens.routeClass

@Composable
fun EditItemScreen(index: Int) {
    val viewModel = hiltViewModel<EditItemViewModel, EditItemViewModel.Factory> { factory ->
        factory.create(index)
    }
    val navController = LocalNavController.current
    EventConsumer(channel = viewModel.exitChanel) {
        if (navController.currentBackStackEntry.routeClass() == EditItemRoute::class) {
            navController.popBackStack()
        }
    }
    val loadResult by viewModel.stateFlow.collectAsState()
    EditItemContent(loadResult = loadResult, onEditButtonClicked = viewModel::update)
}

@Composable
fun EditItemContent(
    loadResult: LoadResult<ScreenState>,
    onEditButtonClicked: (String) -> Unit,
) {
    LoadResultContent(
        loadResult = loadResult,
        content = { screenState ->
            SuccessEditItemContent(
                state = screenState,
                onEditButtonClicked = onEditButtonClicked
            )
        }
    )
}


@Composable
private fun SuccessEditItemContent(
    state: ScreenState,
    onEditButtonClicked: (String) -> Unit,
) {
    ItemDetails(
        state = ItemDetailState(
            loadedItem = state.loadingItem,
            textFieldPlaceHolder = stringResource(id = R.string.edit_item),
            actionButtonText = stringResource(id = R.string.edit),
            isActionInProgress = state.isEditInProgress
        ),
        onActionButtonClicked = onEditButtonClicked,
        modifier = Modifier.fillMaxSize()
    )
}