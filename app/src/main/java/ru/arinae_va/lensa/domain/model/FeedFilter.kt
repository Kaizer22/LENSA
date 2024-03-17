package ru.arinae_va.lensa.domain.model

data class FeedFilter(
    val specialization: String,
    val searchQuery: String,
    val country: String,
    val city: String,
    val priceFrom: Int,
    val priceTo: Int,
    val ratingOrder: OrderType,
)

const val ORDER_TYPE_RATING_ASC_TEXT = "По возрастанию"
const val ORDER_TYPE_RATING_DESC_TEXT = "По убыванию"
enum class OrderType(val text: String) {
    RATING_ASC(text = ORDER_TYPE_RATING_ASC_TEXT),
    RATING_DESC(text = ORDER_TYPE_RATING_DESC_TEXT)
}