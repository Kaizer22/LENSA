package ru.arinae_va.lensa.presentation.feature.auth.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.LensaButton
import ru.arinae_va.lensa.presentation.common.component.LensaHeader
import ru.arinae_va.lensa.presentation.common.component.LensaInput
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.auth.viewmodel.OtpScreenInputField
import ru.arinae_va.lensa.presentation.feature.auth.viewmodel.OtpScreenState
import ru.arinae_va.lensa.presentation.feature.auth.viewmodel.OtpViewModel
import ru.arinae_va.lensa.presentation.theme.LensaTheme

private const val RESEND_DELAY_SECONDS = 60

@Composable
fun OtpScreen(
    viewModel: OtpViewModel,
) {
    val state by viewModel.state.collectAsState()
    setSystemUiColor()
    Screen(
        state = state,
        onEnableResend = viewModel::enableResend,
        onResendOtp = viewModel::onResendOtp,
        onOtpInputChanged = viewModel::onOtpInputChanged,
        onNextClick = viewModel::verifyCode
    )
}

@Composable
private fun Screen(
    state: OtpScreenState,
    onResendOtp: () -> Unit,
    onOtpInputChanged: (String) -> Unit,
    onEnableResend: () -> Unit,
    onNextClick: () -> Unit,
) {
    // TODO add loaders
    var timeToResend by remember { mutableStateOf(RESEND_DELAY_SECONDS) }
    LaunchedEffect(Unit) {
        while (timeToResend != 0) {
            delay(1000)
            timeToResend--
        }
        onEnableResend.invoke()
    }
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
            text = stringResource(R.string.otp_screen_input_label),
            style = LensaTheme.typography.text,
        )
        Text(
            color = LensaTheme.colors.textColor,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
            text = stringResource(R.string.otp_screen_sent_message, state.phoneNumber),
            style = LensaTheme.typography.signature,
        )
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                value = state.otpInput,
                inputType = KeyboardType.Number,
                placeholder = stringResource(R.string.otp_screen_otp_input_placeholder),
                onValueChanged = onOtpInputChanged,
            )
            state.validationErrors[OtpScreenInputField.OTP_CODE]?.let {
                Text(
                    text = it,
                    style = LensaTheme.typography.signature,
                    color = LensaTheme.colors.textColorSecondary,
                )
            }
            VSpace(h = 16.dp)
            if (!state.isOtpCodeResent) {
                LensaTextButton(
                    isFillMaxWidth = true,
                    text = if (state.isResendEnabled) {
                        stringResource(R.string.otp_screen_send_new_code)
                    } else {
                        stringResource(
                            R.string.otp_screen_resend_counter_hint,
                            timeToResend / 60,
                            timeToResend % 60
                        )
                    },
                    onClick = { if (state.isResendEnabled) onResendOtp.invoke() },
                    type = if (state.isResendEnabled) LensaTextButtonType.ACCENT
                    else LensaTextButtonType.SECONDARY,
                )
            } else {
                Text(
                    text = stringResource(R.string.otp_screen_otp_resent_hint),
                    style = LensaTheme.typography.signature,
                    color = LensaTheme.colors.textColorSecondary,
                )
            }

            VSpace(h = 16.dp)
            LensaButton(
                text = stringResource(R.string.otp_screen_continue_btn),
                onClick = onNextClick,
                isFillMaxWidth = true,
                enabled = state.isButtonNextEnabled,
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
        state = OtpScreenState(
            phoneNumber = "",
            otpInput = "",
            isOtpCodeResent = true,
            isResendEnabled = false,
            isButtonNextEnabled = false,
            validationErrors = emptyMap(),
            verificationId = null,
            token = null,
        ),
        onEnableResend = {},
        onResendOtp = {},
        onNextClick = {},
        onOtpInputChanged = {},
    )
}