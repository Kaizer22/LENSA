package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.arinae_va.lensa.R

@Composable
fun LensaAsyncImage(
    modifier: Modifier = Modifier,
    aspectRatio: Float = 1f,
    pictureUrl: String = "",
    contentScale: ContentScale = ContentScale.Crop,
) {
    AsyncImage(
        modifier = modifier.aspectRatio(aspectRatio),
        model = ImageRequest.Builder(LocalContext.current)
            .data(pictureUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = contentScale,
        placeholder = painterResource(id = R.drawable.shimmer_fill),
        error = painterResource(id = R.drawable.ic_cancel),
    )
}