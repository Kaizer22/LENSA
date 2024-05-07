package ru.arinae_va.lensa.presentation.feature.feed.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.domain.model.FeedFilter
import ru.arinae_va.lensa.domain.model.OrderType
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import ru.arinae_va.lensa.presentation.common.StateViewModel
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context,
    private val navHostController: NavHostController,
    private val userProfileRepository: IUserProfileRepository,
) : StateViewModel<FeedState>(
    initialState = FeedState.INITIAL,
) {
    private var lastSearchQueryChange: Long = 0

    fun onAttach() {
        loadFeed()
    }

    private fun loadFeed() {
        viewModelScope.launch(ioDispatcher) {
            setLoading(true)
            val result = userProfileRepository.getFeed(state.value.filter)
            updateSuspending(
                state.value.copy(
                    feed = result,
                )
            )
            setLoading(false)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        update(
            state.value.copy(
                isLoading = isLoading,
            )
        )
    }

    private fun setRefreshing(isRefreshing: Boolean) {
        update(
            state.value.copy(
                isRefreshing = isRefreshing,
            )
        )
    }

    fun onProfileClick() {
        val isSelf = true
        userProfileRepository.currentProfileId().let {
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
            updateSuspending(
                state.value.copy(
                    filter = FeedFilter.EMPTY,
                )
            )
            loadFeed()
        }
    }

    fun onRefreshClick() {
        viewModelScope.launch(ioDispatcher) {
            setRefreshing(true)
            val result = userProfileRepository.getFeed(state.value.filter)
            delay(2000L)
            updateSuspending(
                state.value.copy(
                    feed = result,
                )
            )
            setRefreshing(false)
        }
    }

    fun onCardClick(profileUid: String) {
        val isSelf = false
        state.value.feed.find { it.profileId == profileUid }?.let {
            navHostController.navigate(
                "${LensaScreens.SPECIALIST_DETAILS_SCREEN.name}/" +
                        "${it.profileId}/" +
                        "$isSelf"
            )
        }
    }

    fun onSearchTextChanged(searchQuery: String) {
        lastSearchQueryChange = System.currentTimeMillis()
        update(
            state.value.copy(
                filter = state.value.filter.copy(searchQuery = searchQuery)
            )
        )
        loadFeed()
    }



    fun onFilterCountryChanged(country: String) {
        update(
            state.value.copy(
                filter = state.value.filter.copy(country = country)
            )
        )
    }

    fun onFilterCityChanged(city: String) {
        update(
            state.value.copy(
                filter = state.value.filter.copy(city = city)
            )
        )
    }

    fun onFilterSpecializationChanged(specialization: String) {
        update(
            state.value.copy(
                filter = state.value.filter.copy(specialization = specialization)
            )
        )
    }

    fun onFilterOrderChanged(orderType: OrderType) {
        update(
            state.value.copy(
                filter = state.value.filter.copy(order = orderType)
            )
        )
    }

    fun onFilterPriceToChanged(priceTo: Int) {
        val validationErrors = validatePriceRange(state.value.filter.priceFrom, priceTo)
        update(
            state.value.copy(
                filter = state.value.filter.copy(
                    priceTo = priceTo
                ),
                filterValidationErrors = validationErrors,
                isApplyFilterButtonEnabled = validationErrors.isEmpty(),
            )
        )
    }

    fun onFilterPriceFromChanged(priceFrom: Int) {
        val validationErrors = validatePriceRange(priceFrom, state.value.filter.priceTo)
        update(
            state.value.copy(
                filter = state.value.filter.copy(
                    priceFrom = priceFrom,
                ),
                filterValidationErrors = validationErrors,
                isApplyFilterButtonEnabled = validationErrors.isEmpty(),
            )
        )
    }

    private fun validatePriceRange(priceFrom: Int, priceTo: Int):
            Map<FeedFilterInputFields, String> {
        val errors = mutableMapOf<FeedFilterInputFields, String>()
        if (priceTo < priceFrom)
            errors[FeedFilterInputFields.PRICE_TO] =
                context.getString(R.string.feed_filter_price_error)
        return errors
    }
}

