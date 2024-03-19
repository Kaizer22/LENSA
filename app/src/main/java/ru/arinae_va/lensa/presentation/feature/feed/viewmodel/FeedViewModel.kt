package ru.arinae_va.lensa.presentation.feature.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.model.FeedFilter
import ru.arinae_va.lensa.domain.model.OrderType
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val userInfoRepository: IUserInfoRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(FeedState.INITIAL)
    val state: StateFlow<FeedState> = _state

    fun onAttach() {
        loadFeed()
    }

    private fun loadFeed() {
        viewModelScope.launch {
            val result = userInfoRepository.getFeed(state.value.filter)
            _state.tryEmit(
                state.value.copy(
                    feed = result,
                )
            )
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

    fun onApplyFilterClick() {
        loadFeed()
    }

    fun onClearFilterClick() {
        viewModelScope.launch {
            _state.emit(
                state.value.copy(
                    filter = FeedFilter.EMPTY,
                )
            )
            loadFeed()
        }
    }

    fun onRefreshClick() {
        loadFeed()
    }

    fun onCardClick(userUid: String) {
        val isSelf = false
        state.value.feed.find { it.id == userUid }?.let {
            navHostController.navigate(
                "${LensaScreens.SPECIALIST_DETAILS_SCREEN.name}/" +
                        "${it.id}/" +
                        "$isSelf"
            )
        }
    }

    fun onSearchTextChanged(searchQuery: String) {

    }

    fun onFilterCountryChanged(country: String) {
        _state.tryEmit(
            state.value.copy(
                filter = state.value.filter.copy(country = country)
            )
        )
    }

    fun onFilterCityChanged(city: String) {
        _state.tryEmit(
            state.value.copy(
                filter = state.value.filter.copy(city = city)
            )
        )
    }

    fun onFilterSpecializationChanged(specialization: String) {
        _state.tryEmit(
            state.value.copy(
                filter = state.value.filter.copy(specialization = specialization)
            )
        )
    }

    fun onFilterOrderChanged(orderType: OrderType) {
        _state.tryEmit(
            state.value.copy(
                filter = state.value.filter.copy(order = orderType)
            )
        )
    }

    fun onFilterPriceToChanged(priceTo: Int) {
        _state.tryEmit(
            state.value.copy(
                filter = state.value.filter.copy(priceTo = priceTo)
            )
        )
    }

    fun onFilterPriceFromChanged(priceFrom: Int) {
        _state.tryEmit(
            state.value.copy(
                filter = state.value.filter.copy(priceFrom = priceFrom)
            )
        )
    }
}

