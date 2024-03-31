package ru.arinae_va.lensa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import dagger.hilt.android.AndroidEntryPoint
import ru.arinae_va.lensa.presentation.navigation.LensaNavGraph
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import javax.inject.Inject

// TODO редактирование профиля +-
// TODO отображение только ненулевых оценок для специалиста +
// TODO лоадеры
// TODO переключение аккаунтов
// TODO просмотр полноразмерных фото +-
// TODO вычисление средней оценки специалиста
// TODO verifyPhoneNumber на AuthScreen

// TODO возможность оставить только один отзыв + редактирование своего отзыва
// TODO состояния ленты: ничего нет (вообще), ничего не нашлось, ошибка +-
// TODO переключение темы - темная, светлая
// TODO поисковая строка

// TODO доработка Dropdown Input
// TODO доработка Input (отображение обязательной иконки)
// TODO форматирование суммы тарифа
// TODO кэширование (текущий пользователь, лента)
// TODO пагинация ленты + доработка фильтрации
// TODO переход по ссылке на персональный сайт
// TODO переход к соц. сетям

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navHostController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LensaTheme {
                LensaNavGraph(
                    navController = navHostController,
                )
            }
        }
    }
}
