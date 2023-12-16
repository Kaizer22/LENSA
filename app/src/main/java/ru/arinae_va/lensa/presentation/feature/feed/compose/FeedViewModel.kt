package ru.arinae_va.lensa.presentation.feature.feed.compose

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.model.SpecialistModel
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import ru.arinae_va.lensa.presentation.navigation.IS_SELF_PROFILE_KEY
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.presentation.navigation.PROFILE_KEY
import ru.arinae_va.lensa.presentation.navigation.toJson
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val userInfoRepository: IUserInfoRepository,
) : ViewModel() {

    private val _feedList = mutableStateListOf<SpecialistModel>()
    val feedList: SnapshotStateList<SpecialistModel> = _feedList

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
        navHostController.navigate(LensaScreens.SPECIALIST_DETAILS_SCREEN.name)
    }

    fun onRefreshClick() {

    }

    fun onCardClick(userUid: String) {
        val isSelf = false
        feedList.find { it.id == userUid }.let {
            navHostController.navigate(
                "${LensaScreens.SPECIALIST_DETAILS_SCREEN.name}/" +
                        "${it.toJson()}/" +
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
