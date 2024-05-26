package ru.arinae_va.lensa.presentation.feature.settings.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.LensaAlertDialog
import ru.arinae_va.lensa.presentation.common.component.LensaIconButton
import ru.arinae_va.lensa.presentation.common.component.LensaReplaceLoader
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.auth.compose.SelectProfileDialog
import ru.arinae_va.lensa.presentation.feature.settings.viewmodel.SettingsScreenState
import ru.arinae_va.lensa.presentation.feature.settings.viewmodel.SettingsViewModel
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
) {
    setSystemUiColor()
    val state by viewModel.state.collectAsState()
    SettingsContent(
        state = state,
        onAboutClick = viewModel::onAboutClick,
        onDeleteClick = viewModel::onDeleteAccountClick,
        onBackPressed = viewModel::onBackPressed,
        onEditProfileClick = viewModel::onEditProfileClick,
        onThemeSwitched = viewModel::onThemeSwitched,
        onFeedbackClick = viewModel::onFeedbackClick,
        onExitClick = viewModel::onExitClick,
        onHideDeleteProfileDialog = viewModel::hideDeleteDialog,
        onHideExitDialog = viewModel::hideExitDialog,
        onHideSelectProfileDialog = viewModel::hideSelectProfileDialog,
        onAddProfileClick = viewModel::onAddProfileClick,
        onShowDeleteProfileDialog = viewModel::showDeleteDialog,
        onShowSelectProfileDialog = viewModel::showSelectProfileDialog,
        onShowExitDialog = viewModel::showExitDialog,
        onSelectOtherProfile = viewModel::onSelectProfile,
    )
}

@Composable
private fun SettingsContent(
    state: SettingsScreenState,
    onThemeSwitched: (Boolean) -> Unit,
    onAboutClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onFeedbackClick: () -> Unit,
    onExitClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onBackPressed: () -> Unit,
    onHideExitDialog: () -> Unit,
    onShowExitDialog: () -> Unit,
    onHideDeleteProfileDialog: () -> Unit,
    onShowDeleteProfileDialog: () -> Unit,
    onHideSelectProfileDialog: () -> Unit,
    onShowSelectProfileDialog: () -> Unit,
    onAddProfileClick: () -> Unit,
    onSelectOtherProfile: (String) -> Unit,
) {
    if (state.isShowExitDialog) {
        LensaAlertDialog(
            onConfirmClick = onExitClick,
            onDismissClick = onHideExitDialog,
            title = "ВЫХОД",
            subtitle = "ВЫ ДЕЙСТВИТЕЛЬНО ХОТИТЕ ВЫЙТИ ИЗ АККАУНТА?",
            confirmText = "ВЫЙТИ",
            dismissText = "ОТМЕНИТЬ",
        )
    }

    if (state.isShowDeleteProfileDialog) {
        LensaAlertDialog(
            onConfirmClick = onDeleteClick,
            onDismissClick = onHideDeleteProfileDialog,
            title = "УДАЛЕНИЕ\nАККАУНТА",
            subtitle = "ВЫ ДЕЙСТВИТЕЛЬНО ХОТИТЕ УДАЛИТЬ АККАУНТ?",
            confirmText = "УДАЛИТЬ",
            dismissText = "ОТМЕНИТЬ",
        )
    }

    if (state.isShowSelectProfileDialog) {
        SelectProfileDialog(
            profiles = state.userProfiles,
            onProfileSelected = onSelectOtherProfile,
            onDismiss = onHideSelectProfileDialog,
            onAddProfileClicked = onAddProfileClick,
            dismissButtonText = state.selectProfileDialogDismissButtonText,
        )
    }

    LensaReplaceLoader(
        isLoading = state.isLoading,
    ) {
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
                    onClick = onShowSelectProfileDialog,
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
                Switch(
                    thumbContent = {
                        if (state.isDarkModeEnabled) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_moon),
                                contentDescription = null,
                                tint = LensaTheme.colors.backgroundColor,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_sun),
                                contentDescription = null,
                                tint = LensaTheme.colors.backgroundColor,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    },
                    checked = state.isDarkModeEnabled,
                    onCheckedChange = onThemeSwitched,
                    colors = SwitchDefaults.colors(
                        checkedBorderColor = LensaTheme.colors.textColor,
                        checkedThumbColor = LensaTheme.colors.textColor,
                        checkedTrackColor = LensaTheme.colors.backgroundColor,
                        uncheckedThumbColor = LensaTheme.colors.textColorAccent,
                        uncheckedTrackColor = LensaTheme.colors.backgroundColor,
                        uncheckedBorderColor = LensaTheme.colors.textColor,
                    )
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
                onClick = onShowExitDialog,
                type = LensaTextButtonType.DEFAULT,
            )
            VSpace(h = 16.dp)
            LensaTextButton(
                text = "Удалить аккаунт",
                onClick = onShowDeleteProfileDialog,
                type = LensaTextButtonType.ACCENT,
            )
            VSpace(h = 64.dp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    LensaTheme {
        SettingsContent(
            state = SettingsScreenState(
                userProfiles = emptyList(),
                isDarkModeEnabled = false,
                isLoading = false,
                isShowDeleteProfileDialog = true,
                isShowExitDialog = false,
                isShowSelectProfileDialog = false,
                selectProfileDialogDismissButtonText = "",
            ),
            onDeleteClick = {},
            onAboutClick = {},
            onBackPressed = {},
            onEditProfileClick = {},
            onThemeSwitched = {},
            onFeedbackClick = {},
            onExitClick = {},
            onSelectOtherProfile = {},
            onShowExitDialog = {},
            onShowSelectProfileDialog = {},
            onShowDeleteProfileDialog = {},
            onAddProfileClick = {},
            onHideSelectProfileDialog = {},
            onHideExitDialog = {},
            onHideDeleteProfileDialog = {},
        )
    }
}