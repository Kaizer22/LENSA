package ru.arinae_va.lensa.presentation.feature.chat.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(
        ChatListState(
            chats = emptyList(),
            isLoading = true,
        )
    )
    val state: StateFlow<ChatListState> = _state
}