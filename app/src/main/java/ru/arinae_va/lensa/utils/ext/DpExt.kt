package ru.arinae_va.lensa.utils.ext

import android.content.Context
import androidx.compose.ui.unit.Dp

fun Dp.toSp(context: Context) =
    this.value * context.resources.displayMetrics.density /
            context.resources.displayMetrics.scaledDensity