package ru.arinae_va.lensa.feature.auth

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.arinae_va.lensa.MainActivity

@RunWith(AndroidJUnit4::class)
@LargeTest
class CustomerRegistrationUITest {
    // Тестовые данные
    private val testPhoneNumber = "9212112121"
    private val testOtp = "123456"

    // Готовим окружение для выполнения теста
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun customerRegistrationTest() {
        with(composeTestRule) {
            // Ожидаем появления экрана авторизации
            waitUntilExists(
                hasTestTag("auth_screen_phone_input")
            )
            // Вводим тестовый номер телефона
            onNodeWithTag("auth_screen_phone_input")
                .performTextInput(testPhoneNumber)
            // Нажимаем кнопку продолжить
            onNodeWithTag("auth_screen_button_get_code")
                .performClick()


            // Ожидаем появления экрана ввода ОТП
            waitUntilExists(
                hasTestTag("otp_screen_otp_input")
            )
            // Вводим тестовый ОТП код
            onNodeWithTag("otp_screen_otp_input")
                .performTextInput(testOtp)
            // Нажимаем кнопку "Продолжить"
            onNodeWithTag("otp_screen_button_continue")
                .performClick()

            // Ожидаем загрузки экрана с выбором роли
            waitUntilExists(
                hasTestTag("role_selector_screen_button_customer")
            )
            // Выбираем роль пользователя "Заказчик"
            onNodeWithTag("role_selector_screen_button_customer")
                .performClick()

            // Ожидаем появления экрана регистрации заказчика
            waitUntilExists(
                hasTestTag("registration_screen_surname_input")
            )
            // Вводим тестовые данные пользователя (Фамилия, Имя, Город, Страна)
            onNodeWithTag("registration_screen_surname_input")
                .performTextInput("Иванов")
            onNodeWithTag("registration_screen_name_input")
                .performTextInput("Иван")
            onNodeWithTag("registration_screen_city_input")
                .performTextInput("Москва")
            onNodeWithTag("registration_screen_country_input")
                .performTextInput("Россия")

            // Скроллим до кнопки "Сохранить"
            onNodeWithTag("registration_screen_scrollable_container")
                .performScrollToNode(
                    hasTestTag("registration_screen_button_save")
                )
            // Нажимаем на кнопку
            onNodeWithTag("registration_screen_button_save")
                .performClick()
            // Ждем пока загрузится лента
            waitUntilExists(
                hasTestTag("feed_screen_content_container")
            )
            // Лента успешно загрузилась, новый пользователь зарегистрирован, тест пройден
        }
    }
}

// Таймаут ожидания появления элемента
private const val WAIT_UNTIL_TIMEOUT = 5_000L

// Функции для того, чтобы дождаться появления элемента на экране
fun ComposeContentTestRule.waitUntilNodeCount(
    matcher: SemanticsMatcher,
    count: Int,
    timeoutMillis: Long = WAIT_UNTIL_TIMEOUT,
) {
    waitUntil(timeoutMillis) {
        onAllNodes(matcher).fetchSemanticsNodes().size == count
    }
}

@OptIn(ExperimentalTestApi::class)
fun ComposeContentTestRule.waitUntilExists(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = WAIT_UNTIL_TIMEOUT,
) = waitUntilNodeCount(matcher, 1, timeoutMillis)

@OptIn(ExperimentalTestApi::class)
fun ComposeContentTestRule.waitUntilDoesNotExist(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = WAIT_UNTIL_TIMEOUT,
) = waitUntilNodeCount(matcher, 0, timeoutMillis)