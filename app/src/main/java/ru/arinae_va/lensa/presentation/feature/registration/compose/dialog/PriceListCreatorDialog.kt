package ru.arinae_va.lensa.presentation.feature.registration.compose.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.domain.model.user.Price
import ru.arinae_va.lensa.domain.model.user.PriceCurrency
import ru.arinae_va.lensa.presentation.common.component.LensaButton
import ru.arinae_va.lensa.presentation.common.component.LensaInput
import ru.arinae_va.lensa.presentation.common.component.LensaMultilineInput
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun PriceListCreatorDialog(
    defaultPricesList: List<Price> = listOf(),
    onSaveClick: (List<Price>) -> Unit,
    onDismissClick: () -> Unit,
) {
    var pricesList by remember { mutableStateOf(defaultPricesList) }
    Dialog(
        onDismissRequest = onDismissClick,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        ),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = LensaTheme.colors.backgroundColor
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp, vertical = 48.dp
                    )
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "ПРАЙС",
                    style = LensaTheme.typography.header2,
                    color = LensaTheme.colors.textColor,
                )
                VSpace(h = 24.dp)
                Divider(color = LensaTheme.colors.dividerColor)
                VSpace(h = 24.dp)
                pricesList.forEach { price ->
                    PriceSection(
                        isFillMaxWidth = true,
                        dName = price.name,
                        dDescription = price.text,
                        dPrice = price.price,
                        onTariffChanged = { name, description, p ->
                            price.name = name
                            price.text = description
                            price.price = p
                        },
                        onDeleteClick = {
                            pricesList = pricesList.filter { it.id != price.id }
                        }
                    )
                    VSpace(h = 32.dp)
                }
                LensaInput(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    onValueChanged = {},
                    value = "Добавить тариф",
                    showTrailingIcon = true,
                    trailingIconRes = R.drawable.ic_plus,
                    onTrailingIconClick = {
                        pricesList = pricesList + Price(
                            name = "",
                            text = "",
                            price = 0,
                            currency = PriceCurrency.RUB,
                        )
                    }
                )
                VSpace(h = 48.dp)
                LensaButton(
                    text = "СОХРАНИТЬ",
                    isFillMaxWidth = true,
                    onClick = {
                        onSaveClick(pricesList)
                    },
                )
                VSpace(h = 20.dp)
                LensaTextButton(
                    text = "ОТМЕНИТЬ",
                    isFillMaxWidth = true,
                    onClick = onDismissClick,
                    type = LensaTextButtonType.DEFAULT,
                )
            }
        }
    }
}

@Composable
fun PriceSection(
    isFillMaxWidth: Boolean = false,
    dName: String = "",
    dDescription: String = "",
    dPrice: Int = 0,
    onTariffChanged: (String, String, Int) -> Unit,
    onDeleteClick: () -> Unit,
) {
    var name by remember { mutableStateOf(dName) }
    var description by remember { mutableStateOf(dDescription) }
    var price by remember { mutableStateOf(dPrice) }

    val mod = if (isFillMaxWidth) Modifier.fillMaxWidth() else Modifier
    Column {
        LensaInput(
            modifier = mod,
            onValueChanged = {
                name = it
                onTariffChanged(name, description, price)
            },
            value = name,
            placeholder = "Название",
        )
        VSpace(h = 12.dp)
        LensaMultilineInput(
            modifier = mod,
            onValueChanged = {
                description = it
                onTariffChanged(name, description, price)
            },
            linesCount = 3,
            value = description,
            placeholder = "Описание",
        )
        VSpace(h = 12.dp)
        LensaInput(
            modifier = mod,
            onValueChanged = {
                price = it.toIntOrNull() ?: 0
                onTariffChanged(name, description, price)
            },
            value = price.toString(),
            showTrailingIcon = true,
            trailingIconRes = R.drawable.ic_rouble,
            placeholder = "Стоимость",
            inputType = KeyboardType.Number,
        )
        VSpace(h = 12.dp)
        LensaTextButton(
            isFillMaxWidth = isFillMaxWidth,
            text = "Удалить тариф",
            onClick = onDeleteClick,
            type = LensaTextButtonType.ACCENT,
        )
        VSpace(h = 12.dp)
        Divider(color = LensaTheme.colors.dividerColor)
    }
}