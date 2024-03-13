package ru.arinae_va.lensa.presentation.feature.registration.compose

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.registration.compose.dialog.PriceListCreatorDialog
import ru.arinae_va.lensa.presentation.feature.registration.compose.dialog.SocialMediaCreatorDialog
import ru.arinae_va.lensa.presentation.feature.registration.compose.dialog.defaultMediasMap
import ru.arinae_va.lensa.presentation.feature.registration.viewmodel.RegistrationScreenState
import ru.arinae_va.lensa.presentation.feature.registration.viewmodel.RegistrationViewModel
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel,
) {
    setSystemUiColor()
    val state by viewModel.state.collectAsState()
    Screen(
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
        onSaveClick = viewModel::onSaveClick,
    )
}

@Composable
private fun Screen(
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
    onSaveClick: () -> Unit,
    onGetInTouchClick: () -> Unit,
) {
    var socialMedia by remember { mutableStateOf(defaultMediasMap) }
    var priceList by remember { mutableStateOf(listOf<Price>()) }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var portfolioUris by remember { mutableStateOf<List<Uri>>(listOf()) }
    var portfolioUrls by remember { mutableStateOf<List<String>>(listOf()) }

    var showPriceListDialog by remember { mutableStateOf(false) }
    if (showPriceListDialog) {
        PriceListCreatorDialog(
            defaultPricesList = priceList,
            onSaveClick = { list ->
                priceList = list
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
            defaultMedias = socialMedia,
            onSaveClick = { socialMediasMap ->
                socialMedia = socialMediasMap
                showSocialMediaDialog = false
            },
            onDismissClick = {
                showSocialMediaDialog = false
            },
        )
    }

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
                    modifier = Modifier.size(216.dp),
                    onImagePicked = { uri ->
                        avatarUri = uri
                    },
                    emptyStateButtonSize = 48.dp,
                )
            }
            VSpace(h = 28.dp)
            Text(
                text = "Все заполненные поля будут видны другим пользователям",
                style = LensaTheme.typography.signature,
                color = LensaTheme.colors.textColorSecondary,
            )
            VSpace(h = 28.dp)
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
                    specializationValue = state.specialization,
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
                items = listOf(
                    "Россия", "Азербайджан", "Армения", "Грузия"
                ),
            )
            VSpace(h = 12.dp)
            LensaDropdownInput(
                allowFreeInput = true,
                showRequired = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = onCityChanged,
                placeholder = "Город",
                items = listOf("Санкт-Петербург", "Москва")
            )
            VSpace(h = 12.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = onPhoneNumberChanged,
                value = state.phoneNumber,
                inputType = KeyboardType.Number,
                placeholder = "Номер телефона"
            )
            VSpace(h = 12.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = onEmailChanged,
                value = state.email,
                placeholder = "Почта"
            )
            VSpace(h = 12.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = onAboutChanged,
                value = state.about,
                placeholder = "О себе"
            )
        }
        if (state.isSpecialistRegistrationScreen) {
            VSpace(h = 28.dp)
            PortfolioCarousel(
                onListChanged = {
                    portfolioUris = it
                }
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
            LensaButton(
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
                onClick = {}
            )
        }
    }
}

@Composable
fun SpecializationSection(
    specializationValue: String,
    onValueChanged: (String) -> Unit,
    onGetInTouchClick: () -> Unit,
) {
    LensaDropdownInput(
        allowFreeInput = false,
        showRequired = true,
        modifier = Modifier.fillMaxWidth(),
        onValueChanged = onValueChanged,
        items = listOf("Фотограф", "Модель", "Стилист"),
        placeholder = "Специализация",
    )
    VSpace(h = 12.dp)
    Text(
        text = "Не нашел свою специализацию?",
        style = LensaTheme.typography.signature,
        color = LensaTheme.colors.textColorSecondary,
    )
    LensaTextButton(
        text = "Свяжись с нами",
        type = LensaTextButtonType.ACCENT,
        onClick = onGetInTouchClick
    )
}

const val MAX_PORTFOLIO_SIZE = 10
@Composable
fun PortfolioCarousel(
    modifier: Modifier = Modifier,
    onListChanged: (List<Uri>) -> Unit,
) {
    var portfolioImages by remember {
        mutableStateOf<List<Uri?>>(List(MAX_PORTFOLIO_SIZE) { null })
    }
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
    ) {
        for (i in 0 until MAX_PORTFOLIO_SIZE) {
            HSpace(w = 4.dp)
            LensaImagePicker(
                modifier = Modifier.size(175.dp),
                isCancelIconVisible = true,
                onImagePicked = {
                    val buf = portfolioImages.toMutableList()
                    buf[i] = it
                    portfolioImages = buf
                    onListChanged(portfolioImages.filterNotNull())
                },
                onCancelButtonClick = {
                    val buf = portfolioImages.toMutableList()
                    buf[i] = null
                    portfolioImages = buf
                    onListChanged(portfolioImages.filterNotNull())
                }
            )
            HSpace(w = 4.dp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    LensaTheme {
        Screen(
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
            onSaveClick = {},
        )
    }
}