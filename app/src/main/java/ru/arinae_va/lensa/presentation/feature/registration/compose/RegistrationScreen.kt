package ru.arinae_va.lensa.presentation.feature.registration.compose

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.domain.model.Price
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaButton
import ru.arinae_va.lensa.presentation.common.component.LensaDropdownInput
import ru.arinae_va.lensa.presentation.common.component.LensaImagePicker
import ru.arinae_va.lensa.presentation.common.component.LensaInput
import ru.arinae_va.lensa.presentation.common.component.LensaMultilineInput
import ru.arinae_va.lensa.presentation.common.component.LensaReplaceLoader
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.feed.compose.SocialMediaType
import ru.arinae_va.lensa.presentation.feature.registration.compose.dialog.PriceListCreatorDialog
import ru.arinae_va.lensa.presentation.feature.registration.compose.dialog.SocialMediaCreatorDialog
import ru.arinae_va.lensa.presentation.feature.registration.viewmodel.RegistrationScreenState
import ru.arinae_va.lensa.presentation.feature.registration.viewmodel.RegistrationViewModel
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import ru.arinae_va.lensa.utils.Constants

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel,
) {
    setSystemUiColor()
    val state by viewModel.state.collectAsState()
    RegistrationContent(
        state = state,
        onNameChanged = viewModel::onNameChanged,
        onSurnameChanged = viewModel::onSurnameChanged,
        onAboutChanged = viewModel::onAboutChanged,
        onCityChanged = viewModel::onCityChanged,
        onCountryChanged = viewModel::onCountryChanged,
        onEmailChanged = viewModel::onEmailChanged,
        onPhoneNumberChanged = viewModel::onPhoneNumberChanged,
        onSpecializationChanged = viewModel::onSpecializationChanged,
        onPersonalWebsiteChanged = viewModel::onPersonalWebsiteChanged,
        onGetInTouchClick = viewModel::onGetInTouchClick,
        onPricesListChanged = viewModel::onPricesChanged,
        onSocialMediasChanged = viewModel::onSocialMediasChanged,
        onAvatarChanged = viewModel::onAvatarChanged,
        onPortfolioChanged = viewModel::onPortfolioChanged,
        onSaveClick = viewModel::onSaveClick,
        onDismissClick = viewModel::onDismissClick,
    )
}

@Composable
private fun RegistrationContent(
    state: RegistrationScreenState,
    onNameChanged: (String) -> Unit,
    onSurnameChanged: (String) -> Unit,
    onSpecializationChanged: (String) -> Unit,
    onCountryChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onAboutChanged: (String) -> Unit,
    onPersonalWebsiteChanged: (String) -> Unit,
    onPricesListChanged: (List<Price>) -> Unit,
    onSocialMediasChanged: (Map<SocialMediaType, String>) -> Unit,
    onAvatarChanged: (Uri) -> Unit,
    onPortfolioChanged: (List<Uri>) -> Unit,
    onSaveClick: () -> Unit,
    onGetInTouchClick: () -> Unit,
    onDismissClick: () -> Unit,
) {

    var showPriceListDialog by remember { mutableStateOf(false) }
    if (showPriceListDialog) {
        PriceListCreatorDialog(
            defaultPricesList = state.prices,
            onSaveClick = { list ->
                onPricesListChanged.invoke(list)
                showPriceListDialog = false
            },
            onDismissClick = {
                showPriceListDialog = false
            },
        )
    }
    var showSocialMediaDialog by remember { mutableStateOf(false) }
    if (showSocialMediaDialog) {
        SocialMediaCreatorDialog(
            defaultMedias = state.socialMedias,
            onSaveClick = { socialMediasMap ->
                onSocialMediasChanged(socialMediasMap)
                showSocialMediaDialog = false
            },
            onDismissClick = {
                showSocialMediaDialog = false
            },
        )
    }

    LensaReplaceLoader(
        isLoading = state.isLoading,
    ) {
        Column(
            modifier = Modifier
                .background(color = LensaTheme.colors.backgroundColor)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                VSpace(h = 40.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    LensaImagePicker(
                        defaultLink = state.avatarUri?.toString(),
                        modifier = Modifier.size(216.dp),
                        onImagePicked = onAvatarChanged,
                        emptyStateButtonSize = 48.dp,
                    )
                }
                VSpace(h = 28.dp)
                Text(
                    text = "Все заполненные поля будут видны другим пользователям",
                    style = LensaTheme.typography.signature,
                    color = LensaTheme.colors.textColorSecondary,
                )
                VSpace(h = 16.dp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_4_star),
                        tint = LensaTheme.colors.textColorAccent,
                        contentDescription = "",
                    )
                    HSpace(w = 12.dp)
                    Text(
                        text = "Поле, обязательное для заполнения",
                        style = LensaTheme.typography.signature,
                        color = LensaTheme.colors.textColorSecondary,
                    )
                }
                VSpace(h = 24.dp)
                LensaInput(
                    inputType = KeyboardType.Text,
                    showRequired = true,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChanged = onSurnameChanged,
                    value = state.surname,
                    placeholder = "Фамилия"
                )
                VSpace(h = 12.dp)
                LensaInput(
                    inputType = KeyboardType.Text,
                    showRequired = true,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChanged = onNameChanged,
                    value = state.name,
                    placeholder = "Имя"
                )
                if (state.isSpecialistRegistrationScreen) {
                    VSpace(h = 12.dp)
                    SpecializationSection(
                        state = state,
                        onValueChanged = onSpecializationChanged,
                        onGetInTouchClick = onGetInTouchClick,
                    )
                }
                VSpace(h = 12.dp)
                LensaDropdownInput(
                    inputType = KeyboardType.Text,
                    allowFreeInput = true,
                    showRequired = true,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChanged = onCountryChanged,
                    value = state.country,
                    placeholder = "Страна",
                    items = Constants.COUNTRIES_LIST,
                )
                VSpace(h = 12.dp)
                LensaDropdownInput(
                    value = state.city,
                    allowFreeInput = true,
                    showRequired = true,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChanged = onCityChanged,
                    placeholder = "Город",
                    items = Constants.RUSSIAN_CITIES_LIST,
                )
                VSpace(h = 12.dp)
                LensaInput(
                    modifier = Modifier.fillMaxWidth(),
                    onValueChanged = onPhoneNumberChanged,
                    value = state.phoneNumber,
                    inputType = KeyboardType.Phone,
                    placeholder = "Номер телефона (+7...)"
                )
                VSpace(h = 12.dp)
                LensaInput(
                    modifier = Modifier.fillMaxWidth(),
                    onValueChanged = onEmailChanged,
                    value = state.email,
                    placeholder = "Почта"
                )
                VSpace(h = 12.dp)
                LensaMultilineInput(
                    modifier = Modifier.fillMaxWidth(),
                    onValueChanged = onAboutChanged,
                    value = state.about,
                    placeholder = "О себе"
                )
            }
            if (state.isSpecialistRegistrationScreen) {
                VSpace(h = 28.dp)
                PortfolioCarousel(
                    defaultList = state.portfolioUris,
                    onListChanged = onPortfolioChanged,
                )
                VSpace(h = 16.dp)
            }
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                VSpace(h = 12.dp)
                LensaInput(
                    modifier = Modifier.fillMaxWidth(),
                    onValueChanged = onPersonalWebsiteChanged,
                    value = state.personalSite,
                    placeholder = "Сайт"
                )
                VSpace(h = 12.dp)
                LensaInput(
                    readOnly = true,
                    showTrailingIcon = true,
                    trailingIconRes = R.drawable.ic_plus,
                    onTrailingIconClick = {
                        showSocialMediaDialog = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    onValueChanged = {},
                    value = "",
                    placeholder = "Социальная сеть"
                )
                if (state.isSpecialistRegistrationScreen) {
                    VSpace(h = 12.dp)
                    LensaInput(
                        readOnly = true,
                        showTrailingIcon = true,
                        trailingIconRes = R.drawable.ic_plus,
                        onTrailingIconClick = {
                            showPriceListDialog = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        onValueChanged = {},
                        value = "",
                        placeholder = "Прайс"
                    )
                }
                VSpace(h = 60.dp)
                if (state.validationErrors.isNotEmpty()) {
                    Text(
                        text = state.validationErrors.values.first(),
                        style = LensaTheme.typography.signature,
                        color = LensaTheme.colors.textColorSecondary,
                    )
                    VSpace(h = 20.dp)
                }
                LensaButton(
                    enabled = state.isButtonNextEnabled,
                    text = "СОХРАНИТЬ",
                    isFillMaxWidth = true,
                    onClick = {
                        onSaveClick()
                    },
                )
                VSpace(h = 20.dp)
                LensaTextButton(
                    text = "ОТМЕНИТЬ",
                    type = LensaTextButtonType.DEFAULT,
                    isFillMaxWidth = true,
                    onClick = onDismissClick,
                )
                VSpace(h = 20.dp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    LensaTheme {
        RegistrationContent(
            state = RegistrationScreenState.INITIAL,
            onNameChanged = {},
            onSurnameChanged = {},
            onAboutChanged = {},
            onCityChanged = {},
            onCountryChanged = {},
            onEmailChanged = {},
            onPhoneNumberChanged = {},
            onSpecializationChanged = {},
            onPersonalWebsiteChanged = {},
            onGetInTouchClick = {},
            onAvatarChanged = {},
            onPortfolioChanged = {},
            onPricesListChanged = {},
            onSocialMediasChanged = {},
            onSaveClick = {},
            onDismissClick = {},
        )
    }
}