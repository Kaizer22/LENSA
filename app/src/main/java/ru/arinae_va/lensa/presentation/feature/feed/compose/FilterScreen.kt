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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
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
        onCountryChanged = viewModel::onFilterCountryChanged,
        onCityChanged = viewModel::onFilterCityChanged,
        onSpecializationChanged = viewModel::onFilterSpecializationChanged,
        onOrderChanged = viewModel::onFilterOrderChanged,
        onPriceToChanged = viewModel::onFilterPriceToChanged,
        onPriceFromChanged = viewModel::onFilterPriceFromChanged,
        onApplyFilterClick = viewModel::onApplyFilterClick,
        onClearFilterClick = viewModel::onClearFilterClick,
        onCloseFilterClick = {},
    )
}

@Composable
internal fun FilterContent(
    state: FeedState,
    onSpecializationChanged: (String) -> Unit,
    onCountryChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onPriceFromChanged: (Int) -> Unit,
    onPriceToChanged: (Int) -> Unit,
    onOrderChanged: (OrderType) -> Unit,
    onApplyFilterClick: () -> Unit,
    onClearFilterClick: () -> Unit,
    onCloseFilterClick: () -> Unit,
) {
    with(state.filter) {
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
                items = Constants.SPECIALIZATIONS_LIST.map { it.first },
                onValueChanged = onSpecializationChanged,
            )
            VSpace(h = 12.dp)
            LensaDropdownInput(
                value = country,
                placeholder = "Страна",
                allowFreeInput = false,
                items = Constants.COUNTRIES_LIST,
                onValueChanged = onCountryChanged,
            )
            VSpace(h = 12.dp)
            LensaDropdownInput(
                value = city,
                placeholder = "Город",
                allowFreeInput = true,
                items = Constants.RUSSIAN_CITIES_LIST,
                onValueChanged = onCityChanged,
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
                    inputType = KeyboardType.Decimal,
                    value = if (priceFrom == 0) "" else priceFrom.toString(),
                    modifier = Modifier.weight(1f),
                    placeholder = "От",
                    showTrailingIcon = true,
                    trailingIconRes = R.drawable.ic_rouble,
                    onValueChanged = { new ->
                        onPriceFromChanged.invoke(new.toIntOrNull() ?: 0)
                    },
                )
                HSpace(w = 8.dp)
                Text(
                    text = "-",
                    style = LensaTheme.typography.header2,
                    color = LensaTheme.colors.textColor,
                )
                HSpace(w = 8.dp)
                LensaInput(
                    inputType = KeyboardType.Decimal,
                    value = if (priceTo == Int.MAX_VALUE) "" else priceTo.toString(),
                    modifier = Modifier.weight(1f),
                    showTrailingIcon = true,
                    trailingIconRes = R.drawable.ic_rouble,
                    placeholder = "До",
                    onValueChanged = { new ->
                        onPriceToChanged(new.toIntOrNull() ?: Int.MAX_VALUE)
                    },
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
                        ORDER_TYPE_RATING_ASC_TEXT -> onOrderChanged.invoke(OrderType.RATING_ASC)
                        ORDER_TYPE_RATING_DESC_TEXT -> onOrderChanged.invoke(OrderType.RATING_DESC)
                    }
                },
            )
            FSpace()
            if (state.filterValidationErrors.isNotEmpty()) {
                Text(
                    text = state.filterValidationErrors.values.first(),
                    style = LensaTheme.typography.signature,
                    color = LensaTheme.colors.textColorSecondary,
                )
            }
            VSpace(h = 16.dp)
            LensaButton(
                enabled = state.isApplyFilterButtonEnabled,
                isFillMaxWidth = true,
                text = "ПОКАЗАТЬ",
                onClick = onApplyFilterClick,
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
            onCountryChanged = {},
            onCityChanged = {},
            onSpecializationChanged = {},
            onOrderChanged = {},
            onPriceToChanged = {},
            onPriceFromChanged = {},
            onApplyFilterClick = {},
            onClearFilterClick = {},
            onCloseFilterClick = {},
        )
    }
}