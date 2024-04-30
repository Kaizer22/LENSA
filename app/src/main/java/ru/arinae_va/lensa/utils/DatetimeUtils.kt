package ru.arinae_va.lensa.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val datetimeFormatter = DateTimeFormatter.ofPattern(
    Constants.DATETIME_FORMAT
)
val dateFormatter = DateTimeFormatter.ofPattern(
    Constants.DATE_FORMAT
)

val timeFormatter = DateTimeFormatter.ofPattern(
    Constants.TIME_FORMAT
)



fun parseIsoDatetime(dateTime: String) = LocalDateTime.parse(
    dateTime,
    DateTimeFormatter.ISO_DATE_TIME
)

fun formatMessageDatetime(dateTime: LocalDateTime) = dateTime.format(
    timeFormatter
)

fun formatPrettyDatetime(dateTime: LocalDateTime): String {
    return "-----"
}

fun formatChatDate(dateTime: LocalDateTime) = dateTime.format(
    dateFormatter
)