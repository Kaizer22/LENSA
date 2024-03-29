package ru.arinae_va.lensa.presentation.feature.favourite.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.LensaAsyncImage
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.theme.LensaTheme

internal const val FOLDER_PREVIEW_ROWS_COUNT = 2
internal const val FOLDER_PREVIEW_COLUMNS_COUNT = 2
internal const val FAVOURITES_FOLDER_PREVIEW_PICTURES_COUNT =
    FOLDER_PREVIEW_COLUMNS_COUNT * FOLDER_PREVIEW_ROWS_COUNT
@Composable
fun FavouritesFolderItem(
    name: String,
    picturesUrls: List<String>,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.clickable(
            onClick = onClick
        )
    ) {
        repeat(FOLDER_PREVIEW_ROWS_COUNT) { i ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = LensaTheme.colors.textColor),
            ) {
                repeat(FOLDER_PREVIEW_COLUMNS_COUNT) { j ->
                    if (i * 2 + j < picturesUrls.size && picturesUrls[i * 2 + j].isNotEmpty()) {
                        LensaAsyncImage(
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(0.7.dp),
                            aspectRatio = 1f,
                            pictureUrl = picturesUrls[i * 2 + j]
                        )
                    } else {
                        Image(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .weight(0.5f)
                                .padding(0.7.dp),
                            painter = painterResource(id = R.drawable.shimmer_fill),
                            contentDescription = null,

                        )
                    }
                }
            }
        }
        VSpace(h = 12.dp)
        Text(
            text = name,
            style = LensaTheme.typography.textButton,
            color = LensaTheme.colors.textColor,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FavouritesFolderItemPreview() {
    LensaTheme {
        FavouritesFolderItem(
            name = "Test",
            picturesUrls = listOf(
                "https://f.vividscreen.info/soft/897d37db01f775424751e91a32542e72/Emerald-Lake-Carcross-Yukon-wide-l.jpg",
                "https://f.vividscreen.info/soft/897d37db01f775424751e91a32542e72/Emerald-Lake-Carcross-Yukon-wide-l.jpg",
                "https://f.vividscreen.info/soft/897d37db01f775424751e91a32542e72/Emerald-Lake-Carcross-Yukon-wide-l.jpg",
                
            ),
            onClick = {},
        )
    }
}