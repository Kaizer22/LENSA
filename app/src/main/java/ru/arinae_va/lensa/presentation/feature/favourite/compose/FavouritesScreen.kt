package ru.arinae_va.lensa.presentation.feature.favourite.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.feed.viewmodel.FeedViewModel

@Composable
fun FavouritesScreen(
    navController: NavController,
    viewModel: FeedViewModel,
) {
    setSystemUiColor()
    Screen()
}

@Composable
private fun Screen() {

}

@Preview
@Composable
fun FavouritesScreenPreview() {

}