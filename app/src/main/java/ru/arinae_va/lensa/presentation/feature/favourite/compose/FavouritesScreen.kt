package ru.arinae_va.lensa.presentation.feature.favourite.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.favourite.viewmodel.FavouritesState
import ru.arinae_va.lensa.presentation.feature.favourite.viewmodel.FavouritesViewModel
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import ru.arinae_va.lensa.utils.ext.appendListToN

@Composable
fun FavouritesScreen(
    viewModel: FavouritesViewModel,
) {
    val state by viewModel.state.collectAsState()
    setSystemUiColor()
    FavouritesContent(
        state = state,
        onFolderClick = viewModel::onFolderClicked,
        onBackPressed = {},
    )
}

@Composable
private fun FavouritesContent(
    state: FavouritesState,
    onFolderClick: (String) -> Unit,
    onBackPressed: () -> Unit,
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
            VSpace(h = 24.dp)
            Text(
                text = "ИЗБРАННОЕ",
                style = LensaTheme.typography.header2,
                color = LensaTheme.colors.textColor,
            )
            VSpace(h = 12.dp)
            Divider(color = LensaTheme.colors.dividerColor)
            VSpace(h = 24.dp)
        }

        items(state.folders.keys.toList()) { folder ->
            val urls = state.folders[folder]?.map { profile ->
                profile.portfolioUrls?.get(0).orEmpty()
            }?.appendListToN(
                n = FAVOURITES_FOLDER_PREVIEW_PICTURES_COUNT,
                emptyValue = "",
            )
            FavouritesFolderItem(
                name = folder,
                picturesUrls = urls ?: emptyList(),
                onClick = {
                    onFolderClick.invoke(folder)
                },
            )
            VSpace(h = 36.dp)
        }
    }
}

@Preview
@Composable
fun FavouritesScreenPreview() {
    LensaTheme {
        FavouritesContent(
            state = FavouritesState(
                folders = emptyMap()
            ),
            onFolderClick = {},
            onBackPressed = {},
        )
    }
}