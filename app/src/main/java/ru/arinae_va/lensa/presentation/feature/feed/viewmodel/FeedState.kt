package ru.arinae_va.lensa.presentation.feature.feed.viewmodel

import ru.arinae_va.lensa.domain.model.FeedFilter
import ru.arinae_va.lensa.domain.model.UserProfileModel

internal enum class FeedFilterInputFields {
    SPECIALIZATION, COUNTRY, CITY,
    PRICE_FROM, PRICE_TO,
    SORT_TYPE,
}
internal data class FeedState(
    val searchQuery: String,
    val filter: FeedFilter,
    val filterValidationErrors: Map<FeedFilterInputFields, String>,
    val isApplyFilterButtonEnabled: Boolean,
    val feed: List<UserProfileModel>,
    val isLoading: Boolean,
) {
    companion object {
        val INITIAL = FeedState(
            searchQuery = "",
            filter = FeedFilter.EMPTY,
            feed = emptyList(),
            isApplyFilterButtonEnabled = true,
            isLoading = false,
            filterValidationErrors = emptyMap(),
        )
    }
}