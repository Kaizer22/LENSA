package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import com.skydoves.cloudy.Cloudy

@Composable
fun LensaBackgroundBlur(
    isBlurry: Boolean,
    content: @Composable () -> Unit,
) {
    Box {
       if (isBlurry) {
           Cloudy {
               content.invoke()
           }
       } else {
           content.invoke()
       }
    }

}