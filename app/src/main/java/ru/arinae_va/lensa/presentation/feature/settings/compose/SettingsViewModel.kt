package ru.arinae_va.lensa.presentation.feature.settings.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.data.datasource.remote.IUserInfoStorage
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userInfoRepository: IUserInfoRepository,
    private val navHostController: NavHostController,
): ViewModel() {
    fun onDeleteClick() {
        viewModelScope.launch {
            userInfoRepository.deleteAccount(
                Firebase.auth.currentUser?.uid ?: "",
            )
            Firebase.auth.signOut()
            navHostController.navigate(LensaScreens.AUTH_SCREEN.name)
        }
    }

    fun onExitClick() {
        viewModelScope.launch {
            Firebase.auth.signOut()
            navHostController.navigate(LensaScreens.AUTH_SCREEN.name)
        }
    }

    fun onSendFeedbackClick(text: String) {
        viewModelScope.launch {
            userInfoRepository.sendFeedback(
                userUid = Firebase.auth.uid,
                text = text,
            )
            navHostController.popBackStack()
        }
    }
}