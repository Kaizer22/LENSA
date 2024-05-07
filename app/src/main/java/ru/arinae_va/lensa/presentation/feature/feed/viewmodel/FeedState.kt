package ru.arinae_va.lensa.presentation.feature.feed.viewmodel

import ru.arinae_va.lensa.domain.model.FeedFilter
import ru.arinae_va.lensa.domain.model.UserProfileModel

enum class FeedFilterInputFields {
    SPECIALIZATION, COUNTRY, CITY,
    PRICE_FROM, PRICE_TO,
    SORT_TYPE,
}
data class FeedState(
    val filter: FeedFilter,
    val filterValidationErrors: Map<FeedFilterInputFields, String>,
    val isApplyFilterButtonEnabled: Boolean,
    val feed: List<UserProfileModel>,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
) {
    companion object {
        val INITIAL = FeedState(
            filter = FeedFilter.EMPTY,
            feed = emptyList(),
            isApplyFilterButtonEnabled = true,
            isLoading = false,
            isRefreshing = false,
            filterValidationErrors = emptyMap(),
        )
    }
}