package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.ratingbar.RatingBar
import com.smarttoolfactory.ratingbar.model.RateChangeStrategy
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.theme.LensaTheme

internal const val MAX_RATING = 5
@Composable
fun LensaRatingBar(
    rating: Float,
    maxRating: Int = MAX_RATING,
    onRatingChanged: (Float) -> Unit,
) {
    RatingBar(
        rating = rating,
        painterEmpty = painterResource(id = R.drawable.ic_star_5_outlined),
        painterFilled = painterResource(id = R.drawable.ic_5_star),
        tintEmpty = LensaTheme.colors.textColor,
        tintFilled = LensaTheme.colors.textColor,
        itemCount = maxRating,
        rateChangeStrategy = RateChangeStrategy.InstantChange,
        onRatingChange = onRatingChanged,
        itemSize = 24.dp,
        space = 4.dp,
    )
}