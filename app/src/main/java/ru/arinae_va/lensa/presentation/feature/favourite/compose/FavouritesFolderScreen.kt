package ru.arinae_va.lensa.presentation.feature.favourite.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.feature.favourite.viewmodel.FavouritesFolderState
import ru.arinae_va.lensa.presentation.feature.favourite.viewmodel.FavouritesFolderViewModel
import ru.arinae_va.lensa.presentation.feature.feed.compose.component.SpecialistCard
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun FavouritesFolderScreen(
    viewModel: FavouritesFolderViewModel,
) {
    val state by viewModel.state.collectAsState()
    FavouritesFolderContent(
        state = state,
        onSpecialistCardClick = viewModel::onProfileClicked,

    )
}

@Composable
fun FavouritesFolderContent(
    state: FavouritesFolderState,
    onSpecialistCardClick: (String) -> Unit,
) {
    Column {
        VSpace(h = 24.dp)
        Text(
            text = state.folderName,
            style = LensaTheme.typography.header2,
            color = LensaTheme.colors.textColor,
        )
        VSpace(h = 12.dp)
        Divider(color = LensaTheme.colors.dividerColor)
        VSpace(h = 24.dp)
        state.folderItems.forEach { profile ->
            SpecialistCard(
                onClick = { onSpecialistCardClick.invoke(profile.id) },
                photoUrl = profile.avatarUrl,
                rating = profile.rating ?: 0f,
                text = "${profile.surname} ${profile.name}"
            )
        }
    }
}