package ru.arinae_va.lensa.presentation.feature.registration.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.presentation.common.component.LensaDropdownInput
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.feature.registration.viewmodel.RegistrationScreenState
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import ru.arinae_va.lensa.utils.Constants

@Composable
internal fun SpecializationSection(
    state: RegistrationScreenState,
    onValueChanged: (String) -> Unit,
    onGetInTouchClick: () -> Unit,
) {
    LensaDropdownInput(
        value = state.specialization,
        allowFreeInput = false,
        showRequired = true,
        modifier = Modifier.fillMaxWidth(),
        onValueChanged = onValueChanged,
        items = Constants.SPECIALIZATIONS_LIST.map { it.first },
        placeholder = "Специализация",
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
        onClick = onGetInTouchClick
    )
}