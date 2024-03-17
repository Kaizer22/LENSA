package ru.arinae_va.lensa.presentation.feature.feed.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.domain.model.FeedFilter
import ru.arinae_va.lensa.domain.model.ORDER_TYPE_RATING_ASC_TEXT
import ru.arinae_va.lensa.domain.model.ORDER_TYPE_RATING_DESC_TEXT
import ru.arinae_va.lensa.domain.model.OrderType
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaButton
import ru.arinae_va.lensa.presentation.common.component.LensaDropdownInput
import ru.arinae_va.lensa.presentation.common.component.LensaIconButton
import ru.arinae_va.lensa.presentation.common.component.LensaInput
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.feed.viewmodel.FeedState
import ru.arinae_va.lensa.presentation.feature.feed.viewmodel.FeedViewModel
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import ru.arinae_va.lensa.utils.Constants

@Composable
fun FilterScreen(
    viewModel: FeedViewModel,
) {
    setSystemUiColor()

    val state by viewModel.state.collectAsState()
    FilterContent(
        state = state,
        onApplyFilterClick = viewModel::onApplyFilterClick,
        onClearFilterClick = viewModel::onClearFilterClick,
        onCloseFilterClick = {},
    )
}

@Composable
fun FilterContent(
    state: FeedState,
    onApplyFilterClick: (FeedFilter) -> Unit,
    onClearFilterClick: () -> Unit,
    onCloseFilterClick: () -> Unit,
) {
    with(state.filter) {
        val searchQuery by remember(state) { mutableStateOf(this?.searchQuery.orEmpty()) }
        val specialization by remember(state) { mutableStateOf(this?.specialization.orEmpty()) }
        val country by remember(state) { mutableStateOf(this?.country.orEmpty()) }
        val city by remember(state) { mutableStateOf(this?.city.orEmpty()) }
        val priceFrom by remember(state) { mutableStateOf(this?.priceFrom) }
        val priceTo by remember(state) { mutableStateOf(this?.priceTo) }
        var order by remember(state) { mutableStateOf(this?.ratingOrder) }

        Column(
            modifier = Modifier
                .background(color = LensaTheme.colors.backgroundColor)
                .fillMaxSize()
                .padding(
                    horizontal = 16.dp,
                )
        ) {
            VSpace(h = 30.dp)
            Row {
                FSpace()
                LensaIconButton(
                    onClick = onCloseFilterClick,
                    icon = R.drawable.ic_arrow_forward,
                    iconSize = 30.dp,
                )
            }
            VSpace(h = 28.dp)
            Text(
                text = "ФИЛЬТР",
                style = LensaTheme.typography.header2,
                color = LensaTheme.colors.textColor,
            )
            VSpace(h = 12.dp)
            Divider(
                modifier = Modifier
                    .height(1.dp)
                    .background(LensaTheme.colors.dividerColor)
            )
            VSpace(h = 28.dp)
            LensaDropdownInput(
                value = specialization,
                placeholder = "Специализация",
                allowFreeInput = false,
                items = Constants.SPECIALIZATIONS_LIST,
                onValueChanged = {},
            )
            VSpace(h = 12.dp)
            LensaDropdownInput(
                value = country,
                placeholder = "Страна",
                allowFreeInput = false,
                items = Constants.COUNTRIES_LIST,
                onValueChanged = {},
            )
            VSpace(h = 12.dp)
            LensaDropdownInput(
                value = city,
                placeholder = "Город",
                allowFreeInput = true,
                items = Constants.RUSSIAN_CITIES_LIST,
                onValueChanged = {}
            )
            VSpace(h = 28.dp)
            Text(
                text = "СТОИМОСТЬ",
                style = LensaTheme.typography.header3,
                color = LensaTheme.colors.textColor
            )
            VSpace(h = 12.dp)
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LensaInput(
                    value = priceFrom?.toString().orEmpty(),
                    modifier = Modifier.weight(1f),
                    placeholder = "От",
                    onValueChanged = {},
                )
                HSpace(w = 8.dp)
                Text(
                    text = "-",
                    style = LensaTheme.typography.header2,
                    color = LensaTheme.colors.textColor,
                )
                HSpace(w = 8.dp)
                LensaInput(
                    value = priceTo?.toString().orEmpty(),
                    modifier = Modifier.weight(1f),
                    placeholder = "До",
                    onValueChanged = {},
                )
            }
            VSpace(h = 28.dp)
            Text(
                text = "РЕЙТИНГ",
                style = LensaTheme.typography.header3,
                color = LensaTheme.colors.textColor
            )
            VSpace(h = 12.dp)
            LensaDropdownInput(
                items = Constants.SORT_OPTIONS,
                placeholder = "Сортировка",
                onValueChanged = { sortOption ->
                    when (sortOption) {
                        ORDER_TYPE_RATING_ASC_TEXT -> order = OrderType.RATING_ASC
                        ORDER_TYPE_RATING_DESC_TEXT -> order = OrderType.RATING_DESC
                    }
                },
            )
            FSpace()
            LensaButton(
                isFillMaxWidth = true,
                text = "ПОКАЗАТЬ",
                onClick = {
                    onApplyFilterClick.invoke(
                        FeedFilter(
                            specialization = specialization,
                            searchQuery = searchQuery,
                            country = country,
                            city = city,
                            priceFrom = priceFrom ?: Int.MIN_VALUE,
                            priceTo = priceTo ?: Int.MAX_VALUE,
                            ratingOrder = order ?: OrderType.RATING_DESC,
                        )
                    )
                },
            )
            VSpace(h = 20.dp)
            LensaTextButton(
                type = LensaTextButtonType.DEFAULT,
                isFillMaxWidth = true,
                text = "СБРОСИТЬ ФИЛЬТРЫ",
                onClick = onClearFilterClick,
            )
            VSpace(h = 28.dp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FilterScreenPreview() {
    LensaTheme {
        FilterContent(
            state = FeedState.INITIAL,
            onApplyFilterClick = {},
            onClearFilterClick = {},
            onCloseFilterClick = {},
        )
    }
}