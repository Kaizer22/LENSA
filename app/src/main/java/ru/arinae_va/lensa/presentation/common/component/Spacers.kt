package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.FSpace() = Spacer(modifier = Modifier.weight(1f))
@Composable
fun VSpace(h: Dp) = Space(0.dp, h)
@Composable
fun HSpace(w: Dp) = Space(w, 0.dp)
@Composable
fun Space(
    w: Dp = 0.dp,
    h: Dp = 0.dp,
) = Spacer(modifier = Modifier.size(w, h))