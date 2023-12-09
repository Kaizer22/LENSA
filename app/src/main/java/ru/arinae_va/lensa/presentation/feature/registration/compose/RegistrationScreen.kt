package ru.arinae_va.lensa.presentation.feature.registration.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaButton
import ru.arinae_va.lensa.presentation.common.component.LensaImagePicker
import ru.arinae_va.lensa.presentation.common.component.LensaInput
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegistrationViewModel,
) {
    setSystemUiColor()
    Screen(
        onSaveClick = {
            navController.navigate(LensaScreens.FEED_SCREEN.name)
        }
    )
}

@Composable
private fun Screen(
    onSaveClick: () -> Unit,
) {
    var isNextEnabled by remember{mutableStateOf(true)}
    Column(
        modifier = Modifier
            .background(color = LensaTheme.colors.backgroundColor)
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            VSpace(h = 40.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                LensaImagePicker(
                    modifier = Modifier.size(216.dp),
                    onClick = {},
                    emptyStateButtonSize = 48.dp,
                )
            }
            VSpace(h = 28.dp)
            Text(
                text = "Все заполненные поля будут видны другим пользователям",
                style = LensaTheme.typography.signature,
                color = LensaTheme.colors.textColorSecondary,
            )
            VSpace(h = 28.dp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_4_star),
                    tint = LensaTheme.colors.textColorAccent,
                    contentDescription = "",
                )
                HSpace(w = 12.dp)
                Text(
                    text = "Поле, обязательное для заполнения",
                    style = LensaTheme.typography.signature,
                    color = LensaTheme.colors.textColorSecondary,
                )
            }
            VSpace(h = 24.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = {},
                defaultValue = "",
                placeholder = "Фамилия"
            )
            VSpace(h = 12.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = {},
                defaultValue = "",
                placeholder = "Имя"
            )
            VSpace(h = 12.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = {},
                defaultValue = "",
                placeholder = "Специализация"
            )
            VSpace(h = 12.dp)
            Text(
                text = "Не нашел свою специализацию?",
                style = LensaTheme.typography.signature,
                color = LensaTheme.colors.textColorSecondary,
            )
            LensaTextButton(
                text = "Свяжись с нами",
                type = LensaTextButtonType.ACCENT,
                onClick = {}
            )
            VSpace(h = 12.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = {},
                defaultValue = "",
                placeholder = "Страна"
            )
            VSpace(h = 12.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = {},
                defaultValue = "",
                placeholder = "Город"
            )
            VSpace(h = 12.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = {},
                defaultValue = "",
                placeholder = "Номер телефона"
            )
            VSpace(h = 12.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = {},
                defaultValue = "",
                placeholder = "Почта"
            )
            VSpace(h = 12.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = {},
                defaultValue = "",
                placeholder = "О себе"
            )
            VSpace(h = 28.dp)
        }
        PortfolioCarousel()
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            VSpace(h = 28.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = {},
                defaultValue = "",
                placeholder = "Сайт"
            )
            VSpace(h = 12.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = {},
                defaultValue = "",
                placeholder = "Социальная сеть"
            )
            VSpace(h = 12.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = {},
                defaultValue = "",
                placeholder = "Прайс"
            )
            VSpace(h = 60.dp)
            LensaButton(
                enabled = isNextEnabled,
                text = "СОХРАНИТЬ",
                isFillMaxWidth = true,
                onClick = onSaveClick,
            )
            VSpace(h = 20.dp)
            LensaTextButton(
                text = "ОТМЕНИТЬ",
                type = LensaTextButtonType.DEFAULT,
                isFillMaxWidth = true,
                onClick = {}
            )
        }
    }
}

@Composable
fun PortfolioCarousel(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
    ) {
        for (i in 0 until 5) {
            HSpace(w = 4.dp)
            LensaImagePicker(
                modifier = Modifier.size(175.dp),
                onClick = { /*TODO*/ }
            )
            HSpace(w = 4.dp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistrationScreenPreview() {
    LensaTheme {
        Screen(
            onSaveClick = {},
        )
    }
}