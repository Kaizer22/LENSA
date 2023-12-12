package ru.arinae_va.lensa.presentation.feature.auth.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.LensaButton
import ru.arinae_va.lensa.presentation.common.component.LensaHeader
import ru.arinae_va.lensa.presentation.common.component.LensaInput
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel,
) {
    setSystemUiColor()
    Screen(
        onSendVerificationCodeClick = viewModel::onEnterPhone,
    )
}

@Composable
private fun Screen(
    onSendVerificationCodeClick: (String) -> Unit,
) {
    var phoneInput by remember { mutableStateOf("+7") }
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
                value = phoneInput,
                onValueChanged = {
                    phoneInput = it
                }
            )
            VSpace(h = 52.dp)
            LensaButton(
                text = "ПОЛУЧИТЬ КОД",
                onClick = {
                    if (phoneInput.length == 12) {
                        onSendVerificationCodeClick(phoneInput)
                    }
                },
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
            onSendVerificationCodeClick = {},
        )
    }
}