package ru.arinae_va.lensa.domain.model.user

import java.util.UUID

data class Price(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var text: String,
    var price: Int,
    val currency: PriceCurrency,
)