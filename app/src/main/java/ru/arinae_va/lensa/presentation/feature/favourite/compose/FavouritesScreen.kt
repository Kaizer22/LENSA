package ru.arinae_va.lensa.presentation.feature.favourite.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.favourite.viewmodel.FavouritesViewModel
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun FavouritesScreen(
    navController: NavController,
    viewModel: FavouritesViewModel,
) {
    setSystemUiColor()
    FavouritesContent()
}

@Composable
private fun FavouritesContent() {

}

@Preview
@Composable
fun FavouritesScreenPreview() {
    LensaTheme {
        FavouritesContent()
    }
}