package ru.arinae_va.lensa.presentation.feature.feed.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LabeledField
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.feature.feed.viewmodel.ProfileDetailsState
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun PersonalInfoSection(
    state: ProfileDetailsState,
    onSendMessageClick: (String) -> Unit,
) {
    with(state.userProfileModel) {
        SpecialistDetailsField(label = "Страна", text = country)
        SpecialistDetailsField(label = "Город", text = city)
        if (personalSite.isNotEmpty())
            SpecialistDetailsField(label = "Сайт", text = personalSite)
        if (email.isNotEmpty())
            SpecialistDetailsField(label = "Почта", text = email)
        VSpace(h = 12.dp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            socialMedias.forEach {
                Icon(
                    painter = painterResource(id = it.type.icon),
                    contentDescription = null,
                    tint = LensaTheme.colors.textColor,
                )
                HSpace(w = 20.dp)
            }
        }
        if (!state.isSelf) {
            VSpace(h = 12.dp)
            LensaTextButton(
                text = "Написать сообщение",
                onClick = {
                    onSendMessageClick.invoke(profileId)
                },
                type = LensaTextButtonType.ACCENT,
            )
        }
    }
}

@Composable
fun SpecialistDetailsField(
    label: String,
    text: String,
) {
    LabeledField(
        labelText = label,
        labelStyle = LensaTheme.typography.text,
        text = text,
        textStyle = LensaTheme.typography.text,
        separator = ": ",
        separatorStyle = LensaTheme.typography.text,
    )
}