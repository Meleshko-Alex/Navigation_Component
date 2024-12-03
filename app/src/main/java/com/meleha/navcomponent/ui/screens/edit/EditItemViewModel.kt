package com.meleha.navcomponent.ui.screens.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meleha.navcomponent.model.ItemsRepository
import com.meleha.navcomponent.model.LoadResult
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

    private val _stateFlow = MutableStateFlow<LoadResult<ScreenState>>(LoadResult.Loading)
    var stateFlow: StateFlow<LoadResult<ScreenState>> = _stateFlow

    private val _exitChanel = Channel<Unit>()
    val exitChanel: ReceiveChannel<Unit> = _exitChanel

    init {
        viewModelScope.launch {
            val loadIem = itemsRepository.getByIndex(index)
            _stateFlow.value = LoadResult.Success(ScreenState(loadIem))
        }
    }

    fun update(newValue: String) {
        val loadResult = _stateFlow.value
        if (loadResult !is LoadResult.Success) return
        viewModelScope.launch {
            showProgress(loadResult)
            itemsRepository.update(index, newValue)
            goBack()
        }
    }

    private fun showProgress(loadResult: LoadResult.Success<ScreenState>) {
        val currentScreenState = loadResult.data
        val updatedScreenState = currentScreenState.copy(isEditInProgress = true)
        _stateFlow.value = LoadResult.Success(updatedScreenState)
    }

    private suspend fun goBack() {
        _exitChanel.send(Unit)
    }

    data class ScreenState(
        val loadingItem: String,
        val isEditInProgress: Boolean = false
    )

    @AssistedFactory
    interface Factory {
        fun create(index: Int): EditItemViewModel
    }
}