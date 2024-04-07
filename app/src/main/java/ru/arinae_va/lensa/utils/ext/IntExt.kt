package ru.arinae_va.lensa.utils.ext

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

val sumFormat = DecimalFormat(
    "###,###,###,###,###.##",
    DecimalFormatSymbols(Locale.ENGLISH)
)

fun Int.toFormattedNumberString() = sumFormat.format(this)
    .replace(',', ' ')