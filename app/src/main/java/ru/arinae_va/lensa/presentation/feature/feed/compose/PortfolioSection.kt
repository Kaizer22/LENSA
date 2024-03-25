package ru.arinae_va.lensa.presentation.feature.feed.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import ru.arinae_va.lensa.presentation.common.component.LensaAsyncImage

@Composable
fun PortfolioSection(
    portfolioUrls: List<String>,
) {
    Column {
        if (portfolioUrls.isNotEmpty()) {
            repeat(portfolioUrls.size / 2 + 1) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (it * 2 < portfolioUrls.size) {
                        LensaAsyncImage(
                            modifier = Modifier
                                .weight(0.5f),
                            pictureUrl = portfolioUrls[it * 2],
                        )
                    }
                    if (it * 2 + 1 < portfolioUrls.size) {
                        LensaAsyncImage(
                            modifier = Modifier
                                .weight(0.5f),
                            pictureUrl = portfolioUrls[it * 2 + 1],
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}