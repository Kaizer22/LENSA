package ru.arinae_va.lensa.presentation.feature.registration.compose.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaButton
import ru.arinae_va.lensa.presentation.common.component.LensaInput
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.feature.feed.compose.SocialMediaType
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun SocialMediaCreatorDialog(
    onSaveClick: () -> Unit,
    onDismissClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissClick,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        ),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = LensaTheme.colors.backgroundColor
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp, vertical = 48.dp
                    )
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "СОЦСЕТИ",
                    style = LensaTheme.typography.header2,
                    color = LensaTheme.colors.textColor,
                )
                VSpace(h = 24.dp)
                SocialMediaType.values().forEach { socialMediaType ->
                    LensaInput(
                        modifier = Modifier.fillMaxWidth(),
                        showLeadingIcon = true,
                        leadingIconRes = socialMediaType.icon,
                        onValueChanged = {},
                        defaultValue = "",
                    )
                    VSpace(h = 12.dp)
                }
                VSpace(h = 36.dp)
                LensaButton(
                    text = "СОХРАНИТЬ",
                    isFillMaxWidth = true,
                    onClick = onSaveClick,
                )
                VSpace(h = 20.dp)
                LensaTextButton(
                    text = "ОТМЕНИТЬ",
                    isFillMaxWidth = true,
                    onClick = onDismissClick,
                    type = LensaTextButtonType.DEFAULT,
                )
                VSpace(h = 36.dp)
                Row {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        tint = LensaTheme.colors.textColorAccent,
                        painter = painterResource(id = R.drawable.ic_4_star),
                        contentDescription = null
                    )
                    HSpace(12.dp)
                    Text(
                        text = "Instagram принадлежит компании Meta, признанной экстремистской " +
                                "организацией и запрещенной в РФ",
                        style = LensaTheme.typography.signature,
                        color = LensaTheme.colors.textColorSecondary,
                    )
                }
                VSpace(h = 48.dp)
            }
        }
    }
}