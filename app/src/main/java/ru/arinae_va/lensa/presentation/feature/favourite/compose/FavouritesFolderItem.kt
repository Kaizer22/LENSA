package ru.arinae_va.lensa.presentation.feature.favourite.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.presentation.common.component.LensaAsyncImage
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun FavouritesFolderItem(
    name: String,
    picturesUrls: List<String>,
) {
    Column {
        repeat(picturesUrls.size / 2 + 1) {
            Row(modifier = Modifier.fillMaxWidth()) {
                if (it * 2 < picturesUrls.size) {
                    LensaAsyncImage(
                        modifier = Modifier.weight(0.5f),
                        aspectRatio = 1f,
                        pictureUrl = picturesUrls[it * 2]
                    )
                }
                if (it * 2 + 1 < picturesUrls.size) {
                    LensaAsyncImage(
                        modifier = Modifier.weight(0.5f),
                        aspectRatio = 1f,
                        pictureUrl = picturesUrls[it * 2 + 1],
                    )
                }
            }
        }
        VSpace(h = 12.dp)
        Text (
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
            )
        )
    }
}