package ru.arinae_va.lensa.presentation.feature.feed.compose.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaAsyncImage
import ru.arinae_va.lensa.presentation.common.component.LensaRating
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun SpecialistCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    photoUrl: String?,
    rating: Float,
    text: String,
    showFavouritesButton: Boolean = false,
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
    ) {
        LensaAsyncImage(
            modifier = Modifier
                .fillMaxWidth(),
            pictureUrl = photoUrl.orEmpty(),
        )
        Row(modifier = Modifier.padding(top = 12.dp)) {
            Text(
                text = text,
                style = LensaTheme.typography.textButton,
                color = LensaTheme.colors.textColor,
            )
            FSpace()
            HSpace(w = 8.dp)
            LensaRating(
                rating = rating,
            )
        }
    }
}