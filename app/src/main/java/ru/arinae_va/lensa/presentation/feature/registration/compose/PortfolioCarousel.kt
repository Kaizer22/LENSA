package ru.arinae_va.lensa.presentation.feature.registration.compose

import android.net.Uri
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaImagePicker
import ru.arinae_va.lensa.utils.ext.appendListToNullableN

const val MAX_PORTFOLIO_SIZE = 10

@Composable
fun PortfolioCarousel(
    modifier: Modifier = Modifier,
    defaultList: List<Uri>? = null,
    onListChanged: (List<Uri>) -> Unit,
) {
    var portfolioImages by remember(defaultList) {
        // TODO refactor to described logic
        mutableStateOf(
            defaultList?.appendListToNullableN(n = MAX_PORTFOLIO_SIZE, emptyValue = null)
                ?: List(MAX_PORTFOLIO_SIZE) { null }
        )
    }
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
    ) {
        for (i in 0 until MAX_PORTFOLIO_SIZE) {
            HSpace(w = 4.dp)
            LensaImagePicker(
                modifier = Modifier.size(175.dp),
                defaultLink = portfolioImages[i]?.toString(),
                isCancelIconVisible = true,
                onImagePicked = {
                    val buf = portfolioImages.toMutableList()
                    buf[i] = it
                    portfolioImages = buf
                    onListChanged(portfolioImages.filterNotNull())
                },
                onCancelButtonClick = {
                    val buf = portfolioImages.toMutableList()
                    buf[i] = null
                    portfolioImages = buf
                    onListChanged(portfolioImages.filterNotNull())
                }
            )
            HSpace(w = 4.dp)
        }
    }
}