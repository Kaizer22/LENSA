package ru.arinae_va.lensa.utils.ext

import android.content.Context
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

fun TextUnit.toDp(context: Context) =
    (this.value * context.resources.displayMetrics.scaledDensity /
            context.resources.displayMetrics.density).dp
