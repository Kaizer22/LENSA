package ru.arinae_va.lensa.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.arinae_va.lensa.presentation.feature.auth.compose.AuthScreen
import ru.arinae_va.lensa.presentation.feature.auth.compose.AuthViewModel
import ru.arinae_va.lensa.presentation.feature.auth.compose.OtpScreen
import ru.arinae_va.lensa.presentation.feature.feed.compose.FeedScreen
import ru.arinae_va.lensa.presentation.feature.feed.compose.FeedViewModel
import ru.arinae_va.lensa.presentation.feature.feed.compose.SpecialistDetailsScreen
import ru.arinae_va.lensa.presentation.feature.feed.compose.SpecialistDetailsViewModel
import ru.arinae_va.lensa.presentation.feature.onboarding.compose.LensaSplashScreen
import ru.arinae_va.lensa.presentation.feature.onboarding.compose.OnboardingScreen
import ru.arinae_va.lensa.presentation.feature.onboarding.compose.OnboardingViewModel
import ru.arinae_va.lensa.presentation.feature.profile.compose.ProfileScreen
import ru.arinae_va.lensa.presentation.feature.profile.compose.ProfileViewModel
import ru.arinae_va.lensa.presentation.feature.registration.compose.RegistrationRoleSelectorScreen
import ru.arinae_va.lensa.presentation.feature.registration.compose.RegistrationScreen
import ru.arinae_va.lensa.presentation.feature.registration.compose.RegistrationViewModel
import ru.arinae_va.lensa.presentation.feature.settings.compose.AboutAppScreen
import ru.arinae_va.lensa.presentation.feature.settings.compose.FeedbackScreen
import ru.arinae_va.lensa.presentation.feature.settings.compose.SettingsScreen
import ru.arinae_va.lensa.presentation.feature.settings.compose.SettingsViewModel


@Composable
fun LensaNavGraph() {
    // TODO вложенные графы для фичей, шаринг вью моделей
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
        composable(route = LensaScreens.FEED_SCREEN.name) {
            val viewModel = hiltViewModel<FeedViewModel>()
            FeedScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }
        composable(route = LensaScreens.SETTINGS_SCREEN.name) {
            val viewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }
        composable(route = LensaScreens.PROFILE_SCREEN.name) {
            val viewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }
        composable(route = LensaScreens.SPECIALIST_DETAILS_SCREEN.name) {
            val viewModel = hiltViewModel<SpecialistDetailsViewModel>()
            SpecialistDetailsScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }
        composable(route = LensaScreens.FEEDBACK_SCREEN.name) {
            val viewModel = hiltViewModel<SettingsViewModel>()
            FeedbackScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }
        composable(route = LensaScreens.ABOUT_APP_SCREEN.name) {
            AboutAppScreen(
                navController = navController,
            )
        }
    }
}