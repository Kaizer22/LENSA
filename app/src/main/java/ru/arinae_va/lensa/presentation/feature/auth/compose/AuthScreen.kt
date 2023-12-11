package ru.arinae_va.lensa.presentation.feature.auth.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.LensaButton
import ru.arinae_va.lensa.presentation.common.component.LensaHeader
import ru.arinae_va.lensa.presentation.common.component.LensaInput
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel,
) {
    setSystemUiColor()
    Screen(
        onGetCodeClick = {
            navController.navigate(LensaScreens.OTP_SCREEN.name)
        },
    )
}

@Composable
private fun Screen(
    onGetCodeClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = LensaTheme.colors.backgroundColor
            ),
    ) {
        LensaHeader()
        VSpace(h = 128.dp)
        Text(
            color = LensaTheme.colors.textColor,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
            text = "Номер телефона",
            style = LensaTheme.typography.text,
        )
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                value = "+7",
                onValueChanged = {}
            )
            VSpace(h = 52.dp)
            LensaButton(
                text = "ПОЛУЧИТЬ КОД",
                onClick = onGetCodeClick,
                isFillMaxWidth = true,
            )
            FSpace()
            // TODO ссылки
            Text(
                modifier = Modifier.padding(40.dp),
                text = "Продолжая авторизацию, вы автоматически соглашаетесь с " +
                        "политикой конфиденциальности и условиями сервиса",
                color = LensaTheme.colors.textColorSecondary,
                style = LensaTheme.typography.signature,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AuthScreenPreview() {
    LensaTheme {
        Screen(
            onGetCodeClick = {},
        )
    }
}