package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun LensaExpandableButton(
    modifier: Modifier = Modifier,
    text: String,
    isFillMaxWidth: Boolean = false,
    hideBottomDivider: Boolean = true,
    expandableContent: @Composable () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
    ) {
        Divider(color = LensaTheme.colors.dividerColor)
        LensaButtonWithIcon(
            contentPadding = PaddingValues(
                horizontal = 0.dp,
                vertical = 16.dp,
            ),
            isFillMaxWidth = isFillMaxWidth,
            icon = if (isExpanded) R.drawable.ic_arrow_top
            else R.drawable.ic_arrow_bottom,
            text = text,
            onClick = { isExpanded = !isExpanded }
        )
        if (hideBottomDivider) {
            if (isExpanded) Divider(color = LensaTheme.colors.dividerColor)
        } else Divider(color = LensaTheme.colors.dividerColor)
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            expandableContent()
        }
    }
}