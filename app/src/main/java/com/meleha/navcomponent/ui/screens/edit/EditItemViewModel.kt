package com.meleha.navcomponent.ui.screens.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meleha.navcomponent.model.ItemsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditItemViewModel.Factory::class)
class EditItemViewModel @AssistedInject constructor(
    @Assisted private val index: Int,
    private val itemsRepository: ItemsRepository,
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<ScreenState>(ScreenState.Loading)
    var stateFlow: StateFlow<ScreenState> = _stateFlow

    private val _exitChanel = Channel<Unit>()
    val exitChanel: ReceiveChannel<Unit> = _exitChanel

    init {
        viewModelScope.launch {
            val loadIem = itemsRepository.getByIndex(index)
            _stateFlow.value = ScreenState.Success(loadIem )
        }
    }

    fun update(newValue: String) {
        val currentState = _stateFlow.value
        if (currentState !is ScreenState.Success) return
        viewModelScope.launch {
            _stateFlow.value = currentState.copy(isEditInProgress = true)
            itemsRepository.update(index, newValue)
            _exitChanel.send(Unit)
        }
    }

    sealed class ScreenState {
        data object Loading : ScreenState()
        data class Success(
            val loadingItem: String,
            val isEditInProgress: Boolean = false
        ) : ScreenState()
    }

    @AssistedFactory
    interface Factory {
        fun create(index: Int): EditItemViewModel
    }
}