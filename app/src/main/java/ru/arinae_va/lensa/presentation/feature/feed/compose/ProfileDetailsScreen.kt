package ru.arinae_va.lensa.presentation.feature.feed.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.domain.model.Price
import ru.arinae_va.lensa.domain.model.PriceCurrency
import ru.arinae_va.lensa.domain.model.Review
import ru.arinae_va.lensa.domain.model.SocialMedia
import ru.arinae_va.lensa.domain.model.UserProfileModel
import ru.arinae_va.lensa.domain.model.UserProfileType
import ru.arinae_va.lensa.presentation.common.component.ExpandableButton
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LabeledField
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.feed.viewmodel.ProfileDetailsState
import ru.arinae_va.lensa.presentation.feature.feed.viewmodel.ProfileDetailsViewModel
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import java.time.LocalDateTime

enum class SocialMediaType(
    @DrawableRes val icon: Int
) {
    INSTAGRAM(icon = R.drawable.ic_instagram),
    TELEGRAM(icon = R.drawable.ic_telegram),
    VK(icon = R.drawable.ic_vk),
    WHATSAPP(icon = R.drawable.ic_whatsapp),
    PINTEREST(icon = R.drawable.ic_pinterest),
    YOUTUBE(icon = R.drawable.ic_youtube),
    BEHANCE(icon = R.drawable.ic_behance),
}

@Composable
fun ProfileDetailsScreen(
    navController: NavController,
    viewModel: ProfileDetailsViewModel,
) {
    setSystemUiColor()
    val state by viewModel.state.collectAsState()
    ProfileDetailsContent(
        state = state,
        onFavouritesClick = {
            navController.navigate(LensaScreens.FAVOURITES_SCREEN.name)
        },
        onAddToFavouritesClick = {},
        onSettingsClick = {
            navController.navigate(LensaScreens.SETTINGS_SCREEN.name)
        },
        onBackPressed = {
            navController.popBackStack()
        },
        onRatingChanged = viewModel::onRatingChanged,
        onReviewAvatarClick = viewModel::onReviewAvatarClicked,
        onReviewChanged = viewModel::onReviewChanged,
        onPostReview = viewModel::onPostReview,
    )
}

@Composable
private fun ProfileDetailsContent(
    state: ProfileDetailsState,
    onBackPressed: () -> Unit,
    onAddToFavouritesClick: () -> Unit,
    onFavouritesClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onReviewChanged: (String) -> Unit,
    onRatingChanged: (Float) -> Unit,
    onReviewAvatarClick: (String) -> Unit,
    onPostReview: () -> Unit,
) {
    val isCustomer = remember(state) {
        state.userProfileModel.type == UserProfileType.CUSTOMER
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = LensaTheme.colors.backgroundColor),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            VSpace(h = 30.dp)
            HeaderSection(
                state = state,
                onAddToFavouritesClick = onAddToFavouritesClick,
                onFavouritesClick = onFavouritesClick,
                onSettingsClick = onSettingsClick,
                onBackPressed = onBackPressed,
            )
            VSpace(h = 16.dp)
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                model = state.userProfileModel.avatarUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            VSpace(h = 24.dp)
            PersonalInfoSection(model = state.userProfileModel)
            VSpace(h = 24.dp)
            Text(
                text = state.userProfileModel.about,
                style = LensaTheme.typography.text,
                // TODO добавить цвета текста в style
                color = LensaTheme.colors.textColor,
            )
            VSpace(h = 24.dp)
            if (!isCustomer) {
                PriceSection(prices = state.userProfileModel.prices)
                VSpace(h = 24.dp)
                PortfolioSection(portfolioUrls = state.userProfileModel.portfolioUrls ?: listOf())
                VSpace(h = 24.dp)
                Divider(color = LensaTheme.colors.dividerColor)
                VSpace(h = 24.dp)
                if (!state.isSelf && state.userProfileModel.type == UserProfileType.SPECIALIST) {
                    AddReviewSection(
                        state = state,
                        onRatingChanged = onRatingChanged,
                        onReviewChanged = onReviewChanged,
                        onPostReview = onPostReview,
                    )
                    VSpace(h = 24.dp)
                    Divider(color = LensaTheme.colors.dividerColor)
                    VSpace(h = 24.dp)
                }
                ReviewsSection(
                    state = state,
                    onUserAvatarClick = onReviewAvatarClick,
                )
            }
        }
    }
}

@Composable
fun PersonalInfoSection(
    model: UserProfileModel,
) {
    SpecialistDetailsField(label = "Страна", text = model.country)
    SpecialistDetailsField(label = "Город", text = model.city)
    SpecialistDetailsField(label = "Сайт", text = model.personalSite)
    SpecialistDetailsField(label = "Почта", text = model.email)
    VSpace(h = 12.dp)
    Row {
        model.socialMedias.forEach {
            Icon(
                painter = painterResource(id = it.type.icon),
                contentDescription = null,
                tint = LensaTheme.colors.textColor,
            )
            HSpace(w = 20.dp)
        }
    }
}

@Composable
fun PriceSection(
    prices: List<Price>,
) {
    Text(
        text = "ПРАЙС",
        style = LensaTheme.typography.header2,
        color = LensaTheme.colors.textColor,
    )
    VSpace(h = 12.dp)
    prices.forEach {
        ExpandableButton(
            text = it.name,
            isFillMaxWidth = true,
        ) {
            // TODO форматирование стоимости
            Text(
                text = "\n${it.text}\nСтоимость: ${it.price}${it.currency.symbol}\n",
                style = LensaTheme.typography.text,
                color = LensaTheme.colors.textColor,
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SpecialistDetailsScreenPreview() {
    LensaTheme {
        ProfileDetailsContent(
            state = ProfileDetailsState(
                userProfileModel = UserProfileModel(
                    id = "",
                    type = UserProfileType.SPECIALIST,
                    name = "Арина",
                    surname = "Еремеева",
                    specialization = "Фотограф",
                    rating = 4.9f,
                    avatarUrl = "",
                    country = "Россия",
                    city = "Санкт-Петербург",
                    personalSite = "",
                    email = "",
                    socialMedias = listOf(
                        SocialMedia(
                            link = "",
                            type = SocialMediaType.INSTAGRAM,
                        ),
                        SocialMedia(
                            link = "",
                            type = SocialMediaType.TELEGRAM,
                        ),
                    ),
                    about = "",
                    portfolioUrls = listOf(
                        "", "",
                    ),
                    prices = listOf(
                        Price(
                            name = "BASIC",
                            text = "1 час съемки\n" +
                                    "1-2 образа\n" +
                                    "5 фотографий в ретуши по вашему выбору\n" +
                                    "150+ кадров без ретуши в профессиональной обработке\n" +
                                    "Срок обработки до 3-х недель\n" +
                                    "Помощь в создании образов, подготовка референсов\n" +
                                    "Подбор и бронирование студии*",
                            price = 8000,
                            currency = PriceCurrency.RUB,
                        ),
                        Price(
                            name = "STANDARD",
                            text = "1 час съемки\n" +
                                    "1-2 образа\n" +
                                    "5 фотографий в ретуши по вашему выбору\n" +
                                    "150+ кадров без ретуши в профессиональной обработке\n" +
                                    "Срок обработки до 3-х недель\n" +
                                    "Помощь в создании образов, подготовка референсов\n" +
                                    "Подбор и бронирование студии*",
                            price = 8000,
                            currency = PriceCurrency.RUB,
                        )
                    ),
                    reviews = listOf(
                        Review(
                            authorId = "",
                            name = "Test",
                            surname = "Test",
                            avatarUrl = "",
                            dateTime = LocalDateTime.now(),
                            rating = 4f,
                            text = "review",
                        )
                    )
                ),
                reviewText = "",
                rating = 0f,
                isSelf = false,
            ),
            onSettingsClick = {},
            onFavouritesClick = {},
            onAddToFavouritesClick = {},
            onBackPressed = {},
            onReviewChanged = {},
            onReviewAvatarClick = {},
            onRatingChanged = {},
            onPostReview = {},
        )
    }
}
