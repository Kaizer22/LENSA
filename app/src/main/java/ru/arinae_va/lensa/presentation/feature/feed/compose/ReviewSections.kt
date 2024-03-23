package ru.arinae_va.lensa.presentation.feature.feed.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.arinae_va.lensa.domain.model.Review
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaMultilineInput
import ru.arinae_va.lensa.presentation.common.component.LensaRating
import ru.arinae_va.lensa.presentation.common.component.LensaRatingBar
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.feature.feed.viewmodel.ProfileDetailsState
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun ReviewsSection(
    state: ProfileDetailsState,
    onUserAvatarClick: (String) -> Unit,
) {
    Column {
        Text(
            text = "ОТЗЫВЫ",
            style = LensaTheme.typography.header2,
            color = LensaTheme.colors.textColor,
        )
        VSpace(h = 24.dp)
        state.userProfileModel.reviews?.let { reviews ->
            if (reviews.isNotEmpty()) {
                reviews.forEach { review ->
                    ReviewItem(
                        model = review,
                        onAvatarClick = onUserAvatarClick,
                    )
                    VSpace(h = 24.dp)
                }
            } else {
                EmptyReviews()
            }
        } ?: run {
            EmptyReviews()
        }
    }
}

@Composable
fun EmptyReviews() {
    Text(
        text = "Тут пока ничего нет...",
        style = LensaTheme.typography.text,
        color = LensaTheme.colors.textColor,
    )
}

@Composable
fun ReviewItem(
    model: Review,
    onAvatarClick: (userId: String) -> Unit,
) {
    Row {
        AsyncImage(
            modifier = Modifier
                .background(
                    color = LensaTheme.colors.fadeColor,
                    shape = LensaTheme.shapes.roundShape,
                )
                .size(44.dp)
                .clickable { onAvatarClick.invoke(model.authorId) },
            model = model.avatarUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        HSpace(w = 12.dp)
        Column {
            Row {
                Text(
                    text = "${model.name} ${model.surname}",
                    style = LensaTheme.typography.name,
                    color = LensaTheme.colors.textColor,
                )
                FSpace()
                LensaRating(rating = model.rating)
            }
            VSpace(h = 4.dp)
            Text(
                text = model.text,
                style = LensaTheme.typography.text,
                color = LensaTheme.colors.textColor,
            )
            VSpace(h = 8.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Text(
                    text = model.dateTime.toString(),
                    style = LensaTheme.typography.signature,
                    color = LensaTheme.colors.textColorSecondary,
                )
            }
        }

    }
}

@Composable
fun AddReviewSection(
    state: ProfileDetailsState,
    onRatingChanged: (Float) -> Unit,
    onReviewChanged: (String) -> Unit,
    onPostReview: () -> Unit,
) {
    Text(
        text = "ОЦЕНИТЬ",
        style = LensaTheme.typography.header2,
        color = LensaTheme.colors.textColor,
    )
    VSpace(h = 12.dp)
    LensaRatingBar(
        rating = state.rating,
        onRatingChanged = onRatingChanged,
    )
    VSpace(h = 24.dp)
    LensaMultilineInput(
        onValueChanged = onReviewChanged,
        value = state.reviewText,
        placeholder = "Отзыв",
    )
    VSpace(h = 12.dp)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        LensaTextButton(
            text = "ОПУБЛИКОВАТь",
            onClick = onPostReview,
            type = LensaTextButtonType.DEFAULT,
        )
    }
}