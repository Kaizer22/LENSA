package ru.arinae_va.lensa.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.arinae_va.lensa.presentation.feature.auth.compose.AuthScreen
import ru.arinae_va.lensa.presentation.feature.auth.compose.AuthViewModel
import ru.arinae_va.lensa.presentation.feature.auth.compose.OtpScreen
import ru.arinae_va.lensa.presentation.feature.onboarding.compose.LensaSplashScreen
import ru.arinae_va.lensa.presentation.feature.onboarding.compose.OnboardingScreen
import ru.arinae_va.lensa.presentation.feature.onboarding.compose.OnboardingViewModel
import ru.arinae_va.lensa.presentation.feature.registration.compose.RegistrationRoleSelectorScreen
import ru.arinae_va.lensa.presentation.feature.registration.compose.RegistrationScreen
import ru.arinae_va.lensa.presentation.feature.registration.compose.RegistrationViewModel


@Composable
fun LensaNavGraph() {
    val navController = rememberNavController()
    return NavHost(
        navController = navController,
        startDestination = LensaScreens.SPLASH_SCREEN.name,
    ) {
        composable(route = LensaScreens.SPLASH_SCREEN.name) {
            val viewModel = hiltViewModel<OnboardingViewModel>()
            LensaSplashScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }
        composable(route = LensaScreens.ONBOARDING_SCREEN.name) {
            val viewModel = hiltViewModel<OnboardingViewModel>()
            OnboardingScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }
        composable(route = LensaScreens.AUTH_SCREEN.name) {
            val viewModel = hiltViewModel<AuthViewModel>()
            AuthScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }
        composable(route = LensaScreens.OTP_SCREEN.name) {
            val viewModel = hiltViewModel<AuthViewModel>()
            OtpScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }
        composable(route = LensaScreens.REGISTRATION_ROLE_SELECTOR_SCREEN.name) {
            RegistrationRoleSelectorScreen(
                navController = navController,
            )
        }
        composable(route = LensaScreens.REGISTRATION_SCREEN.name) {
            val viewModel = hiltViewModel<RegistrationViewModel>()
            RegistrationScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }
    }
}