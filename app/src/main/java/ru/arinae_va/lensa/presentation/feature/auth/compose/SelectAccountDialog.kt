package ru.arinae_va.lensa.presentation.feature.auth.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.arinae_va.lensa.domain.model.UserProfileModel
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaAsyncImage
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun SelectProfileDialog(
    profiles: List<UserProfileModel>,
    onProfileSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    onAddProfileClicked: () -> Unit,
    dismissButtonText: String = "ОТМЕНИТЬ",
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        ),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 48.dp
                ),
            color = LensaTheme.colors.backgroundColor
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp, vertical = 48.dp
                    )
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                profiles.forEach { profile ->
                    ProfileItem(
                        profile = profile,
                        onItemClicked = {
                            onProfileSelected.invoke(profile.profileId)
                        }
                    )
                }
                Divider(color = LensaTheme.colors.dividerColor)
                VSpace(h = 48.dp)
                LensaTextButton(
                    type = LensaTextButtonType.ACCENT,
                    text = "Добавить аккаунт",
                    onClick = onAddProfileClicked
                )
                VSpace(h = 16.dp)
                LensaTextButton(
                    text = dismissButtonText,
                    onClick = onDismiss,
                    type = LensaTextButtonType.DEFAULT,
                )
            }
        }
    }

}

@Composable
fun ProfileItem(
    profile: UserProfileModel,
    onItemClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.clickable(
            onClick = onItemClicked,
        )
    ) {
        Divider(color = LensaTheme.colors.dividerColor)
        VSpace(h = 8.dp)
        Row {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(shape = LensaTheme.shapes.roundShape),
            ) {
                LensaAsyncImage(
                    pictureUrl = profile.avatarUrl.orEmpty(),
                )
            }
            HSpace(w = 12.dp)
            Column {
                Text(
                    text = "${profile.name.uppercase()} ${profile.surname.uppercase()}",
                    style = LensaTheme.typography.name,
                    color = LensaTheme.colors.textColor,
                )
                VSpace(h = 4.dp)
                Text(
                    text = profile.specialization,
                    style = LensaTheme.typography.signature,
                    color = LensaTheme.colors.textColorSecondary,
                )
            }
        }
        VSpace(h = 8.dp)
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SelectProfileDialogPreview() {
    LensaTheme {
        SelectProfileDialog(
            profiles = listOf(
                UserProfileModel.EMPTY,
                UserProfileModel.EMPTY,
            ),
            onProfileSelected = {},
            onDismiss = { },
            onAddProfileClicked = {}
        )
    }
}