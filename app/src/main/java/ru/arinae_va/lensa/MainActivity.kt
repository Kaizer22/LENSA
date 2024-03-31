package ru.arinae_va.lensa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import dagger.hilt.android.AndroidEntryPoint
import ru.arinae_va.lensa.presentation.navigation.LensaNavGraph
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import javax.inject.Inject

// DONE редактирование профиля
// DONE отображение только ненулевых оценок для специалиста
// DONE лоадеры
// DONE просмотр полноразмерных фото

// TODO переключение аккаунтов
// TODO вычисление средней оценки специалиста
// TODO возможность оставить только один отзыв + редактирование своего отзыва
// TODO поисковая строка

// TODO состояния ленты: ничего нет (вообще), ничего не нашлось, ошибка +-
// TODO переключение темы - темная, светлая

// TODO доработка Dropdown Input
// TODO доработка Input (отображение обязательной иконки)
// TODO форматирование суммы тарифа
// TODO кэширование (текущий пользователь, лента)
// TODO пагинация ленты + доработка фильтрации
// TODO переход по ссылке на персональный сайт
// TODO переход к соц. сетям

// TODO чаты

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
