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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.LensaButton
import ru.arinae_va.lensa.presentation.common.component.LensaInput
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun PriceListCreatorDialog(
    onSaveClick: () -> Unit,
    onDismissClick: () -> Unit,
) {
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

                LensaButton(
                    text = "СОХРАНИТЬ",
                    isFillMaxWidth = true,
                    onClick = onSaveClick,
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
fun TariffSection() {
    Column {
        LensaInput(
            onValueChanged = {},
            defaultValue = "",
            placeholder = "Название",
        )
        VSpace(h = 12.dp)
        LensaInput(
            onValueChanged = {},
            defaultValue = "",
            placeholder = "Описание",
        )
        VSpace(h = 12.dp)
        LensaInput(
            onValueChanged = {},
            defaultValue = "",
            showTrailingIcon = true,
            trailingIconRes = R.drawable.ic_rouble,
            placeholder = "Стоимость",
        )
        VSpace(h = 12.dp)
        LensaTextButton(
            text = "Удалить тариф",
            onClick = { /*TODO*/ },
            type = LensaTextButtonType.ACCENT,
        )
        VSpace(h = 12.dp)
        Divider(color = LensaTheme.colors.dividerColor)
    }
}