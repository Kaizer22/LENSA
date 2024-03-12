package ru.arinae_va.lensa.presentation.feature.auth.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.LensaButton
import ru.arinae_va.lensa.presentation.common.component.LensaHeader
import ru.arinae_va.lensa.presentation.common.component.LensaInput
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.auth.viewmodel.AuthScreenField
import ru.arinae_va.lensa.presentation.feature.auth.viewmodel.AuthScreenState
import ru.arinae_va.lensa.presentation.feature.auth.viewmodel.AuthViewModel
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
) {
    setSystemUiColor()
    val state by viewModel.state.collectAsState()
    Screen(
        state = state,
        onPhoneNumberChanged = viewModel::onPhoneNumberChanged,
        onTermsClick = {},
        onConditionsClick = {},
        onSendVerificationCodeClick = viewModel::onEnterPhoneNumber,
    )
}

@Composable
private fun Screen(
    state: AuthScreenState,
    onTermsClick: () -> Unit,
    onConditionsClick: () -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
    onSendVerificationCodeClick: () -> Unit,
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
            text = stringResource(R.string.auth_screen_phone_number_input_label),
            style = LensaTheme.typography.text,
        )
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                value = state.phoneNumber,
                inputType = KeyboardType.Phone,
                onValueChanged = onPhoneNumberChanged,
            )
            state.validationErrors[AuthScreenField.PHONE_NUMBER]?.let {
                Text(
                    text = it,
                    style = LensaTheme.typography.hint,
                    color = LensaTheme.colors.textColorSecondary,
                )
            }
            VSpace(h = 52.dp)
            LensaButton(
                text = stringResource(R.string.auth_screen_button_request_code),
                onClick = onSendVerificationCodeClick,
                enabled = state.isEnabledNextButton,
                isFillMaxWidth = true,
            )
            FSpace()
            // TODO ссылки
            Text(
                modifier = Modifier.padding(40.dp),
                text = stringResource(R.string.auth_screen_terms_and_conditions_hint),
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
            state = AuthScreenState(
                isEnabledNextButton = false,
                phoneNumber = "+7",
                validationErrors = mapOf(
                    AuthScreenField.PHONE_NUMBER to "Ошибка!"
                ),
            ),
            onTermsClick = {},
            onConditionsClick = {},
            onPhoneNumberChanged = {},
            onSendVerificationCodeClick = {},
        )
    }
}