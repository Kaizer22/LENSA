package ru.arinae_va.lensa.utils

import ru.arinae_va.lensa.domain.model.ORDER_TYPE_RATING_ASC_TEXT
import ru.arinae_va.lensa.domain.model.ORDER_TYPE_RATING_DESC_TEXT

object Constants {
    internal const val RUSSIA_COUNTRY_CODE = "+7"
    internal const val RUSSIAN_PHONE_NUMBER_LENGTH = 12

    internal const val CUSTOMER_SPECIALIZATION = "Заказчик"

    // TODO localization; get automatically
    internal val SPECIALIZATIONS_LIST = listOf(
        "Фотограф",
        "Модель",
        "Стилист",
        "Визажист",
        "Гаффер",
        "Мастер по ногтям",
        "Мастер по волосам",
        "Ассистент съемки",
        "Видеограф",
        "Бэкстейджер",
        "Рилсмейкер",
        "Сет-дизайнер",
        "Монтажер",
        "Фуд-стилист",
        "Ретушер",
        "Графический дизайнер",
        "Моушн-дизайнер"
    )

    internal val RUSSIAN_CITIES_LIST = listOf(
        "Москва",
        "Санкт-Петербург",
        "Новосибирск",
        "Екатеринбург",
        "Казань",
        "Нижний Новгород",
        "Красноярск",
        "Челябинск",
        "Самара",
        "Уфа",
        "Ростов-на-Дону",
        "Краснодар",
        "Омск",
        "Воронеж",
        "Пермь"
    )

    // TODO add countries and cities; how to get automatically?
    internal val COUNTRIES_LIST = listOf(
        "Россия",
    )


    internal val SORT_OPTIONS = listOf(
        ORDER_TYPE_RATING_ASC_TEXT,
        ORDER_TYPE_RATING_DESC_TEXT,
    )

    internal val DATETIME_FORMAT = "dd.MM.yyyy HH:mm"
}