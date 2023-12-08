package ru.arinae_va.lensa.presentation.feature.registration.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import ru.arinae_va.lensa.presentation.common.component.LensaHeader
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun RegistrationScreen(
    navController: NavController,
){
    setSystemUiColor()
}

@Composable
private fun Screen() {
    Column {
        LensaHeader()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistrationScreenPreview() {
    LensaTheme {
        Screen()
    }
}