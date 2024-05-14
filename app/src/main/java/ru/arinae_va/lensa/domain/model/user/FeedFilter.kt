package ru.arinae_va.lensa.domain.model.user

data class FeedFilter(
    val specialization: String,
    val searchQuery: String,
    val country: String,
    val city: String,
    val priceFrom: Int,
    val priceTo: Int,
    val order: OrderType,
) {
   companion object {
       val EMPTY = FeedFilter(
           specialization = "",
           searchQuery = "",
           country = "",
           city = "",
           priceFrom = 0,
           priceTo = Int.MAX_VALUE,
           order = OrderType.RATING_DESC,
       )
   }
}

const val ORDER_TYPE_RATING_ASC_TEXT = "По возрастанию"
const val ORDER_TYPE_RATING_DESC_TEXT = "По убыванию"
enum class OrderType(val text: String) {
    RATING_ASC(text = ORDER_TYPE_RATING_ASC_TEXT),
    RATING_DESC(text = ORDER_TYPE_RATING_DESC_TEXT)
}