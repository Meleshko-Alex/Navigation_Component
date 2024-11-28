package com.meleha.navcomponent.ui.screens.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meleha.navcomponent.model.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository,
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(ScreenState())
    val stateFlow: StateFlow<ScreenState> = _stateFlow

    private val _exitChanel = Channel<Unit>()
    val exitChannel: ReceiveChannel<Unit> = _exitChanel

    fun add(title: String) {
        viewModelScope.launch {
            _stateFlow.update { it.copy(isAddInProgress = true) }
            itemsRepository.add(title)
            _exitChanel.send(Unit)
        }
    }

    data class ScreenState(
        private val isAddInProgress: Boolean = false,
    ) {
        val isTextInputEnabled: Boolean get() = !isAddInProgress
        val isProgressVisible: Boolean get() = isAddInProgress
        fun isAddButtonEnabled(input: String): Boolean {
            return input.isNotBlank() && !isAddInProgress
        }
    }
}