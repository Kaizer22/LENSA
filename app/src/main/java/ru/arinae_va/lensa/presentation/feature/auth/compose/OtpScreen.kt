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
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun OtpScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    phoneNumber: String,
) {
    viewModel.verifyPhoneNumber(phoneNumber)
    setSystemUiColor()
    Screen(
        phoneNumber = phoneNumber,
        onNextClick = viewModel::verifyCode
//            navController.navigate(
//                LensaScreens.REGISTRATION_ROLE_SELECTOR_SCREEN.name
//            )
    )
}

@Composable
private fun Screen(
    phoneNumber: String,
    onNextClick: (String) -> Unit,
) {
    var isResendEnabled by remember { mutableStateOf(false) }
    var otpInput by remember { mutableStateOf("") }
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
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp),
            text = "Код из СМС",
            style = LensaTheme.typography.text,
        )
        Text(
            color = LensaTheme.colors.textColor,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
            text = "Отправили код на $phoneNumber",
            style = LensaTheme.typography.signature,
        )
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                value = otpInput,
                placeholder = "000000",
                onValueChanged = {
                    otpInput = it
                }
            )
            VSpace(h = 16.dp)
            LensaTextButton(
                isFillMaxWidth = true,
                text = if (isResendEnabled) {
                    "Отправить новый код"
                } else {
                    "Отправить еще раз через 01:00"
                },
                onClick = {},

                type = if (isResendEnabled) LensaTextButtonType.ACCENT
                else LensaTextButtonType.SECONDARY,
            )
            VSpace(h = 16.dp)
            LensaButton(
                text = "ПРОДОЛЖИТЬ",
                onClick = {
                    onNextClick(otpInput)
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
fun OtpScreenPreview() = LensaTheme {
    Screen(
        phoneNumber = "",
        onNextClick = {},
    )
}