package ru.arinae_va.lensa.presentation.feature.feed.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import org.jetbrains.annotations.Async
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.ExpandableButton
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LabeledField
import ru.arinae_va.lensa.presentation.common.component.LensaIconButton
import ru.arinae_va.lensa.presentation.common.component.LensaRating
import ru.arinae_va.lensa.presentation.common.component.LensaStateButton
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import java.time.LocalDateTime

@Composable
fun SpecialistDetailsScreen(
    navController: NavController,
    viewModel: SpecialistDetailsViewModel,
) {
    setSystemUiColor()
    Screen(
        isSelf = true,
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
        model = SpecialistModel(
            name = "Арина",
            surname = "Еремеева",
            specialization = "Фотограф",
            rating = 4.9f,
            avatarUrl = "https://pixelbox.ru/wp-content/uploads/2021/11/black-white-avatars-steam-pixelbox.ru-27.jpg",
            country = "Россия",
            city = "Санкт-Петербург",
            personalSite = "tttttttttt",
            email = "tttttttttttt",
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
            about = "Фотограф со стажем 2 года. Фотографирую портреты и творческие съемки. " +
                    "Записаться можно на моем сайте по ссылке",
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
                    price = 8000.0,
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
                    price = 8000.0,
                    currency = PriceCurrency.RUB,
                )
            ),
            reviews = listOf(
                Review(
                    name = "Test",
                    surname = "Test",
                    avatarUrl = "",
                    dateTime = LocalDateTime.now(),
                )
            )
        )
    )
}

data class SpecialistModel(
    val name: String,
    val surname: String,
    val specialization: String,
    val rating: Float,
    val avatarUrl: String,
    val country: String,
    val city: String,
    val personalSite: String,
    val email: String,
    val socialMedias: List<SocialMedia>,
    val about: String,
    val portfolioUrls: List<String>,
    val prices: List<Price>,
    val reviews: List<Review>,
)

data class Review(
    val name: String,
    val surname: String,
    val avatarUrl: String,
    val dateTime: LocalDateTime,
)

data class Price(
    val name: String,
    val text: String,
    val price: Double,
    val currency: PriceCurrency,
)

enum class PriceCurrency(
    val symbol: String,
) {
    RUB(symbol = "₽")
}

data class SocialMedia(
    val link: String,
    val type: SocialMediaType,
)

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
private fun Screen(
    onBackPressed: () -> Unit,
    onAddToFavouritesClick: () -> Unit,
    onFavouritesClick: () -> Unit,
    onSettingsClick: () -> Unit,
    isSelf: Boolean,
    model: SpecialistModel,
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
                isSelf = isSelf,
                onAddToFavouritesClick = onAddToFavouritesClick,
                onFavouritesClick = onFavouritesClick,
                onSettingsClick = onSettingsClick,
                onBackPressed = onBackPressed,
                model = model,
            )
            VSpace(h = 16.dp)
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                model = model.avatarUrl,
                contentDescription = null,
            )
            VSpace(h = 24.dp)
            PersonalInfoSection(model = model)
            VSpace(h = 24.dp)
            Text(
                text = model.about,
                style = LensaTheme.typography.text,
                // TODO добавить цвета текста в style
                color = LensaTheme.colors.textColor,
            )
            VSpace(h = 24.dp)
            PriceSection(prices = model.prices)
            VSpace(h = 24.dp)
            PortfolioSection()
            VSpace(h = 24.dp)
            Divider(color = LensaTheme.colors.dividerColor)
            VSpace(h = 24.dp)
            AddReviewSection()
            VSpace(h = 24.dp)
            Divider(color = LensaTheme.colors.dividerColor)
            VSpace(h = 24.dp)
            ReviewsSection()
        }
    }
}

@Composable
fun ReviewsSection() {

}

@Composable
fun AddReviewSection() {

}

@Composable
fun PortfolioSection() {

}

@Composable
fun HeaderSection(
    onBackPressed: () -> Unit,
    onFavouritesClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAddToFavouritesClick: () -> Unit,
    isSelf: Boolean,
    model: SpecialistModel,
) {
    Row {
        LensaIconButton(
            onClick = onBackPressed,
            icon = R.drawable.ic_arrow_back,
            iconSize = 30.dp,
        )
        FSpace()
        if (isSelf) {
            LensaIconButton(
                onClick = onFavouritesClick,
                icon = R.drawable.ic_heart_outlined,
                iconSize = 28.dp
            )
            HSpace(w = 16.dp)
            LensaIconButton(
                onClick = onSettingsClick,
                icon = R.drawable.ic_settings,
                iconSize = 28.dp
            )
        } else {
            LensaStateButton(
                onClick = {
                    onAddToFavouritesClick()
                },
                iconEnabledRes = R.drawable.ic_heart_filled,
                iconDisabledRes = R.drawable.ic_heart_outlined
            )
            HSpace(w = 16.dp)
        }
    }
    VSpace(h = 24.dp)
    Text(
        text = "${model.surname.toUpperCase()}\n${model.name.toUpperCase()}",
        style = LensaTheme.typography.header2,
        color = LensaTheme.colors.textColor,
    )
    VSpace(h = 4.dp)
    Row {
        Text(
            text = model.specialization,
            style = LensaTheme.typography.header3,
            color = LensaTheme.colors.textColor,
        )
        FSpace()
        LensaRating(rating = model.rating)
    }
}

@Composable
fun PersonalInfoSection(
    model: SpecialistModel,
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
        Screen(
            isSelf = true,
            onSettingsClick = {},
            onFavouritesClick = {},
            onAddToFavouritesClick = {},
            onBackPressed = {},
            model = SpecialistModel(
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
                        price = 8000.0,
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
                        price = 8000.0,
                        currency = PriceCurrency.RUB,
                    )
                ),
                reviews = listOf(
                    Review(
                        name = "Test",
                        surname = "Test",
                        avatarUrl = "",
                        dateTime = LocalDateTime.now(),
                    )
                )
            )
        )
    }
}
