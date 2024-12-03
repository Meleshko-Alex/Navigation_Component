package com.meleha.navcomponent.ui.screens.add

import androidx.lifecycle.ViewModel
import com.meleha.navcomponent.model.ItemsRepository
import com.meleha.navcomponent.ui.screens.action.ActionViewModel.Delegate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository,
) : ViewModel(),  Delegate<AddItemViewModel.ScreenState, String> {

    override suspend fun loadState(): ScreenState {
        return ScreenState()
    }

    override fun showProgress(input: ScreenState): ScreenState {
        return input.copy(isProgressVisible = true)
    }

    override suspend fun execute(action: String) {
        itemsRepository.add(action)
    }

    data class ScreenState(
        val isProgressVisible: Boolean = false,
    )
}