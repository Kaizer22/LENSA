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
import androidx.navigation.NavController
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.domain.model.Price
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaButton
import ru.arinae_va.lensa.presentation.common.component.LensaImagePicker
import ru.arinae_va.lensa.presentation.common.component.LensaInput
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.feed.compose.SocialMediaType
import ru.arinae_va.lensa.presentation.feature.registration.compose.dialog.PriceListCreatorDialog
import ru.arinae_va.lensa.presentation.feature.registration.compose.dialog.SocialMediaCreatorDialog
import ru.arinae_va.lensa.presentation.feature.registration.compose.dialog.defaultMediasMap
import ru.arinae_va.lensa.presentation.feature.registration.compose.model.RegistrationScreenData
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun RegistrationScreen(
    isSpecialist: Boolean,
    viewModel: RegistrationViewModel,
) {
    setSystemUiColor()
    Screen(
        isSpecialist = isSpecialist,
        onGetInTouchClick = viewModel::onGetInTouchClick,
        onSaveClick = viewModel::onSaveClick,
    )
}

@Composable
private fun Screen(
    isSpecialist: Boolean,
    onSaveClick: (RegistrationScreenData) -> Unit,
    onGetInTouchClick: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var specialization by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var about by remember { mutableStateOf("") }
    var site by remember { mutableStateOf("") }
    var socialMedia by remember { mutableStateOf(defaultMediasMap) }
    var priceList by remember { mutableStateOf(listOf<Price>()) }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var portfolioUris by remember { mutableStateOf<List<Uri>>(listOf()) }
    var portfolioUrls by remember { mutableStateOf<List<String>>(listOf()) }

    val isNextEnabled = validateRegistrationForm(
        isSpecialist = isSpecialist,
        isAvatarSelected = avatarUri != null,
        name = name,
        surname = surname,
        specialization = specialization,
        country = country,
        city = city,
        phoneNumber = phoneNumber,
        email = email,
        about = about,
        site = site,
        socialMedia = socialMedia,
        priceList = priceList,
    )
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
                showRequired = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = { surname = it },
                value = surname,
                placeholder = "Фамилия"
            )
            VSpace(h = 12.dp)
            LensaInput(
                showRequired = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = { name = it },
                value = name,
                placeholder = "Имя"
            )
            if (isSpecialist) {
                VSpace(h = 12.dp)
                SpecializationSection(
                    specializationValue = specialization,
                    onValueChanged = { specialization = it },
                    onGetInTouchClick = onGetInTouchClick,
                )
            }
            VSpace(h = 12.dp)
            LensaInput(
                showRequired = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = { country = it },
                value = country,
                placeholder = "Страна"
            )
            VSpace(h = 12.dp)
            LensaInput(
                showRequired = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = { city = it },
                value = city,
                placeholder = "Город"
            )
            VSpace(h = 12.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = { phoneNumber = it },
                value = phoneNumber,
                inputType = KeyboardType.Number,
                placeholder = "Номер телефона"
            )
            VSpace(h = 12.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = { email = it },
                value = email,
                placeholder = "Почта"
            )
            VSpace(h = 12.dp)
            LensaInput(
                modifier = Modifier.fillMaxWidth(),
                onValueChanged = { about = it },
                value = about,
                placeholder = "О себе"
            )
        }
        if (isSpecialist) {
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
                onValueChanged = { site = it },
                value = site,
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
            if (isSpecialist) {
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
                enabled = isNextEnabled,
                text = "СОХРАНИТЬ",
                isFillMaxWidth = true,
                onClick = {
                    avatarUri?.let {
                        val result = RegistrationScreenData(
                            name = name,
                            surname = surname,
                            specialization = specialization,
                            avatarUri = it,
                            country = country,
                            city = city,
                            personalSite = site,
                            email = email,
                            socialMedias = socialMedia,
                            about = about,
                            portfolioUris = portfolioUris,
                            prices = priceList,
                        )
                        onSaveClick(result)
                    }
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

fun validateRegistrationForm(
    isSpecialist: Boolean,
    isAvatarSelected: Boolean,
    name: String,
    surname: String,
    specialization: String,
    country: String,
    city: String,
    phoneNumber: String,
    email: String,
    about: String,
    site: String,
    socialMedia: Map<SocialMediaType, String>,
    priceList: List<Price>
): Boolean = name.isNotBlank() &&
        surname.isNotBlank() &&
        country.isNotBlank() &&
        city.isNotBlank() &&
        (isSpecialist && specialization.isNotBlank()) &&
        priceList.isNotEmpty() &&
        isAvatarSelected


@Composable
fun SpecializationSection(
    specializationValue: String,
    onValueChanged: (String) -> Unit,
    onGetInTouchClick: () -> Unit,
) {
    LensaInput(
        showRequired = true,
        modifier = Modifier.fillMaxWidth(),
        onValueChanged = onValueChanged,
        value = specializationValue,
        placeholder = "Специализация"
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistrationScreenPreview() {
    LensaTheme {
        Screen(
            isSpecialist = false,
            onSaveClick = {},
            onGetInTouchClick = {},
        )
    }
}