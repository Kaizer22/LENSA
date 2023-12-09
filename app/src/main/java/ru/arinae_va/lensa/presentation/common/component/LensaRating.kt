package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun LensaRating(
    rating: Float,
) {
    Row {
        Text(
            text = rating.toString(),
            style = LensaTheme.typography.textButton,
            color = LensaTheme.colors.textColor,
        )
        HSpace(w = 8.dp)
        Icon(
            painter = painterResource(id = R.drawable.ic_5_star),
            contentDescription = null,
            tint = LensaTheme.colors.textColor,
        )
    }
}