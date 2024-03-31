package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun LensaZoomableContainer(
    content: @Composable (scale: Float, offsetX: Float, offsetY: Float) -> Unit,
) {
//https://www.geeksforgeeks.org/android-jetpack-compose-implement-zoomable-view/
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        awaitFirstDown()
                        do {
                            val event = awaitPointerEvent()
                            scale *= event.calculateZoom()
                            val offset = event.calculatePan()
                            offsetX += offset.x
                            offsetY += offset.y
                        } while (event.changes.any { it.pressed })
                    }
                }
            }
    ) {
        content.invoke(scale, offsetX, offsetY)
    }
}

@Composable
fun LensaZoomablePreview(
    shownImageIndex: Int = 0,
    imageUrls: List<String>,
    onCloseClick: () -> Unit,
) {
    val context = LocalContext.current
    var imageIndex by remember(shownImageIndex) { mutableStateOf(shownImageIndex) }
    LensaZoomableContainer { scale, offsetX, offsetY ->
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = LensaTheme.colors.fadeColor)
            ) {}
            LensaAsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    ),
                aspectRatio = null,
                pictureUrl = imageUrls[imageIndex],
                contentScale = ContentScale.Fit,
            )
            Column {
                FSpace()
                Row {
                    if (imageIndex - 1 >= 0) {
                        LensaIconButton(
                            onClick = { imageIndex-- },
                            icon = R.drawable.ic_arrow_back,
                            iconSize = 24.dp,
                        )
                    }
                    FSpace()
                    if (imageIndex + 1 < imageUrls.size) {
                        LensaIconButton(
                            onClick = { imageIndex++ },
                            icon = R.drawable.ic_arrow_forward,
                            iconSize = 24.dp,
                        )
                    }
                }
                FSpace()
                LensaButton(
                    text = "ЗАКРЫТЬ",
                    isFillMaxWidth = true,
                    onClick = onCloseClick,
                )
            }
        }
    }
}