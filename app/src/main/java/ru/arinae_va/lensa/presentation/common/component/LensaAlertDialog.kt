package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun LensaAlertDialog(
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
    title: String,
    subtitle: String,
    confirmText: String,
    dismissText: String,
) {
    Dialog(
        onDismissRequest = onDismissClick,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 48.dp),
            color = LensaTheme.colors.backgroundColor,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 48.dp)
            ) {
                Text(
                    text = title,
                    style = LensaTheme.typography.header2,
                    color = LensaTheme.colors.textColor,
                )
                VSpace(h = 24.dp)
                Divider(color = LensaTheme.colors.dividerColor)
                VSpace(h = 24.dp)
                Text(
                    text = subtitle,
                    style = LensaTheme.typography.header3,
                    color = LensaTheme.colors.textColor,
                )
                VSpace(h = 48.dp)
                LensaButton(text = confirmText, onClick = onConfirmClick, isFillMaxWidth = true)
                VSpace(h = 20.dp)
                LensaTextButton(
                    text = dismissText,
                    onClick = onDismissClick,
                    isFillMaxWidth = true,
                    type = LensaTextButtonType.DEFAULT
                )
            }
        }
    }
}