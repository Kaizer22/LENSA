package ru.arinae_va.lensa.presentation.feature.feed.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.arinae_va.lensa.presentation.common.component.LensaActionBar
import ru.arinae_va.lensa.presentation.common.component.LensaHeader
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.feed.compose.component.SpecialistCard
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun FeedScreen(
    navController: NavController,
    viewModel: FeedViewModel,
) {
    setSystemUiColor()
    Screen(
        onSearchTextChanged = {},
        onProfileClick = {
            navController.navigate(LensaScreens.SPECIALIST_DETAILS_SCREEN.name)
        },
        onCardClick = {
            navController.navigate(LensaScreens.SPECIALIST_DETAILS_SCREEN.name)
        },
        cards = listOf(
            CardModel(
                photoUrl = "https://pixelbox.ru/wp-content/uploads/2021/11/black-white-avatars-steam-pixelbox.ru-27.jpg",
                rating = 4.9f,
                name = "Test",
                surname = "Testtest",
            ),
            CardModel(
                photoUrl = "https://pixelbox.ru/wp-content/uploads/2021/11/black-white-avatars-steam-pixelbox.ru-27.jpg",
                rating = 4.9f,
                name = "Test",
                surname = "Testtest",
            ),
            CardModel(
                photoUrl = "https://pixelbox.ru/wp-content/uploads/2021/11/black-white-avatars-steam-pixelbox.ru-27.jpg",
                rating = 4.9f,
                name = "Test",
                surname = "Testtest",
            ),
            CardModel(
                photoUrl = "https://pixelbox.ru/wp-content/uploads/2021/11/black-white-avatars-steam-pixelbox.ru-27.jpg",
                rating = 4.9f,
                name = "Test",
                surname = "Testtest",
            ),
            CardModel(
                photoUrl = "https://pixelbox.ru/wp-content/uploads/2021/11/black-white-avatars-steam-pixelbox.ru-27.jpg",
                rating = 4.9f,
                name = "Test",
                surname = "Testtest",
            ),
        )
    )
}

data class CardModel(
    val photoUrl: String,
    val rating: Float,
    val name: String,
    val surname: String,
)

@Composable
private fun Screen(
    onSearchTextChanged: (String) -> Unit,
    onProfileClick: () -> Unit,
    onCardClick: () -> Unit,
    cards: List<CardModel>,
) {

    LazyColumn(
        modifier = Modifier
            .background(
                color = LensaTheme.colors.backgroundColor
            )
            .fillMaxSize(),
    ) {
        item {
            LensaActionBar(
                modifier = Modifier.fillMaxWidth(),
                onMenuClick = {},
                onSearchTextChanged = onSearchTextChanged,
                onProfileClick = onProfileClick,
            )
        }
        item {
            FeedHeader()
        }
        items(items = cards) { cardModel ->
            VSpace(h = 24.dp)
            SpecialistCard(
                modifier = Modifier.padding(horizontal = 16.dp),
                photoUrl = cardModel.photoUrl,
                rating = cardModel.rating,
                text = "${cardModel.surname} ${cardModel.name}",
                onClick = onCardClick,
            )
            VSpace(h = 24.dp)
        }
        item {
            FeedLastItem()
        }
    }
}

@Composable
fun FeedLastItem() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VSpace(h = 40.dp)
        Divider(color = LensaTheme.colors.dividerColor)
        VSpace(h = 40.dp)
        LensaTextButton(
            text = "ВЕРНУТЬСЯ НАВЕРХ",
            onClick = {},
            isFillMaxWidth = true,
            type = LensaTextButtonType.DEFAULT,
        )
        Text(
            text = "На этом пока все...",
            color = LensaTheme.colors.textColor,
        )
        VSpace(h = 40.dp)
    }
}

@Composable
fun FeedHeader() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VSpace(h = 12.dp)
        Text(
            text = "СПЕЦИАЛИСТЫ",
            style = LensaTheme.typography.header2,
            color = LensaTheme.colors.textColor,
        )
        VSpace(h = 12.dp)
        Divider(
            color = LensaTheme.colors.dividerColor,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FeedScreenPreview() {
    LensaTheme {
        Screen(
            onCardClick = {},
            onSearchTextChanged = {},
            onProfileClick = {},
            cards = listOf(
                CardModel(
                    photoUrl = "https://pixelbox.ru/wp-content/uploads/2021/11/black-white-avatars-steam-pixelbox.ru-27.jpg",
                    rating = 4.9f,
                    name = "Test",
                    surname = "Testtest",
                )
            )
        )
    }
}