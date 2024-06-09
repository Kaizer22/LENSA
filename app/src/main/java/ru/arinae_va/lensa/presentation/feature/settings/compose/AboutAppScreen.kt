package ru.arinae_va.lensa.presentation.feature.settings.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.LensaHeader
import ru.arinae_va.lensa.presentation.common.component.LensaIconButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun AboutAppScreen(
    navController: NavController,
) {
    setSystemUiColor()
    AboutAppContent(
        onBackPressed = {
            navController.popBackStack()
        },
        onTermsAndConditionsClick = {},
    )
}

@Composable
private fun AboutAppContent(
    onBackPressed: () -> Unit,
    onTermsAndConditionsClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LensaTheme.colors.backgroundColor)
            .padding(horizontal = 16.dp)
    ) {
        LensaHeader()
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "ВЕРСИЯ",
            style = LensaTheme.typography.header1,
            color = LensaTheme.colors.textColor,
        )
        Text(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 32.dp,
            ),
            text = "0.0.9\nDEV",
            style = LensaTheme.typography.header2,
            color = LensaTheme.colors.textColor,
        )
        VSpace(h = 32.dp)
        LensaTextButton(
            modifier = Modifier.padding(start = 16.dp),
            text = "Политика конфиденциальности\nи условия сервиса",
            onClick = onTermsAndConditionsClick,
            type = LensaTextButtonType.ACCENT,
        )
        FSpace()
        LensaIconButton(
            modifier = Modifier.padding(start = 16.dp),
            icon = R.drawable.ic_arrow_back,
            iconSize = 60.dp,
            onClick = onBackPressed,
        )
        VSpace(h = 64.dp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AboutAppScreenPreview() {
    LensaTheme {
        AboutAppContent(
            onBackPressed = {},
            onTermsAndConditionsClick = {},
        )
    }
}