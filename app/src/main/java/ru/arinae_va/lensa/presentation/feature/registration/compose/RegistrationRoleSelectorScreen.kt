package ru.arinae_va.lensa.presentation.feature.registration.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.LensaButtonWithIcon
import ru.arinae_va.lensa.presentation.common.component.LensaHeader
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun RegistrationRoleSelectorScreen(
    navController: NavController,
) {
    setSystemUiColor()
    Screen(
        onSpecialistClick = {
            navController.navigate(LensaScreens.REGISTRATION_SCREEN.name)
        },
        onCustomerClick = {
            navController.navigate(LensaScreens.REGISTRATION_SCREEN.name)
        }
    )
}

@Composable
private fun Screen(
    onSpecialistClick: () -> Unit,
    onCustomerClick: () -> Unit,
) {
    Column(modifier = Modifier.background(color = LensaTheme.colors.backgroundColor)) {
        LensaHeader()
        FSpace()
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
            icon = R.drawable.ic_arrow_forward_2,
            text = "ЗАКАЗЧИК",
            isFillMaxWidth = true,
            onClick = onCustomerClick,
        )
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = LensaTheme.colors.dividerColor,
        )
        FSpace()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistrationRoleSelectorScreenPreview() {
    LensaTheme {
        Screen(
            onCustomerClick = {},
            onSpecialistClick = {},
        )
    }
}