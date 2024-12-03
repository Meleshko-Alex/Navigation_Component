package com.meleha.navcomponent.ui.screens.edit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.meleha.navcomponent.R
import com.meleha.navcomponent.ui.components.ItemDetailState
import com.meleha.navcomponent.ui.components.ItemDetails
import com.meleha.navcomponent.ui.screens.action.ActionScreen
import com.meleha.navcomponent.ui.screens.edit.EditItemViewModel.ScreenState

@Composable
fun EditItemScreen(index: Int) {
    val viewModel = hiltViewModel<EditItemViewModel, EditItemViewModel.Factory> { factory ->
        factory.create(index)
    }
    ActionScreen(
        delegate = viewModel,
        content = { (screenState, onExecuteAction) ->
            EditItemContent(screenState, onExecuteAction)
        })
}

@Composable
private fun EditItemContent(
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