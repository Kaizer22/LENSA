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

val dayOfWeekFormatter = DateTimeFormatter.ofPattern(
    Constants.DAY_OF_WEEK_FORMAT
)

val dayMonthFormatter = DateTimeFormatter.ofPattern(
    Constants.DAY_MONTH_FORMAT
)

fun parseIsoDatetime(dateTime: String) = LocalDateTime.parse(
    dateTime,
    DateTimeFormatter.ISO_DATE_TIME
)

fun formatMessageDatetime(dateTime: LocalDateTime) = dateTime.format(
    timeFormatter
)

fun formatPrettyDatetime(dateTime: LocalDateTime): String {
    val now = LocalDateTime.now()
    return when {
        now.minusDays(1) < dateTime -> timeFormatter.format(dateTime)
        now.minusWeeks(1) < dateTime -> dayOfWeekFormatter.format(dateTime)
        now.minusMonths(6) < dateTime -> dayMonthFormatter.format(dateTime)
        else -> dateFormatter.format(dateTime)
    }
}

fun formatChatDate(dateTime: LocalDateTime) = dateTime.format(
    dateFormatter
)