package ru.arinae_va.lensa.presentation.feature.favourite.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        onChangeFavouriteStatus = viewModel::onRemoveProfileClicked,
        onBackPressed = viewModel::onBackPressed,
    )
}

@Composable
fun FavouritesFolderContent(
    state: FavouritesFolderState,
    onBackPressed: () -> Unit,
    onChangeFavouriteStatus: (userId: String, isNeedToDelete: Boolean) -> Unit,
    onSpecialistCardClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LensaTheme.colors.backgroundColor)
            .padding(
                horizontal = 16.dp,
            )
    ) {
        item {
            FavouritesHeader(
                header = state.folderName,
                onBackPressed = onBackPressed,
            )
        }
        items(state.folderItems) { profile ->
            SpecialistCard(
                onClick = { onSpecialistCardClick.invoke(profile.id) },
                photoUrl = profile.avatarUrl,
                rating = profile.rating ?: 0f,
                text = "${profile.surname} ${profile.name}",
                showFavouritesButton = true,
                onShowFavouriteButtonClick = { isSelected ->
                    onChangeFavouriteStatus.invoke(profile.id, isSelected)
                }
            )
        }
    }
}


@Preview
@Composable
fun FavouritesFolderPreview() {
    LensaTheme {
        FavouritesFolderContent(
            state = FavouritesFolderState(
                folderName = "Test",
                folderItems = emptyList(),
                idsToDelete = emptyList(),
            ),
            onSpecialistCardClick = {},
            onBackPressed = {},
            onChangeFavouriteStatus = { _, _ -> }
        )
    }
}