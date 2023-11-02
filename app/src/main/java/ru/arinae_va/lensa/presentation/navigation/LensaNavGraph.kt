package ru.arinae_va.lensa.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.arinae_va.lensa.presentation.feature.onboarding.compose.LensaSplashScreen
import ru.arinae_va.lensa.presentation.feature.onboarding.compose.OnboardingScreen


@Composable
fun LensaNavGraph() {
    val navController = rememberNavController()
    return NavHost(
        navController = navController,
        startDestination = LensaScreens.SPLASH_SCREEN.name,
    ) {
        composable(route = LensaScreens.SPLASH_SCREEN.name) {
            LensaSplashScreen(
                navController = navController,
            )
        }
        composable(route = LensaScreens.ONBOARDING_SCREEN.name) {
            OnboardingScreen(
                navController = navController,
            )
        }
    }
}