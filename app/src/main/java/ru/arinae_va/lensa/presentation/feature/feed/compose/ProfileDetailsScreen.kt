package ru.arinae_va.lensa.presentation.feature.feed.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.domain.model.Price
import ru.arinae_va.lensa.domain.model.PriceCurrency
import ru.arinae_va.lensa.domain.model.Review
import ru.arinae_va.lensa.domain.model.SocialMedia
import ru.arinae_va.lensa.domain.model.UserProfileModel
import ru.arinae_va.lensa.domain.model.UserProfileType
import ru.arinae_va.lensa.presentation.common.component.LensaAsyncImage
import ru.arinae_va.lensa.presentation.common.component.LensaReplaceLoader
import ru.arinae_va.lensa.presentation.common.component.LensaZoomablePreview
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
        onChatsClick = viewModel::onChatsClick,
        onSendMessageClick = viewModel::onSendMessageClick,
        onAddToFavouritesClick = viewModel::onAddToFavouritesClick,
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
    onAddToFavouritesClick: (Boolean) -> Unit,
    onFavouritesClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onChatsClick: () -> Unit,
    onSendMessageClick: (String) -> Unit,
    onReviewChanged: (String) -> Unit,
    onRatingChanged: (Float) -> Unit,
    onReviewAvatarClick: (String) -> Unit,
    onPostReview: () -> Unit,
) {
    val isCustomer = remember(state) {
        state.userProfileModel.type == UserProfileType.CUSTOMER
    }

    var isShowImagePreview by remember{ mutableStateOf(false) }
    var previewImages by remember { mutableStateOf(
        listOf(state.userProfileModel.avatarUrl.orEmpty())
    )}
    var shownImageIndex by remember { mutableStateOf(0) }
    LensaReplaceLoader(
        isLoading = state.isLoading,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
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
                        onChatsClick = onChatsClick,
                        onBackPressed = onBackPressed,
                    )
                    VSpace(h = 16.dp)
                    LensaAsyncImage(
                        onClick = {
                            previewImages = listOf(state.userProfileModel.avatarUrl.orEmpty())
                            shownImageIndex = 0
                            isShowImagePreview = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        pictureUrl = state.userProfileModel.avatarUrl.orEmpty(),
                    )
                    VSpace(h = 24.dp)
                    PersonalInfoSection(
                        state = state,
                        onSendMessageClick = onSendMessageClick,
                    )
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
                        PortfolioSection(
                            portfolioUrls = state.userProfileModel.portfolioUrls ?: emptyList(),
                            onImageClick = { portfolioImageIndex ->
                                previewImages = state.userProfileModel.portfolioUrls ?: emptyList()
                                shownImageIndex = portfolioImageIndex
                                isShowImagePreview = true
                            }
                        )
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
            // TODO комопонент-обертка
            if (isShowImagePreview && previewImages.isNotEmpty()) {
                LensaZoomablePreview(
                    imageUrls = previewImages,
                    shownImageIndex = shownImageIndex,
                    onCloseClick = { isShowImagePreview = false }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SpecialistDetailsScreenPreview() {
    LensaTheme {
        ProfileDetailsContent(
            state = ProfileDetailsState(
                userProfileModel = UserProfileModel(
                    userId = "",
                    profileId = "",
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
                    ),
                    phoneNumber = "",
                ),
                reviewText = "",
                rating = 0f,
                isSelf = false,
                isLoading = false,
                isAddedToFavourites = false,
            ),
            onSettingsClick = {},
            onFavouritesClick = {},
            onChatsClick = {},
            onAddToFavouritesClick = {},
            onBackPressed = {},
            onReviewChanged = {},
            onReviewAvatarClick = {},
            onRatingChanged = {},
            onPostReview = {},
            onSendMessageClick = {},
        )
    }
}
