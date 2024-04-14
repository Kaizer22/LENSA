package ru.arinae_va.lensa.presentation.feature.feed.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.domain.model.Price
import ru.arinae_va.lensa.presentation.common.component.LensaExpandableButton
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import ru.arinae_va.lensa.utils.ext.toFormattedNumberString

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
    prices.forEachIndexed { index, price ->
        LensaExpandableButton(
            text = price.name,
            isFillMaxWidth = true,
            hideBottomDivider = index != prices.size - 1,
        ) {
            Column {
                Text(
                    text = "\n${price.text}\n\nСтоимость: ${price.price.toFormattedNumberString()} ${price.currency.symbol}\n",
                    style = LensaTheme.typography.text,
                    color = LensaTheme.colors.textColor,
                )
                if (index == prices.size - 1) {
                    Divider(color = LensaTheme.colors.dividerColor)
                }
            }
        }
    }
}