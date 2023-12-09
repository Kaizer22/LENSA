package ru.arinae_va.lensa.presentation.feature.settings.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.LensaIconButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel,
) {
    setSystemUiColor()
    Screen(
        onAboutClick = {
            navController.navigate(LensaScreens.ABOUT_APP_SCREEN.name)
        },
        onBackPressed = {
            navController.popBackStack()
        },
        onEditProfileClick = {
            navController.navigate(LensaScreens.PROFILE_SCREEN.name)
        },
        onThemeSwitched = {},
        onFeedbackClick = {
            navController.navigate(LensaScreens.FEEDBACK_SCREEN.name)
        },
        onExitClick = {
            navController.navigate(LensaScreens.AUTH_SCREEN.name)
        }
    )
}

@Composable
private fun Screen(
    onThemeSwitched: (Boolean) -> Unit,
    onAboutClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onFeedbackClick: () -> Unit,
    onExitClick: () -> Unit,
    onBackPressed: () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        VSpace(h = 28.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LensaIconButton(
                onClick = onBackPressed,
                icon = R.drawable.ic_arrow_back,
                iconSize = 30.dp,
            )
            FSpace()
            LensaTextButton(
                text = "Сменить аккаунт",
                type = LensaTextButtonType.ACCENT,
                onClick = {},
            )
        }
        VSpace(h = 28.dp)
        Text(
            text = "НАСТРОЙКИ",
            style = LensaTheme.typography.header2,
            color = LensaTheme.colors.textColor,
        )
        VSpace(h = 80.dp)
        Divider(color = LensaTheme.colors.dividerColor)
        VSpace(h = 16.dp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "ТЕМА",
                style = LensaTheme.typography.textButton,
            )
            FSpace()
            // TODO custom switch
            Switch(
                checked = true,
                onCheckedChange = onThemeSwitched,
            )
        }
        VSpace(h = 16.dp)
        Divider(color = LensaTheme.colors.dividerColor)
        VSpace(h = 16.dp)
        LensaTextButton(
            text = "О ПРИЛОЖЕНИИ",
            onClick = onAboutClick,
            type = LensaTextButtonType.DEFAULT,
        )
        VSpace(h = 16.dp)
        Divider(color = LensaTheme.colors.dividerColor)
        VSpace(h = 16.dp)
        LensaTextButton(
            text = "РЕДАКТИРОВАТЬ\nПРОФИЛЬ",
            onClick = onEditProfileClick,
            type = LensaTextButtonType.DEFAULT,
        )
        VSpace(h = 16.dp)
        Divider(color = LensaTheme.colors.dividerColor)
        VSpace(h = 16.dp)
        LensaTextButton(
            text = "ОБРАТНАЯ СВЯЗЬ",
            onClick = onFeedbackClick,
            type = LensaTextButtonType.DEFAULT,
        )
        VSpace(h = 16.dp)
        Divider(color = LensaTheme.colors.dividerColor)
        FSpace()
        LensaTextButton(
            text = "ВЫЙТИ",
            onClick = onExitClick,
            type = LensaTextButtonType.DEFAULT,
        )
        VSpace(h = 16.dp)
        LensaTextButton(
            text = "Удалить аккаунт",
            onClick = {
                // TODO
            },
            type = LensaTextButtonType.ACCENT,
        )
        VSpace(h = 64.dp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    LensaTheme {
        Screen(
            onAboutClick = {},
            onBackPressed = {},
            onEditProfileClick = {},
            onThemeSwitched = {},
            onFeedbackClick = {},
            onExitClick = {}
        )
    }
}