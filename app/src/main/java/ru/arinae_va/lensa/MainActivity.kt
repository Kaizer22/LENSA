package ru.arinae_va.lensa

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavHostController
import dagger.hilt.android.AndroidEntryPoint
import ru.arinae_va.lensa.presentation.AppThemeManager
import ru.arinae_va.lensa.presentation.common.component.LensaSnackbarHost
import ru.arinae_va.lensa.presentation.navigation.LensaNavGraph
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import javax.inject.Inject

// DONE редактирование профиля
// DONE отображение только ненулевых оценок для специалиста
// DONE лоадеры
// DONE просмотр полноразмерных фото
// DONE поисковая строка
// DONE переключение аккаунтов
// DONE вычисление средней оценки специалиста
// DONE возможность оставить только один отзыв
// DONE доработка избранного
// DONE редактирование своего отзыва
// DONE переключение темы - темная, светлая
// DONE фикс анимаций текстовых кнопок!

// TODO состояния ленты: ничего нет (вообще), ничего не нашлось, ошибка +-
// TODO доработка Input (отображение обязательной иконки, маски ввода)
// TODO форматирование всех сумм

// TODO переход по ссылке на персональный сайт
// TODO переход к соц. сетям
// TODO переход на отправку email
// TODO доработка Dropdown Input
// TODO кэширование (текущий пользователь, лента) +-
// TODO пагинация ленты + доработка фильтрации

// TODO чаты:
// - DONE закрепеленные сообщения
// - DONE статус "в сети"
// - DONE отметка прочитанных сообщений

@AndroidEntryPoint
class MainActivity : PresenceAwareActivity() {

    @Inject
    lateinit var navHostController: NavHostController

    @Inject
    lateinit var snackbarHostState: MutableState<SnackbarHostState>

    @Inject
    lateinit var appThemeManager: AppThemeManager

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkTheme by appThemeManager.isDarkTheme.collectAsState()
            LensaTheme(isDarkTheme) {
                LensaSnackbarHost(
                    modifier = Modifier.semantics { testTagsAsResourceId = true },
                    state = snackbarHostState.value
                ) {
                    LensaNavGraph(
                        navController = navHostController,
                    )
                }
            }
        }
    }
}
