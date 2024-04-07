package ru.arinae_va.lensa.presentation.feature.feed.compose.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaAsyncImage
import ru.arinae_va.lensa.presentation.common.component.LensaRating
import ru.arinae_va.lensa.presentation.common.component.LensaStateButton
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun SpecialistCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    photoUrl: String?,
    rating: Float,
    text: String,
    showFavouritesButton: Boolean = false,
    onShowFavouriteButtonClick: (Boolean) -> Unit = {},
) {
    Box(contentAlignment = Alignment.TopEnd) {
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
                if (rating != 0f) {
                    LensaRating(
                        rating = rating,
                    )
                }
            }
        }
        if (showFavouritesButton) {
            var favouriteButtonEnabled by remember { mutableStateOf(true)}
            LensaStateButton(
                modifier = Modifier.padding(
                    top = 20.dp,
                    end = 20.dp,
                ),
                enabled = favouriteButtonEnabled,
                onClick = { enabled ->
                    favouriteButtonEnabled = enabled
                    onShowFavouriteButtonClick.invoke(enabled)
                },
                iconEnabledRes = R.drawable.ic_heart_filled,
                iconDisabledRes = R.drawable.ic_heart_outlined,
            )
        }
    }
}