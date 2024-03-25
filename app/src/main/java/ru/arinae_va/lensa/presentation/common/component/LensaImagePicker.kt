package ru.arinae_va.lensa.presentation.common.component

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun LensaImagePicker(
    modifier: Modifier = Modifier,
    defaultLink: String? = null,
    onImagePicked: (Uri) -> Unit,
    onCancelButtonClick: () -> Unit = {},
    isCancelIconVisible: Boolean = false,
    emptyStateButtonSize: Dp = 40.dp,
) {
    val context = LocalContext.current
    var imageData by remember { mutableStateOf<Uri?>(null) }

    var isEmpty by remember(imageData) {
        mutableStateOf((defaultLink == null && imageData == null))
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            it?.let {
                imageData = it
                onImagePicked(it)
                isEmpty = false
            }
        }
    )
    val emptyModifier = modifier
        .border(
            width = 1.dp,
            color = LensaTheme.colors.textColor,
            shape = LensaTheme.shapes.noRoundedCornersShape,
        )

    Box(
        modifier = (if (isEmpty) emptyModifier else modifier).clickable {
            launcher.launch(
                "image/*"
            )
        },
        contentAlignment = Alignment.Center,
    ) {
        if (isEmpty) {
            Box(
                modifier = Modifier
                    .size(emptyStateButtonSize)
                    .background(
                        color = LensaTheme.colors.textColorAccent,
                        shape = LensaTheme.shapes.roundShape,
                    )
                    .border(
                        width = 1.dp,
                        color = LensaTheme.colors.textColor,
                        shape = LensaTheme.shapes.roundShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.size(emptyStateButtonSize / 2),
                    painter = painterResource(id = R.drawable.il_plus),
                    contentDescription = null,
                    tint = LensaTheme.colors.textColor,
                )
            }
        } else {
            if (imageData != null) {
                val bitmap = remember { mutableStateOf<Bitmap?>(null) }
                val uri = imageData
                if (uri != null) {
                    val source = ImageDecoder
                        .createSource(context.contentResolver, uri)
                    bitmap.value = ImageDecoder.decodeBitmap(source)

                    bitmap.value?.let { btm ->
                        Image(
                            bitmap = btm.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            } else {
                LensaAsyncImage(
                    pictureUrl = defaultLink.orEmpty(),
                )
            }

            if (isCancelIconVisible) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    LensaIconButton(
                        onClick = {
                            onCancelButtonClick.invoke()
                            imageData = null
                        },
                        icon = R.drawable.ic_cancel,
                        iconSize = 16.dp,
                    )
                }
            }
        }
    }
}