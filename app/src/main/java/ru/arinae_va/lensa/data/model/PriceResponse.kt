package ru.arinae_va.lensa.data.model

import ru.arinae_va.lensa.domain.model.user.PriceCurrency

data class PriceResponse(
    val id: String? = null,
    var name: String? = null,
    var text: String? = null,
    var price: Int? = null,
    val currency: PriceCurrency? = null,
)