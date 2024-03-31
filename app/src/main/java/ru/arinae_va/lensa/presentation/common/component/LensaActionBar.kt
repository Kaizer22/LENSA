package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R

@Composable
fun LensaActionBar(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onProfileClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        LensaLogo(
            modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally),
            size = LensaLogoSize.SMALL)
        Row(
            //modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HSpace(w = 16.dp)
            LensaIconButton(
                icon = R.drawable.ic_menu,
                iconSize = 24.dp,
                onClick = onMenuClick
            )
            FSpace()
            LensaIconButton(
                icon = R.drawable.ic_loupe,
                iconSize = 24.dp,
                onClick = onSearchClick,
            )
            HSpace(w = 16.dp)
            LensaIconButton(
                icon = R.drawable.ic_profile,
                iconSize = 24.dp,
                onClick = onProfileClick
            )
            HSpace(w = 16.dp)
        }
    }

}