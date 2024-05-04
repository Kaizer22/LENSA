package ru.arinae_va.lensa.utils

import ru.arinae_va.lensa.domain.model.ORDER_TYPE_RATING_ASC_TEXT
import ru.arinae_va.lensa.domain.model.ORDER_TYPE_RATING_DESC_TEXT

object Constants {
    internal const val RUSSIA_COUNTRY_CODE = "+7"
    internal const val RUSSIAN_PHONE_NUMBER_LENGTH = 12

    internal const val CUSTOMER_SPECIALIZATION = "Заказчик"

    // TODO localization; get automatically
    internal val SPECIALIZATIONS_LIST = listOf(
        Pair("Фотограф", "Фотографы"),
        Pair("Модель", "Модели"),
        Pair("Стилист", "Стилисты"),
        Pair("Визажист","Визажисты"),
        Pair("Гаффер", "Гафферы"),
        Pair("Мастер по ногтям", "Мастера по ногтям"),
        Pair("Мастер по волосам", "Мастера по волосам"),
        Pair("Ассистент съемки", "Ассистенты съемки"),
        Pair("Видеограф", "Видеографы"),
        Pair("Бэкстейджер", "Бэкстейджеры"),
        Pair("Рилсмейкер", "Рилсмейкеры"),
        Pair("Сет-дизайнер", "Сет-дизайнеры"),
        Pair("Монтажер", "Монтажеры"),
        Pair("Фуд-стилист", "Фуд-стилисты"),
        Pair("Ретушер", "Ретушеры"),
        Pair("Графический дизайнер", "Графические дизайнеры"),
        Pair("Моушн-дизайнер", "Моушн-дизайнеры"),
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
    internal val DATE_FORMAT = "dd.MM.yyyy"
    internal val TIME_FORMAT = "HH:mm"
    internal val DAY_OF_WEEK_FORMAT = "E."
    internal val DAY_MONTH_FORMAT = "dd MMM"
}