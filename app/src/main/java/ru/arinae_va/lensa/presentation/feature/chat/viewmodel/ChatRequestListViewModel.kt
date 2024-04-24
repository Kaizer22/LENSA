package ru.arinae_va.lensa.presentation.feature.chat.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChatRequestListViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(ChatRequestListState())
    val state: StateFlow<ChatRequestListState> = _state
}