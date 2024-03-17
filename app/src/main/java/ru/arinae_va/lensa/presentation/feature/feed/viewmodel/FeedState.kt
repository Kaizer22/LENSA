package ru.arinae_va.lensa.presentation.feature.feed.viewmodel

import ru.arinae_va.lensa.domain.model.FeedFilter
import ru.arinae_va.lensa.domain.model.UserProfileModel

data class FeedState(
    val searchQuery: String,
    val filter: FeedFilter?,
    val feed: List<UserProfileModel>,
) {
    companion object {
        val INITIAL = FeedState(
            searchQuery = "",
            filter = null,
            feed = emptyList(),
        )
    }
}