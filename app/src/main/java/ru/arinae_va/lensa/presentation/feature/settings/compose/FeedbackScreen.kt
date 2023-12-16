package ru.arinae_va.lensa.presentation.feature.settings.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.navigation.navOptions
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.LensaHeader
import ru.arinae_va.lensa.presentation.common.component.LensaIconButton
import ru.arinae_va.lensa.presentation.common.component.LensaInput
import ru.arinae_va.lensa.presentation.common.component.LensaMultilineInput
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun FeedbackScreen(
    navController: NavController,
    viewModel: SettingsViewModel,
) {
    setSystemUiColor()
    Screen(
        onBackPressed = {
            navController.popBackStack()
        },
        onSendPressed = viewModel::onSendFeedbackClick,
    )
}

@Composable
private fun Screen(
    onBackPressed: () -> Unit,
    onSendPressed: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LensaTheme.colors.backgroundColor)
    ) {
        LensaHeader()
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            VSpace(h = 24.dp)
            Text(
                text = "ОБРАТНАЯ\nСВЯЗЬ",
                style = LensaTheme.typography.header2,
                color = LensaTheme.colors.textColor,
            )
            VSpace(h = 48.dp)
            LensaMultilineInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = {
                    text = it
                },
                value = text,
                linesCount = 6,
                placeholder = "Обратная связь",
            )
            VSpace(h = 12.dp)
            Row {
                FSpace()
                LensaTextButton(
                    text = "ОТПРАВИТЬ",
                    onClick = {
                        onSendPressed(text)
                    },
                    type = LensaTextButtonType.DEFAULT,
                )
            }
            FSpace()
            LensaIconButton(
                icon = R.drawable.ic_arrow_back,
                iconSize = 60.dp,
                onClick = onBackPressed,
            )
            VSpace(100.dp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FeedbackScreenPreview() {
    LensaTheme {
        Screen(
            onSendPressed = {},
            onBackPressed = {},
        )
    }
}