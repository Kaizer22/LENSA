package ru.arinae_va.lensa.presentation.feature.feed.viewmodel

import android.content.Context
import androidx.navigation.NavHostController
import io.mockk.coEvery
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import ru.arinae_va.lensa.domain.model.FeedFilter
import ru.arinae_va.lensa.domain.model.OrderType
import ru.arinae_va.lensa.domain.model.Price
import ru.arinae_va.lensa.domain.model.PriceCurrency
import ru.arinae_va.lensa.domain.model.SocialMedia
import ru.arinae_va.lensa.domain.model.UserProfileModel
import ru.arinae_va.lensa.domain.model.UserProfileType
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import ru.arinae_va.lensa.presentation.feature.feed.compose.SocialMediaType
import ru.arinae_va.lensa.presentation.navigation.LensaScreens

// Юнит-тесты класса FeedViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {
    // Чтобы полностью контролировать асинхронные процессы при выполнении
    // тестов создаем свой экземпляр StandardTestDispatcher
    // Это позволит вызывать в тестах метод advanceUntilIdle(), чтобы гарантировать
    // выполнение асинхронных операций и обновление состояний
    private val dispatcher = StandardTestDispatcher()

    // Моки - фиктивные реализации экземпляров классов для применения в тестах
    // https://ru.wikipedia.org/wiki/Mock-объект

    // Мок класса Context
    private val context = mockk<Context>(relaxed = true)

    // Мок NavHostController используемого для навигации по экранам приложения
    private val navHostController = mockk<NavHostController>()

    // Мок репозитория пользовательских профилей
    private val userProfileRepository = mockk<IUserProfileRepository>()

    // Экземпляр тестируемого класса
    // В конструктор передаем подготовленные моки
    private val viewModel = FeedViewModel(
        ioDispatcher = dispatcher,
        context = context,
        navHostController = navHostController,
        userProfileRepository = userProfileRepository,
    )

    // Блок с тестовыми данными
    private val testCity1 = "testCity1"
    private val testCity2 = "testCity2"

    private val testProfileId1 = "testProfileId1"
    private val testProfileId2 = "testProfileId2"

    private val testName = "testName"
    private val testUserProfile1 = UserProfileModel(
        userId = "",
        profileId = testProfileId1,
        type = UserProfileType.SPECIALIST,
        name = testName,
        surname = "",
        specialization = "",
        rating = 3.0f,
        avatarUrl = "",
        country = "",
        city = testCity1,
        personalSite = "",
        phoneNumber = "",
        email = "",
        socialMedias = listOf(SocialMedia("", SocialMediaType.INSTAGRAM)),
        about = "",
        portfolioUrls = listOf(""),
        prices = listOf(
            Price(
                name = "",
                text = "",
                price = 1,
                currency = PriceCurrency.RUB
            )
        ),
        minimalPrice = 1200,
        maximalPrice = 1300,
        reviews = emptyList(),
    )

    private val testUserProfile2 = UserProfileModel(
        userId = "",
        profileId = testProfileId2,
        type = UserProfileType.SPECIALIST,
        name = "",
        surname = "",
        specialization = "",
        rating = 3.0f,
        avatarUrl = "",
        country = "",
        city = testCity2,
        personalSite = "",
        phoneNumber = "",
        email = "",
        socialMedias = listOf(SocialMedia("", SocialMediaType.INSTAGRAM)),
        about = "",
        portfolioUrls = listOf(""),
        prices = listOf(
            Price(
                name = "",
                text = "",
                price = 1,
                currency = PriceCurrency.RUB
            )
        ),
        minimalPrice = 1200,
        maximalPrice = 1300,
        reviews = emptyList(),
    )

    // Тестовый ответ для ленты пользователей
    // Состоит из двух тестовых пользователей, созданных выше
    private val testFeed = listOf(
        testUserProfile1,
        testUserProfile2,
    )

    // Перед выполнением тестов переключаем Main dispatcher
    // (на котором выполняются корутины, запущенные из viewModelScope)
    // на наш подготовленный dispatcher для тестов
    @Before
    fun before() {
        Dispatchers.setMain(dispatcher)
    }

    // Далее все тесты строятся по схеме:
    // Given - When - Then
    // Given - исходные данные и моки вызовов методов
    // When - вызов целевых тестируемых методов классов
    // Then - проверка результатов операций на соответствие требованиям

    // Тест №1. Вызов метода onAttach должен подгружать ленту специалистов
    @Test
    fun `onAttach should load feed`() = runTest {
        // given
        // Каждый вызов метода репозитория с любым фильтром будет возвращать заготовленный testFeed
        coEvery { userProfileRepository.getFeed(any()) } returns testFeed
        // when
        // Вызываем целевой метод
        viewModel.onAttach()
        // Используя подготовленный dispatcher ожидаем завершения всех асинхронных операций
        // и обновления состояния экрана viewModel.state
        dispatcher.scheduler.advanceUntilIdle()
        // then
        // Выполняем проверку, что загрузка завершилась
        Assertions.assertEquals(false, viewModel.state.value.isLoading)
        // Проверяем, что лента в state соответствует замоканому значению
        Assertions.assertEquals(testFeed, viewModel.state.value.feed)
    }

    // Тест №2. Вызов onProfileClick вызывает навигацию на профиль текущего пользователя
    @Test
    fun `onProfileClick should navigate to current profile details`() {
        // given
        val testId = "testId"
        val isSelf = true
        every { userProfileRepository.currentProfileId() } returns testId
        // Если метод ничего не возвращает, то используем justRun, чтобы замокать его вызов
        justRun { navHostController.navigate(any<String>()) }
        // when
        // Вызываем целевой метод. Т.к. в данном случае асинхронной работы нет,
        // то не нужно вызывать advanceUntilIdle()
        viewModel.onProfileClick()
        // then
        // Проверяем, что был вызван метод навигации с заданными параметрами
        verify {
            navHostController.navigate(
                LensaScreens.SPECIALIST_DETAILS_SCREEN.name +
                        "/$testId" +
                        "/$isSelf"
            )
        }
    }

    // Тест №3. Вызов onApplyFilterClick должен подгружать отфильтрованную ленту специалистов
    @Test
    fun `onApplyFilterClick should load filtered feed`() = runTest {
        // given
        val resultFeed = listOf(testUserProfile2)
        // Мокаем вызов getFeed с любым фильтром (чтобы точно убедиться, что фильтр применяется)
        coEvery { userProfileRepository.getFeed(any()) } returns testFeed
        // Мокаем целевой вызов с фильтром по городу
        coEvery {
            userProfileRepository.getFeed(
                feedFilter = FeedFilter.EMPTY.copy(city = testCity2)
            )
        } returns resultFeed
        // С помощью метода устанавливаем город для фильтра
        viewModel.onFilterCityChanged(testCity2)
        dispatcher.scheduler.advanceUntilIdle()
        // when
        // Вызываем метод применения фильтра
        viewModel.onApplyFilterClick()
        dispatcher.scheduler.advanceUntilIdle()
        // then
        // Проверяем, что загрузка завершилась и в результате лента отфильтровалась
        Assertions.assertEquals(false, viewModel.state.value.isLoading)
        Assertions.assertEquals(resultFeed, viewModel.state.value.feed)
    }

    // Тест №4. Вызов onClearFilterClick должен очищать фильтр и подгружать ленту специалистов
    @Test
    fun `onClearFilterClick should clear feed filter`() = runTest {
        // given
        // Мокаем негативный кейс - если есть фильтр по городу,
        // то вернется отфильтрованный результат
        val wrongFeed = listOf(testUserProfile1)
        coEvery {
            userProfileRepository.getFeed(
                feedFilter = FeedFilter.EMPTY.copy(city = testCity1)
            )
        } returns wrongFeed

        // Мок для позитивного кейса - если фильтр пуст, то возвращаем ленту как есть
        coEvery { userProfileRepository.getFeed(FeedFilter.EMPTY) } returns testFeed
        // Устанавливаем город для фильтрации
        viewModel.onFilterCityChanged(testCity1)
        dispatcher.scheduler.advanceUntilIdle()
        // when
        // Очищаем фильтр
        viewModel.onClearFilterClick()
        dispatcher.scheduler.advanceUntilIdle()
        // then
        // Проверяем, что фильтр пуст
        Assertions.assertEquals(FeedFilter.EMPTY, viewModel.state.value.filter)
        Assertions.assertEquals(false, viewModel.state.value.isLoading)
        // Проверяем, что вернулась неотфильтрованная лента специалистов
        Assertions.assertEquals(testFeed, viewModel.state.value.feed)
    }

    // Тест №5. Вызов onCardClick должен вызывать навигацию на профиль специалиста
    // См. Тест №2 (только в данном случае флаг isSelf = false)
    @Test
    fun `onCardClick should navigate to profile details`() {
        // given
        val isSelf = false
        justRun { navHostController.navigate(any<String>()) }
        coEvery { userProfileRepository.getFeed(any()) } returns testFeed
        // Вызываем onAttach, чтобы подгрузить ленту специалистов
        viewModel.onAttach()
        dispatcher.scheduler.advanceUntilIdle()
        // when
        // Вызываем onCardClick для одного специалиста из текущей ленты
        viewModel.onCardClick(testProfileId1)
        // then
        verify {
            navHostController.navigate(
                LensaScreens.SPECIALIST_DETAILS_SCREEN.name +
                        "/$testProfileId1" +
                        "/$isSelf"
            )
        }
    }

    // Тест №6. Вызов onSearchTextChanged обновляет фильтр и подгружает ленту с результатами поиска
    @Test
    fun `onSearchTextChanged should update state and load filtered feed`() {
        // given
        // Мок на позитивный кейс - лента отфильтрована по поисковому запросу
        val resultFeed = listOf(testUserProfile1)
        coEvery {
            userProfileRepository.getFeed(
                feedFilter = FeedFilter.EMPTY.copy(searchQuery = testName)
            )
        } returns resultFeed

        // Мок на негативный кейс - поисковый запрос не обновился и лента не отфильтрована
        coEvery { userProfileRepository.getFeed(FeedFilter.EMPTY) } returns testFeed
        // when
        // Задаем поисковый запрос
        viewModel.onSearchTextChanged(testName)
        dispatcher.scheduler.advanceUntilIdle()
        // then
        // Проверяем, что лента отфильтрована в соответствии с поисковым запросом
        Assertions.assertEquals(resultFeed, viewModel.state.value.feed)
    }

    // Тест №7. Вызов onFilterCountryChanged обновляет страну в фильтре
    @Test
    fun `onFilterCountryChanged should update state`() = runTest {
        // given
        val testCountry = "testCountry"
        // when
        viewModel.onFilterCountryChanged(testCountry)
        dispatcher.scheduler.advanceUntilIdle()
        // then
        Assertions.assertEquals(testCountry, viewModel.state.value.filter.country)
    }

    // Тест №8. Вызов onFilterCityChanged обновляет город в фильтре
    @Test
    fun `onFilterCityChanged should update state`() = runTest {
        // given
        val testCity = "testCity"
        // when
        viewModel.onFilterCityChanged(testCity)
        dispatcher.scheduler.advanceUntilIdle()
        // then
        Assertions.assertEquals(testCity, viewModel.state.value.filter.city)
    }

    // Тест №9. Вызов onFilterSpecializationChanged обновляет специализацию в фильтре
    @Test
    fun `onFilterSpecializationChanged should update state`() = runTest {
        // given
        val testSpecialization = "testSpecialization"
        // when
        viewModel.onFilterSpecializationChanged(testSpecialization)
        dispatcher.scheduler.advanceUntilIdle()
        // then
        Assertions.assertEquals(testSpecialization, viewModel.state.value.filter.specialization)
    }

    // Тест №10. Вызов onFilterOrderChanged обновляет порядок сортировки
    @Test
    fun `onFilterOrderChanged should update state`() = runTest {
        // given
        val testOrder = OrderType.RATING_ASC
        // when
        viewModel.onFilterOrderChanged(testOrder)
        dispatcher.scheduler.advanceUntilIdle()
        // then
        Assertions.assertEquals(testOrder, viewModel.state.value.filter.order)
    }

    // Тест №11. Вызов onFilterPriceToChanged должен генерировать ошибку валидации, если
    // цена "ОТ" больше цены "ДО"
    @Test
    fun `onFilterPriceToChanged should create validation error and update state`() = runTest {
        // given
        val testPriceToWrong = 12000
        viewModel.onFilterPriceFromChanged(15000)
        dispatcher.scheduler.advanceUntilIdle()
        // when
        viewModel.onFilterPriceToChanged(testPriceToWrong)
        dispatcher.scheduler.advanceUntilIdle()
        // then
        Assertions.assertTrue(
            viewModel.state.value.filterValidationErrors.isNotEmpty()
        )
    }

    // Тест №12. Вызов onFilterPriceFromChanged с корректным значением должен обновлять фильтр
    @Test
    fun `onFilterPriceFromChanged should validate price and update state`() = runTest {
        // given
        val testPriceFrom = 12000
        viewModel.onFilterPriceToChanged(15000)
        dispatcher.scheduler.advanceUntilIdle()
        // when
        viewModel.onFilterPriceFromChanged(testPriceFrom)
        dispatcher.scheduler.advanceUntilIdle()
        // then
        Assertions.assertEquals(testPriceFrom, viewModel.state.value.filter.priceFrom)
    }
}