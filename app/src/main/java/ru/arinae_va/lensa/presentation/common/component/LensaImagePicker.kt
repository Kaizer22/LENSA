package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.theme.LensaShapes
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun LensaImagePicker(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isCancelIconVisible: Boolean = false,
    emptyStateButtonSize: Dp = 40.dp,
) {
    val emptyModifier = modifier.border(
            width = 1.dp,
            color = LensaTheme.colors.textColor,
            shape = LensaTheme.shapes.noRoundedCornersShape,
    )
    var isEmpty by remember { mutableStateOf(true)}
    Box(
        modifier = if (isEmpty) emptyModifier else modifier,
        contentAlignment = Alignment.Center,
    ) {
        if (isEmpty) {
            Box(
                modifier = Modifier
                    .size(emptyStateButtonSize)
                    .background(
                        color = LensaTheme.colors.textColorAccent,
                        shape = LensaTheme.shapes.roundShape,
                    )
                    .border(
                        width = 1.dp,
                        color = LensaTheme.colors.textColor,
                        shape = LensaTheme.shapes.roundShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.size(emptyStateButtonSize / 2),
                    painter = painterResource(id = R.drawable.il_plus),
                    contentDescription = null,
                    tint = LensaTheme.colors.textColor,
                )
            }
        }
    }
}