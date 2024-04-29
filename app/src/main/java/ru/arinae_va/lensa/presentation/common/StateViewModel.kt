package ru.arinae_va.lensa.presentation.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class StateViewModel<T>(
    initialState: T,
): ViewModel() {
    private val _state: MutableStateFlow<T> = MutableStateFlow(initialState)
    val state: StateFlow<T> = _state

    fun update(newState: T) {
        _state.tryEmit(newState)
    }

    suspend fun updateSuspending(newState: T) {
        _state.emit(newState)
    }
}