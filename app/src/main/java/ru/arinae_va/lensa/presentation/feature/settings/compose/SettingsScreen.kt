package ru.arinae_va.lensa.presentation.feature.settings.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.LensaAlertDialog
import ru.arinae_va.lensa.presentation.common.component.LensaIconButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.common.utils.todo
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel,
) {
    setSystemUiColor()
    val context = LocalContext.current
    Screen(
        onAboutClick = {
            navController.navigate(LensaScreens.ABOUT_APP_SCREEN.name)
        },
        onDeleteClick = viewModel::onDeleteClick,
        onBackPressed = {
            navController.popBackStack()
        },
        onEditProfileClick = {
            todo(context)
            //navController.navigate(LensaScreens.PROFILE_SCREEN.name)
        },
        onThemeSwitched = {},
        onFeedbackClick = {
            navController.navigate(LensaScreens.FEEDBACK_SCREEN.name)
        },
        onExitClick = viewModel::onExitClick
    )
}

@Composable
private fun Screen(
    onThemeSwitched: (Boolean) -> Unit,
    onAboutClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onFeedbackClick: () -> Unit,
    onExitClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onBackPressed: () -> Unit,
) {
    var showExitDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showExitDialog) {
        LensaAlertDialog(
            onConfirmClick = onExitClick,
            onDismissClick = { showExitDialog = false },
            title = "ВЫХОД",
            subtitle = "ВЫ ДЕЙСТВИТЕЛЬНО ХОТИТЕ ВЫЙТИ ИЗ АККАУНТА?",
            confirmText = "ВЫЙТИ",
            dismissText = "ОТМЕНИТЬ",
        )
    }

    if (showDeleteDialog) {
        LensaAlertDialog(
            onConfirmClick = onDeleteClick,
            onDismissClick = { showDeleteDialog = false },
            title = "УДАЛЕНИЕ\nАККАУНТА",
            subtitle = "ВЫ ДЕЙСТВИТЕЛЬНО ХОТИТЕ УДАЛИТЬ АККАУНТ?",
            confirmText = "УДАЛИТЬ",
            dismissText = "ОТМЕНИТЬ",
        )
    }

    Column(
        modifier = Modifier
            .background(color = LensaTheme.colors.backgroundColor)
            .padding(horizontal = 16.dp)
    ) {
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
                style = LensaTheme.typography.textButtonDefault,
                color = LensaTheme.colors.textColor,
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
            onClick = {
                showExitDialog = true
            },
            type = LensaTextButtonType.DEFAULT,
        )
        VSpace(h = 16.dp)
        LensaTextButton(
            text = "Удалить аккаунт",
            onClick = {
                showDeleteDialog = true
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
            onDeleteClick = {},
            onAboutClick = {},
            onBackPressed = {},
            onEditProfileClick = {},
            onThemeSwitched = {},
            onFeedbackClick = {},
            onExitClick = {}
        )
    }
}