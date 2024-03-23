package ru.arinae_va.lensa.presentation.feature.feed.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun PortfolioSection(
    portfolioUrls: List<String>,
) {
    Column {
        if (portfolioUrls.isNotEmpty()) {
            repeat(portfolioUrls.size / 2 + 1) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (it * 2 < portfolioUrls.size) {
                        AsyncImage(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .weight(0.5f),
                            model = portfolioUrls[it * 2],
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                    if (it * 2 + 1 < portfolioUrls.size) {
                        AsyncImage(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .weight(0.5f),
                            model = portfolioUrls[it * 2 + 1],
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}