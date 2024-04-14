package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LensaSearchBar(
    modifier: Modifier = Modifier,
    placeholder: String = "Стилист по маникюру",
    onSearchTextChanged: (String) -> Unit,
    onLeadingIconClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .animateContentSize()
    ) {

    }
}