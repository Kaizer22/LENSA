package ru.arinae_va.lensa.presentation.feature.onboarding.compose

import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import ru.arinae_va.lensa.presentation.common.component.LensaLogo
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun LensaSplashScreen(
    navController: NavController,
    viewModel: OnboardingViewModel,
) {
    val opacity = remember {
        androidx.compose.animation.core.Animatable(0f)
    }
    setSystemUiColor()

    // Анимация появления с изменением прозрачности
    LaunchedEffect(key1 = true) {
        opacity.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = {
                    LinearInterpolator().getInterpolation(it)
                })
        )
        delay(1000L)
        if (viewModel.isNeedOnboarding()) {
            navController.navigate(LensaScreens.ONBOARDING_SCREEN.name)
        } else {
            navController.navigate(LensaScreens.AUTH_SCREEN.name)
        }
    }
    Screen(
        modifier = Modifier.alpha(opacity.value)
    )
}

@Composable
private fun Screen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = LensaTheme.colors.backgroundColor),
        contentAlignment = Alignment.Center
    ) {

    }
    LensaLogo(
        modifier = modifier.fillMaxSize()
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LensaSplashScreenPreview() = LensaTheme {
    Screen()
}