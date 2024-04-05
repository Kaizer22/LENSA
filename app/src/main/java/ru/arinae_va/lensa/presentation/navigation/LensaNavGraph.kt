package ru.arinae_va.lensa.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import ru.arinae_va.lensa.presentation.common.screen.ErrorScreen
import ru.arinae_va.lensa.presentation.feature.auth.compose.AuthScreen
import ru.arinae_va.lensa.presentation.feature.auth.viewmodel.AuthViewModel
import ru.arinae_va.lensa.presentation.feature.auth.compose.OtpScreen
import ru.arinae_va.lensa.presentation.feature.auth.viewmodel.OtpViewModel
import ru.arinae_va.lensa.presentation.feature.favourite.compose.FavouritesFolderScreen
import ru.arinae_va.lensa.presentation.feature.favourite.compose.FavouritesScreen
import ru.arinae_va.lensa.presentation.feature.favourite.viewmodel.FavouritesFolderViewModel
import ru.arinae_va.lensa.presentation.feature.favourite.viewmodel.FavouritesViewModel
import ru.arinae_va.lensa.presentation.feature.feed.compose.FeedScreen
import ru.arinae_va.lensa.presentation.feature.feed.viewmodel.FeedViewModel
import ru.arinae_va.lensa.presentation.feature.feed.compose.ProfileDetailsScreen
import ru.arinae_va.lensa.presentation.feature.feed.viewmodel.ProfileDetailsViewModel
import ru.arinae_va.lensa.presentation.feature.onboarding.compose.LensaSplashScreen
import ru.arinae_va.lensa.presentation.feature.onboarding.compose.OnboardingScreen
import ru.arinae_va.lensa.presentation.feature.onboarding.compose.OnboardingViewModel
import ru.arinae_va.lensa.presentation.feature.registration.compose.RegistrationRoleSelectorScreen
import ru.arinae_va.lensa.presentation.feature.registration.compose.RegistrationScreen
import ru.arinae_va.lensa.presentation.feature.registration.viewmodel.EMPTY_USER_ID
import ru.arinae_va.lensa.presentation.feature.registration.viewmodel.RegistrationViewModel
import ru.arinae_va.lensa.presentation.feature.settings.compose.AboutAppScreen
import ru.arinae_va.lensa.presentation.feature.settings.compose.FeedbackScreen
import ru.arinae_va.lensa.presentation.feature.settings.compose.SettingsScreen
import ru.arinae_va.lensa.presentation.feature.settings.viewmodel.SettingsViewModel
import java.net.URLDecoder
import java.net.URLEncoder

const val PHONE_ID_KEY = "phone"
const val ERROR_TEXT_KEY = "errorText"
const val IS_SPECIALIST_KEY = "isSpecialist"
const val USER_ID_KEY = "userId"
const val PROFILE_UID_KEY = "profileUid"
const val IS_SELF_PROFILE_KEY = "isSelf"
const val FOLDER_NAME_KEY = "folderName"
const val FOLDER_PROFILE_IDS_KEY = "folderProfileIds"

fun <A> String?.fromJson(type: Class<A>): A {
    //return Gson().fromJson(this, type)
    return Gson().fromJson(URLDecoder.decode(this, "utf-8"), type)
}

fun <A> A.toJson(): String {
    //return Gson().toJson(this)
    return URLEncoder.encode(Gson().toJson(this), "utf-8")
}

@Composable
fun LensaNavGraph(
    navController: NavHostController,
) {
    // TODO вложенные графы для фичей, шаринг вью моделей
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
                viewModel = viewModel,
            )
        }

        composable(route = LensaScreens.AUTH_SCREEN.name) {
            val viewModel = hiltViewModel<AuthViewModel>()
            AuthScreen(
                viewModel = viewModel,
            )
        }

        composable(route = LensaScreens.OTP_SCREEN.name +
                "/{$PHONE_ID_KEY}",
            arguments = listOf(
                navArgument(PHONE_ID_KEY) {
                    type = NavType.StringType
                }
            )) { backStackEntry ->
            val viewModel = hiltViewModel<OtpViewModel>(backStackEntry)
            val phoneNumber = remember {
                requireNotNull(backStackEntry.arguments).getString(PHONE_ID_KEY, "")
            }
            LaunchedEffect(viewModel) {
                viewModel.onAttach(phoneNumber)
            }
            OtpScreen(
                viewModel = viewModel,
            )
        }

        composable(route = LensaScreens.COMMON_ERROR_SCREEN.name +
                "/{$ERROR_TEXT_KEY}",
            arguments = listOf(
                navArgument(ERROR_TEXT_KEY) {
                    type = NavType.StringType
                }
            )) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            ErrorScreen(
                navController = navController,
                text = arguments.getString(ERROR_TEXT_KEY, "")
            )
        }

        composable(route = LensaScreens.REGISTRATION_ROLE_SELECTOR_SCREEN.name) {
            val viewModel = hiltViewModel<RegistrationViewModel>()
            RegistrationRoleSelectorScreen(
                viewModel = viewModel,
            )
        }

        composable(
            route = LensaScreens.REGISTRATION_SCREEN.name +
                    "/{$IS_SPECIALIST_KEY}" +
                    "/{$USER_ID_KEY}",
            arguments = listOf(
                navArgument(IS_SPECIALIST_KEY) {
                    type = NavType.BoolType
                },
                navArgument(USER_ID_KEY) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            ),
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val viewModel = hiltViewModel<RegistrationViewModel>()
            LaunchedEffect(Unit) {
                val userId = arguments.getString(USER_ID_KEY)
                viewModel.setType(arguments.getBoolean(IS_SPECIALIST_KEY))
                if (!userId.isNullOrEmpty() && userId != EMPTY_USER_ID) {
                    viewModel.setUser(userId)
                }
            }
            RegistrationScreen(
                viewModel = viewModel,
            )
        }

        composable(route = LensaScreens.FEED_SCREEN.name) {
            val viewModel = hiltViewModel<FeedViewModel>()
            FeedScreen(
                viewModel = viewModel,
            )
        }

        composable(route = LensaScreens.SETTINGS_SCREEN.name) {
            val viewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(
                viewModel = viewModel,
            )
        }

        composable(route = "${LensaScreens.SPECIALIST_DETAILS_SCREEN.name}/" +
                "{$PROFILE_UID_KEY}/" +
                "{$IS_SELF_PROFILE_KEY}",
            arguments = listOf(
                navArgument(PROFILE_UID_KEY) {
                    type = NavType.StringType
                },
                navArgument(IS_SELF_PROFILE_KEY) {
                    type = NavType.BoolType
                }
            )
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val viewModel = hiltViewModel<ProfileDetailsViewModel>()

            val userId = arguments.getString(PROFILE_UID_KEY)
            val isSelf = arguments.getBoolean(IS_SELF_PROFILE_KEY)

            LaunchedEffect(arguments) {
                userId?.let { uid ->
                    viewModel.loadUserProfile(
                        profileUid = uid,
                        isSelf = isSelf
                    )
                }

            }

            ProfileDetailsScreen(
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

        composable(route = LensaScreens.FAVOURITES_SCREEN.name) {
            val viewModel = hiltViewModel<FavouritesViewModel>()
            LaunchedEffect(viewModel) {
                viewModel.loadFolders()
            }
            FavouritesScreen(
                viewModel = viewModel,
            )
        }

        composable(
            route = LensaScreens.FAVOURITE_FOLDER_SCREEN.name +
                    "/{$FOLDER_NAME_KEY}",
            arguments = listOf(
                navArgument(FOLDER_NAME_KEY) {
                    type = NavType.StringType
                },
            )
        ) { backStackEntry ->
            val viewModel = hiltViewModel<FavouritesFolderViewModel>()
            val arguments = requireNotNull(backStackEntry.arguments)

            val folderName = arguments.getString(FOLDER_NAME_KEY)
            LaunchedEffect(viewModel) {
                folderName?.let { name ->
                    viewModel.loadProfiles(
                        folderName = name,
                    )
                }
            }
            FavouritesFolderScreen(
                viewModel = viewModel,
            )
        }
    }
}