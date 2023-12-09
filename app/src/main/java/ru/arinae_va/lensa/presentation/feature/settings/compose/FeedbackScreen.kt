package ru.arinae_va.lensa.presentation.feature.settings.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import ru.arinae_va.lensa.presentation.common.component.LensaInput
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun FeedbackScreen(
    navController: NavController,
    viewModel: SettingsViewModel,
) {
    setSystemUiColor()
    Screen()
}

@Composable
private fun Screen() {
    Column {
        LensaHeader()
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            VSpace(h = 24.dp)
            Text(
                text = "ОБРАТНАЯ\nСВЯЗЬ",
                style = LensaTheme.typography.header2,
            )
            VSpace(h = 48.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = {},
                defaultValue = "",
                placeholder = "Обратная связь",
            )
            FSpace()
            LensaIconButton(
                icon = R.drawable.ic_arrow_back,
                iconSize = 60.dp,
                onClick = {}
            )
            VSpace(100.dp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FeedbackScreenPreview() {
    LensaTheme {
        Screen()
    }
}