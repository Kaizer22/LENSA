package ru.arinae_va.lensa.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val datetimeFormatter = DateTimeFormatter.ofPattern(
    Constants.DATETIME_FORMAT
)

fun parseIsoDatetime(dateTime: String) = LocalDateTime.parse(
    dateTime,
    DateTimeFormatter.ISO_DATE_TIME
)

fun formatPrettyDatetime(dateTime: LocalDateTime): String {
    return "-----"
}