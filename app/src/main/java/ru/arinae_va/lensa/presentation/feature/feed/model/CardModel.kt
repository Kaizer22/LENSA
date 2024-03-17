package ru.arinae_va.lensa.presentation.feature.feed.model

data class CardModel(
    val userId: String,
    val photoUrl: String,
    val rating: Float,
    val name: String,
    val surname: String,
)