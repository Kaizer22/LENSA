package ru.arinae_va.lensa.presentation.feature.favourite.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.favourite.viewmodel.FavouritesState
import ru.arinae_va.lensa.presentation.feature.favourite.viewmodel.FavouritesViewModel
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun FavouritesScreen(
    navController: NavController,
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LensaTheme.colors.backgroundColor)
            .padding(
                horizontal = 16.dp,
            )
    ) {
        VSpace(h = 24.dp)
        Text(
            text = "ИЗБРАННОЕ",
            style = LensaTheme.typography.header2,
            color = LensaTheme.colors.textColor,
        )
        VSpace(h = 12.dp)
        Divider(color = LensaTheme.colors.dividerColor)
        VSpace(h = 24.dp)
        state.folders.forEach { folder ->
            val urls = appendListToN(
                folder.value.map { profile ->
                    profile.portfolioUrls?.get(0).orEmpty()
                }
            )
            FavouritesFolderItem(
                name = folder.key,
                picturesUrls = urls,
                onClick = {
                    onFolderClick.invoke(folder.key)
                },
            )
            VSpace(h = 36.dp)
        }
    }
}

private fun appendListToN(
    list: List<String>,
    n: Int = 4,
    emptyValue: String = "",
): List<String> {
    return if (list.size >= n) list.take(n)
    else {
        val mL = list.toMutableList()
        repeat(n - list.size) { mL.add(emptyValue) }
        mL
    }
}

@Preview
@Composable
fun FavouritesScreenPreview() {
    LensaTheme {
        FavouritesContent(
            state = FavouritesState.INITIAL,
            onFolderClick = {},
            onBackPressed = {},
        )
    }
}