package ru.arinae_va.lensa.presentation.feature.registration.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.LensaButtonWithIcon
import ru.arinae_va.lensa.presentation.common.component.LensaHeader
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.registration.viewmodel.RegistrationViewModel
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun RegistrationRoleSelectorScreen(
    viewModel: RegistrationViewModel,
) {
    setSystemUiColor()
    RegistrationContent(
        onSpecialistClick = {
            viewModel.onSelectAccountTypeClick(isSpecialist = true)
        },
        onCustomerClick = {
            viewModel.onSelectAccountTypeClick(isSpecialist = false)
        }
    )
}

@Composable
private fun RegistrationContent(
    onSpecialistClick: () -> Unit,
    onCustomerClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = LensaTheme.colors.backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Column {
            LensaHeader()
            FSpace()
        }
        Column {
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = LensaTheme.colors.dividerColor,
            )
            LensaButtonWithIcon(
                icon = R.drawable.ic_arrow_forward_2,
                text = "СПЕЦИАЛИСТ",
                isFillMaxWidth = true,
                onClick = onSpecialistClick,
            )
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = LensaTheme.colors.dividerColor,
            )
            LensaButtonWithIcon(
                modifier = Modifier.testTag("role_selector_screen_button_customer"),
                icon = R.drawable.ic_arrow_forward_2,
                text = "ЗАКАЗЧИК",
                isFillMaxWidth = true,
                onClick = onCustomerClick,
            )
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = LensaTheme.colors.dividerColor,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistrationRoleSelectorScreenPreview() {
    LensaTheme {
        RegistrationContent(
            onCustomerClick = {},
            onSpecialistClick = {},
        )
    }
}