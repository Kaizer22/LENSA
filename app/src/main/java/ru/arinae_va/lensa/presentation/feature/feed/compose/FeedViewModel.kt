package ru.arinae_va.lensa.presentation.feature.feed.compose

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.model.UserProfileModel
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val userInfoRepository: IUserInfoRepository,
) : ViewModel() {

    private val _feedList = mutableStateListOf<UserProfileModel>()
    val feedList: SnapshotStateList<UserProfileModel> = _feedList

    init {
        loadFeed()
    }

    fun loadFeed(
        filter: FeedFilter? = null,
    ) {
        viewModelScope.launch {
            val result = userInfoRepository.getFeed()
            _feedList.clear()
            _feedList.addAll(result)
        }
    }

    fun onProfileClick() {
        val isSelf = true
        Firebase.auth.currentUser?.uid?.let {
            navHostController.navigate(
                "${LensaScreens.SPECIALIST_DETAILS_SCREEN.name}/" +
                        "$it/" +
                        "$isSelf"
            )
        }
    }

    fun onRefreshClick() {

    }

    fun onCardClick(userUid: String) {
        val isSelf = false
        feedList.find { it.id == userUid }?.let {
            navHostController.navigate(
                "${LensaScreens.SPECIALIST_DETAILS_SCREEN.name}/" +
                        "${it.id}/" +
                        "$isSelf"
            )
        }

    }
}

data class FeedFilter(
    val spec: String,
    val searchQuery: String,
    val country: String,
    val city: String,
    val priceFrom: Int,
    val priceTo: Int,
    val ratingOrder: RatingOrderType,
)

enum class RatingOrderType {
    ASC, DESC,
}
