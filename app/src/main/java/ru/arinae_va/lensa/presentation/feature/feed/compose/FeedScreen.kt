package ru.arinae_va.lensa.presentation.feature.feed.compose

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.model.OrderType
import ru.arinae_va.lensa.presentation.common.component.LensaActionBar
import ru.arinae_va.lensa.presentation.common.component.LensaReplaceLoader
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.feed.compose.component.SpecialistCard
import ru.arinae_va.lensa.presentation.feature.feed.model.CardModel
import ru.arinae_va.lensa.presentation.feature.feed.viewmodel.FeedState
import ru.arinae_va.lensa.presentation.feature.feed.viewmodel.FeedViewModel
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
) {
    setSystemUiColor()
    val state by viewModel.state.collectAsState()
    LaunchedEffect(viewModel) {
        viewModel.onAttach()
    }
    FeedContent(
        state = state,
        onSearchTextChanged = viewModel::onSearchTextChanged,
        onCountryChanged = viewModel::onFilterCountryChanged,
        onCityChanged = viewModel::onFilterCityChanged,
        onSpecializationChanged = viewModel::onFilterSpecializationChanged,
        onOrderChanged = viewModel::onFilterOrderChanged,
        onPriceToChanged = viewModel::onFilterPriceToChanged,
        onPriceFromChanged = viewModel::onFilterPriceFromChanged,
        onProfileClick = viewModel::onProfileClick,
        onCardClick = viewModel::onCardClick,
        onApplyFilterClick = viewModel::onApplyFilterClick,
        onClearFilterClick = viewModel::onClearFilterClick,
    )
}

// TODO move component with animation to lib
enum class SideMenuState {
    HIDDEN, OPEN, OPENING, HIDING
}

@Composable
private fun FeedContent(
    state: FeedState,
    onSpecializationChanged: (String) -> Unit,
    onCountryChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onPriceFromChanged: (Int) -> Unit,
    onPriceToChanged: (Int) -> Unit,
    onOrderChanged: (OrderType) -> Unit,
    onApplyFilterClick: () -> Unit,
    onClearFilterClick: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onProfileClick: () -> Unit,
    onCardClick: (userUid: String) -> Unit,
) {
    var sideMenuState by remember { mutableStateOf(SideMenuState.HIDDEN) }
    BoxWithConstraints {
        val interactionSource = remember { MutableInteractionSource() }
        val menuWidthDp = maxWidth / 5 * 4
        val menuHideOffset = -menuWidthDp
        val menuShownOffset = 0.dp

        val filterVisibilityAnimation = animateDpAsState(
            targetValue = when (sideMenuState) {
                SideMenuState.HIDDEN -> menuHideOffset
                SideMenuState.OPEN -> menuShownOffset
                SideMenuState.OPENING -> menuShownOffset
                SideMenuState.HIDING -> menuHideOffset
            },
            label = "filterVisibility",
            finishedListener = {
                when (sideMenuState) {
                    SideMenuState.OPENING -> sideMenuState = SideMenuState.OPEN
                    SideMenuState.HIDING -> sideMenuState = SideMenuState.HIDDEN
                    else -> {}
                }
            }
        )

        FeedAndSearchBar(
            state = state,
            onMenuClick = { sideMenuState = SideMenuState.OPENING },
            onSearchTextChanged = onSearchTextChanged,
            onProfileClick = onProfileClick,
            onCardClick = onCardClick,
            onErrorClearFilter = onClearFilterClick,
        )
        if (sideMenuState != SideMenuState.HIDDEN) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = LensaTheme.colors.fadeColor
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        onClick = {
                            sideMenuState = SideMenuState.HIDING
                        },
                        indication = null
                    )
            )
        }
        Box(
            modifier = Modifier
                .width(menuWidthDp)
                .offset(
                    x = filterVisibilityAnimation.value,
                    y = 0.dp
                )
                .clickable(
                    interactionSource = interactionSource,
                    onClick = {},
                    indication = null
                )
        ) {
            FilterContent(
                state = state,
                onCountryChanged = onCountryChanged,
                onPriceFromChanged = onPriceFromChanged,
                onPriceToChanged = onPriceToChanged,
                onOrderChanged = onOrderChanged,
                onSpecializationChanged = onSpecializationChanged,
                onCityChanged = onCityChanged,
                onApplyFilterClick = {
                    onApplyFilterClick.invoke()
                    sideMenuState = SideMenuState.HIDING
                },
                onCloseFilterClick = { sideMenuState = SideMenuState.HIDING },
                onClearFilterClick = {
                    onClearFilterClick.invoke()
                    sideMenuState = SideMenuState.HIDING
                }
            )
        }
    }
}

@Composable
internal fun FeedAndSearchBar(
    state: FeedState,
    onErrorClearFilter: () -> Unit,
    onMenuClick: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onProfileClick: () -> Unit,
    onCardClick: (userUid: String) -> Unit,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val cards = remember(state) {
        state.feed.map { specialistModel ->
            CardModel(
                photoUrl = specialistModel.avatarUrl ?: "",
                rating = specialistModel.rating ?: 0.0f,
                name = specialistModel.name,
                surname = specialistModel.surname,
                profileId = specialistModel.profileId,
            )
        }
    }

    Column(
        modifier = Modifier
            .testTag("feed_screen_content_container")
            .background(
                color = LensaTheme.colors.backgroundColor
            )
            .fillMaxSize(),
    ) {
        LensaActionBar(
            modifier = Modifier
                .fillMaxWidth(),
            searchQuery = state.filter.searchQuery,
            onMenuClick = onMenuClick,
            onSearchClick = {},
            onSearchTextChanged = onSearchTextChanged,
            onProfileClick = onProfileClick,
        )
        LensaReplaceLoader(isLoading = state.isLoading) {
            if (cards.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                ) {

                    item {
                        FeedHeader(
                            header = state.filter.specialization
                                .ifEmpty { "СПЕЦИАЛИСТЫ" }
                        )
                    }

                    items(items = cards) { cardModel ->
                        VSpace(h = 24.dp)
                        SpecialistCard(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            photoUrl = cardModel.photoUrl,
                            rating = cardModel.rating,
                            text = "${cardModel.surname} ${cardModel.name}",
                            onClick = {
                                onCardClick(cardModel.profileId)
                            },
                        )
                        VSpace(h = 24.dp)
                    }
                    item {
                        if (cards.size > 2) {
                            FeedLastItem(
                                onClick = {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(index = 0)
                                    }
                                }
                            )
                        }
                    }
                }
            } else {
                FeedEmptyState(
                    onErrorClearFilter = onErrorClearFilter,
                )
            }
        }
    }
}

@Composable
fun FeedEmptyState(
    modifier: Modifier = Modifier,
    onErrorClearFilter: () -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        FeedHeader(header = "ИЗВИНИТЕ")
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "ПО ЭТОМУ\nЗАПРОСУ\nНИЧЕГО НЕ\nНАЙДЕНО",
                    style = LensaTheme.typography.header2,
                    color = LensaTheme.colors.textColor,
                )
                VSpace(h = 48.dp)
                LensaTextButton(
                    text = "Сбросить фильтр",
                    onClick = onErrorClearFilter,
                    type = LensaTextButtonType.ACCENT,
                )
            }
        }
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
    header: String,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VSpace(h = 12.dp)
        Text(
            text = header,
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
        FeedContent(
            state = FeedState.INITIAL,
            onSearchTextChanged = {},
            onCountryChanged = {},
            onCityChanged = {},
            onSpecializationChanged = {},
            onOrderChanged = {},
            onPriceToChanged = {},
            onPriceFromChanged = {},
            onApplyFilterClick = {},
            onClearFilterClick = {},
            onProfileClick = {},
            onCardClick = {}
        )
    }
}