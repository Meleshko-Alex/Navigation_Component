package com.meleha.navcomponent.ui.screens.action

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meleha.navcomponent.ui.components.ExceptionToMessageMapper
import com.meleha.navcomponent.ui.components.LoadResultContent
import com.meleha.navcomponent.ui.screens.EventConsumer
import com.meleha.navcomponent.ui.screens.LocalNavController
import com.meleha.navcomponent.ui.screens.action.ActionViewModel.Delegate
import com.meleha.navcomponent.ui.screens.routeClass

data class ActionContentState<State, Action>(
    val state: State,
    val onExecuteAction: (Action) -> Unit,
)

@Composable
fun <State, Action> ActionScreen(
    delegate: Delegate<State, Action>,
    content: @Composable (ActionContentState<State, Action>) -> Unit,
    modifier: Modifier = Modifier,
    exceptionToMessageMapper: ExceptionToMessageMapper = ExceptionToMessageMapper.Default
) {
    val viewModel = viewModel<ActionViewModel<State, Action>> {
        ActionViewModel(delegate)
    }
    val navController = LocalNavController.current
    val screenState by viewModel.screenStateFlow.collectAsStateWithLifecycle(
        minActiveState = Lifecycle.State.RESUMED
    )

    LaunchedEffect(screenState) {
        screenState.exit?.let {
            navController.popBackStack()
            viewModel.onExitHandled()
        }
    }
    /*EventConsumer(channel = viewModel.exitChanel) {
        navController.popBackStack()
    }*/


    val context = LocalContext.current
    LaunchedEffect(screenState) {
        screenState.error?.let { exception ->
            val message = exceptionToMessageMapper.getUserMessage(exception, context)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.onErrorHandled()
        }
    }
    /*EventConsumer(channel = viewModel.errorChanel) { exception ->
        val message = exceptionToMessageMapper.getUserMessage(exception, context)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }*/
    val loadResult by viewModel.stateFlow.collectAsState()
    LoadResultContent(
        loadResult = loadResult,
        content = { state ->
            val actionContentState = ActionContentState(
                state = state,
                onExecuteAction = viewModel::execute,
            )
            content(actionContentState)
        },
        modifier = modifier,
        onTryAgainAction = viewModel::load,
    )
}