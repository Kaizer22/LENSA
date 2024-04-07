package ru.arinae_va.lensa.presentation.feature.feed.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.domain.model.Review
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaAsyncImage
import ru.arinae_va.lensa.presentation.common.component.LensaMultilineInput
import ru.arinae_va.lensa.presentation.common.component.LensaRating
import ru.arinae_va.lensa.presentation.common.component.LensaRatingBar
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.feature.feed.viewmodel.ProfileDetailsState
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import ru.arinae_va.lensa.utils.datetimeFormatter

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
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(shape = LensaTheme.shapes.roundShape)
                .clickable { onAvatarClick.invoke(model.authorId) },
        ) {
            LensaAsyncImage(
                pictureUrl = model.avatarUrl,
            )
        }
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
                    text = model.dateTime.format(datetimeFormatter),
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
            text = "ОПУБЛИКОВАТЬ",
            onClick = onPostReview,
            type = LensaTextButtonType.DEFAULT,
        )
    }
}