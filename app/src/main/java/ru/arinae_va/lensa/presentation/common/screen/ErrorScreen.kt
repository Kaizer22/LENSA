package ru.arinae_va.lensa.presentation.common.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun ErrorScreen(
    navController: NavController,
    text: String = "ПРОИЗО\nШЛА\nКАКАЯ-\nТО\nОШИБКА",
) {
    setSystemUiColor()
    Screen(
        onBackPressed = navController::popBackStack,
        text = text,
    )
}

@Composable
private fun Screen(
    onBackPressed: () -> Unit,
    text: String,
) {
    Column(
        modifier = Modifier.background(
            color = LensaTheme.colors.backgroundColor,
        )
    ) {
        LensaHeader()
        VSpace(100.dp)
        Text(
            modifier = Modifier.padding(start = 16.dp),
            style = LensaTheme.typography.header1,
            color = LensaTheme.colors.textColor,
            text = text,
        )
        FSpace()
        LensaIconButton(
            modifier = Modifier.padding(start = 16.dp),
            icon = R.drawable.ic_arrow_back,
            iconSize = 60.dp,
            onClick = onBackPressed,
        )
        VSpace(100.dp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AuthScreenPreview() {
    LensaTheme {
        Screen(
            onBackPressed = {},
            text = "ПРОИЗО\nШЛА\nКАКАЯ-\nТО\nОШИБКА"
        )
    }
}