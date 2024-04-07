package ru.arinae_va.lensa.presentation.feature.favourite.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.LensaIconButton
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.favourite.viewmodel.FavouritesState
import ru.arinae_va.lensa.presentation.feature.favourite.viewmodel.FavouritesViewModel
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import ru.arinae_va.lensa.utils.ext.appendListToN

@Composable
fun FavouritesHeader(
    header: String,
    onBackPressed: () -> Unit,
) {
    Column {
        VSpace(h = 24.dp)
        LensaIconButton(
            onClick = onBackPressed,
            icon = R.drawable.ic_arrow_back,
            iconSize = 28.dp,
        )
        VSpace(h = 24.dp)
        Text(
            text = header,
            style = LensaTheme.typography.header2,
            color = LensaTheme.colors.textColor,
        )
        VSpace(h = 12.dp)
        Divider(color = LensaTheme.colors.dividerColor)
        VSpace(h = 24.dp)
    }
}

@Composable
fun FavouritesScreen(
    viewModel: FavouritesViewModel,
) {
    val state by viewModel.state.collectAsState()
    setSystemUiColor()
    FavouritesContent(
        state = state,
        onFolderClick = viewModel::onFolderClicked,
        onBackPressed = viewModel::onBackPressed,
    )
}

@Composable
private fun FavouritesContent(
    state: FavouritesState,
    onFolderClick: (String) -> Unit,
    onBackPressed: () -> Unit,
) {
    if (state.folders.isEmpty()) {
        FavouritesEmpty(
            header = "ИЗБРАННОЕ",
            onBackPressed = onBackPressed,
        )
    } else {
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
                    header = "ИЗБРАННОЕ",
                    onBackPressed = onBackPressed,
                )
            }

            items(state.folders.keys.toList()) { folder ->
                val urls = state.folders[folder]?.map { profile ->
                    profile.avatarUrl.orEmpty()
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
}

@Composable
fun FavouritesEmpty(
    header: String,
    onBackPressed: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LensaTheme.colors.backgroundColor)
            .padding(
                horizontal = 16.dp,
            )
    ) {
        FavouritesHeader(
            header = header,
            onBackPressed = onBackPressed,
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "ВЫ ПОКА НЕ ДОБАВИЛИ НИЧЕГО В ИЗБРАННОЕ",
                style = LensaTheme.typography.header2,
                color = LensaTheme.colors.textColor,
            )
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