package ru.arinae_va.lensa.presentation.feature.feed.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaRating

import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun SpecialistCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    photoUrl: String?,
    rating: Float,
    text: String,
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            model = photoUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Row(modifier = Modifier.padding(top = 12.dp)) {
            Text(
                text = text,
                style = LensaTheme.typography.textButton,
                color = LensaTheme.colors.textColor,
            )
            FSpace()
            HSpace(w = 8.dp)
            LensaRating(
                rating = rating,
            )
        }
    }
}