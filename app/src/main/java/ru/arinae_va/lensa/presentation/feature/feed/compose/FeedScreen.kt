package ru.arinae_va.lensa.presentation.feature.feed.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.presentation.common.component.FSpace
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
        onProfileClick = viewModel::onProfileClick,
        onCardClick = viewModel::onCardClick,
        cards = viewModel.feedList.toList().map { specialistModel ->
            CardModel(
                photoUrl = specialistModel.avatarUrl ?: "",
                rating = specialistModel.rating ?: 0.0f,
                name = specialistModel.name,
                surname = specialistModel.surname,
                userId = specialistModel.id,
            )
        }
    )
}

data class CardModel(
    val userId: String,
    val photoUrl: String,
    val rating: Float,
    val name: String,
    val surname: String,
)

@Composable
private fun Screen(
    onSearchTextChanged: (String) -> Unit,
    onProfileClick: () -> Unit,
    onCardClick: (userUid: String) -> Unit,
    cards: List<CardModel>,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()


    LazyColumn(
        modifier = Modifier
            .background(
                color = LensaTheme.colors.backgroundColor
            )
            .fillMaxSize(),
        state = listState,
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
        if (cards.isNotEmpty()) {
            items(items = cards) { cardModel ->
                VSpace(h = 24.dp)
                SpecialistCard(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    photoUrl = cardModel.photoUrl,
                    rating = cardModel.rating,
                    text = "${cardModel.surname} ${cardModel.name}",
                    onClick = {
                        onCardClick(cardModel.userId)
                    },
                )
                VSpace(h = 24.dp)
            }
            item {
                if (cards.size > 5) {
                    FeedLastItem(
                        onClick = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(index = 0)
                            }
                        }
                    )
                }
            }
        } else {
            item {
                FeedEmptyState()
            }
        }
    }
}

@Composable
fun FeedEmptyState(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "НИЧЕГО НЕ НАЙДЕНО",
            style = LensaTheme.typography.header2,
            color = LensaTheme.colors.textColor,
        )
    }
}

@Composable
fun FeedLastItem(
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VSpace(h = 40.dp)
        Divider(color = LensaTheme.colors.dividerColor)
        VSpace(h = 40.dp)
        LensaTextButton(
            text = "ВЕРНУТЬСЯ НАВЕРХ",
            onClick = onClick,
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
fun FeedHeader(
    text: String = "СПЕЦИАЛИСТЫ"
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VSpace(h = 12.dp)
        Text(
            text = text,
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
                    userId = "fff"
                )
            )
        )
    }
}