package ru.arinae_va.lensa.presentation.feature.feed.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaIconButton
import ru.arinae_va.lensa.presentation.common.component.LensaRating
import ru.arinae_va.lensa.presentation.common.component.LensaStateButton
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.feature.feed.viewmodel.ProfileDetailsState
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import java.util.Locale

@Composable
fun HeaderSection(
    state: ProfileDetailsState,
    onBackPressed: () -> Unit,
    onFavouritesClick: () -> Unit,
    onChatsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAddToFavouritesClick: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    Row {
        LensaIconButton(
            onClick = onBackPressed,
            icon = R.drawable.ic_arrow_back,
            iconSize = 30.dp,
        )
        FSpace()
        if (state.isSelf) {
            LensaIconButton(
                onClick = onFavouritesClick,
                icon = R.drawable.ic_heart_outlined,
                iconSize = 28.dp
            )
            HSpace(w = 16.dp)
            LensaIconButton(
                onClick = onChatsClick,
                icon = R.drawable.ic_chat,
                iconSize = 28.dp
            )
            HSpace(w = 16.dp)
            LensaIconButton(
                onClick = onSettingsClick,
                icon = R.drawable.ic_settings,
                iconSize = 28.dp
            )
        } else {
            LensaStateButton(
                enabled = state.isAddedToFavourites,
                onClick = onAddToFavouritesClick,
                iconEnabledRes = R.drawable.ic_heart_filled,
                iconDisabledRes = R.drawable.ic_heart_outlined
            )
            HSpace(w = 16.dp)
        }
    }
    VSpace(h = 24.dp)
    Text(
        text = state.userProfileModel.surname.uppercase(Locale.ROOT) + "\n" +
                state.userProfileModel.name.uppercase(Locale.ROOT),
        style = LensaTheme.typography.header2,
        color = LensaTheme.colors.textColor,
    )
    VSpace(h = 4.dp)
    Row {
        Text(
            text = state.userProfileModel.specialization,
            style = LensaTheme.typography.header3,
            color = LensaTheme.colors.textColor,
        )
        FSpace()
        state.userProfileModel.rating?.let { rating ->
            if (rating != 0f) {
                LensaRating(rating = rating)
            }
        }
    }
}