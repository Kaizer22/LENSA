package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.theme.LensaTheme

enum class LensaLogoSize {
    BIG, SMALL
}

@Composable
fun LensaLogo(
    modifier: Modifier = Modifier,
    size: LensaLogoSize = LensaLogoSize.BIG,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = when(size){
                LensaLogoSize.BIG -> LensaTheme.typography.logoBig
                LensaLogoSize.SMALL -> LensaTheme.typography.logoSmall
            }
        )
    }
}