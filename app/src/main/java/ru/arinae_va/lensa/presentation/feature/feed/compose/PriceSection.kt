package ru.arinae_va.lensa.presentation.feature.feed.compose

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.domain.model.Price
import ru.arinae_va.lensa.presentation.common.component.ExpandableButton
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun PriceSection(
    prices: List<Price>,
) {
    Text(
        text = "ПРАЙС",
        style = LensaTheme.typography.header2,
        color = LensaTheme.colors.textColor,
    )
    VSpace(h = 12.dp)
    prices.forEach {
        ExpandableButton(
            text = it.name,
            isFillMaxWidth = true,
        ) {
            // TODO форматирование стоимости
            Text(
                text = "\n${it.text}\n\nСтоимость: ${it.price}${it.currency.symbol}\n",
                style = LensaTheme.typography.text,
                color = LensaTheme.colors.textColor,
            )
        }
    }
}