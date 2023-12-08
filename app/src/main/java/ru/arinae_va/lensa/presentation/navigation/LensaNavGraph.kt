package ru.arinae_va.lensa.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.arinae_va.lensa.presentation.feature.auth.compose.AuthScreen
import ru.arinae_va.lensa.presentation.feature.auth.compose.OtpScreen
import ru.arinae_va.lensa.presentation.feature.onboarding.compose.LensaSplashScreen
import ru.arinae_va.lensa.presentation.feature.onboarding.compose.OnboardingScreen
import ru.arinae_va.lensa.presentation.feature.registration.compose.RegistrationRoleSelectorScreen
import ru.arinae_va.lensa.presentation.feature.registration.compose.RegistrationScreen


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
        composable(route = LensaScreens.AUTH_SCREEN.name) {
            AuthScreen(
                navController = navController,
            )
        }
        composable(route = LensaScreens.OTP_SCREEN.name) {
            OtpScreen(
                navController = navController
            )
        }
        composable(route = LensaScreens.REGISTRATION_ROLE_SELECTOR_SCREEN.name) {
            RegistrationRoleSelectorScreen(
                navController = navController
            )
        }
        composable(route = LensaScreens.REGISTRATION_SCREEN.name) {
            RegistrationScreen(
                navController = navController,
            )
        }
    }
}