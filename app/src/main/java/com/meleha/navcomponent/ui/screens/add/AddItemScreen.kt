package com.meleha.navcomponent.ui.screens.add

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.meleha.navcomponent.R
import com.meleha.navcomponent.ui.components.ItemDetailState
import com.meleha.navcomponent.ui.components.ItemDetails
import com.meleha.navcomponent.ui.screens.action.ActionScreen
import com.meleha.navcomponent.ui.screens.add.AddItemViewModel.ScreenState

@Composable
fun AddItemScreen() {
    val viewModel: AddItemViewModel = hiltViewModel()
    ActionScreen(
        delegate = viewModel,
        content = { (screenState, onExecuteAction) ->
            AddItemContent(screenState, onExecuteAction)
        }
    )
}

@Composable
fun AddItemContent(
    screenState: ScreenState,
    onAddButtonClicked: (String) -> Unit,
) {
    ItemDetails(
        state = ItemDetailState(
            loadedItem = "",
            textFieldPlaceHolder = stringResource(id = R.string.enter_new_item),
            actionButtonText = stringResource(id = R.string.add),
            isActionInProgress = screenState.isProgressVisible
        ),
        onActionButtonClicked = onAddButtonClicked,
        modifier = Modifier.fillMaxSize()
    )
}