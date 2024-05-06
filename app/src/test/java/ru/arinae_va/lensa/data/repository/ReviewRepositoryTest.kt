package ru.arinae_va.lensa.data.repository

import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions
import ru.arinae_va.lensa.data.datasource.remote.IReviewDataSource
import ru.arinae_va.lensa.data.repositroy.ReviewRepository
import ru.arinae_va.lensa.domain.model.Review
import ru.arinae_va.lensa.domain.repository.IAuthRepository
import java.time.LocalDateTime

// Тесты класса репозитория пользовательских отзывов
class ReviewRepositoryTest {

    // Мок data source отзывов
    private val reviewDataSource = mockk<IReviewDataSource>()
    // Мок репозитория авторизации (используется для доступа к
    // данным о текущей пользовательской сессии)
    private val authRepository = mockk<IAuthRepository>()

    // Тестируемый класс репозитория
    private val reviewRepository = ReviewRepository(
        reviewDataSource = reviewDataSource,
        authRepository = authRepository,
    )
    // Тестовые данные - тестовые id, отзыв и список, содержащий этот один отзыв
    private val testProfileId = "testProfileId"
    private val testCurrentUserId = "testCurrentUserId"

    private val testReview = Review(
        authorId = "testAuthorId",
        profileId = "testProfileId",
        name = "testName",
        surname = "testSurname",
        rating = 5.0f,
        text = "Test review text",
        avatarUrl = "testAvatarUrl",
        dateTime = LocalDateTime.now(),
    )

    private val testReviews = listOf(testReview)

    // Тесты построены по схеме Given - When - Then (см. FeedViewModelTest.kt)

    // Тест №1. Вызов getReviewsByProfileId должен вызывать соответствующий метод data source класса
    // и возвращать список отзывов
    @Test
    fun `getReviewsByProfileId should call data source and return list of reviews`() = runBlocking {
        // given
        // Мок вызова метода data source с возвратом целевого результата
        coEvery { reviewDataSource.getReviewsByProfileId(testProfileId) } returns testReviews
        // when
        // Получаем список отзывов
        val reviews = reviewRepository.getReviewsByProfileId(testProfileId)
        // then
        // Проверяем, что метод data source был вызван
        coVerify { reviewDataSource.getReviewsByProfileId(testProfileId) }
        // Проверяем, что полученный список отзывов соответствует ожидаемому
        Assertions.assertEquals(testReviews, reviews)
    }

    // Тест №2. Вызов upsertReview должен вызывать соответствующий метод data source класса
    @Test
    fun `upsertReview should call data source`() = runBlocking {
        // given
        // Т.к. reviewDataSource.upsertReview ничего не возвращает и выполняется асинхронно,
        // то мокаем через coJustRun
        coJustRun {
            reviewDataSource.upsertReview(
                targetProfileId = testProfileId,
                currentUserId = testCurrentUserId,
                review = testReview
            )
        }
        // Каждый запрос id текущего пользователя будет возвращать testCurrentUserId
        every { authRepository.currentUserId() } returns testCurrentUserId
        // when
        reviewRepository.upsertReview(testProfileId, testReview)
        // then
        // Проверяем, что метод data source вызвался
        coVerify { reviewDataSource.upsertReview(testProfileId, testCurrentUserId, testReview) }
    }

    // Тест №3. Вызов deleteReview должен вызывать соответствующий метод data source.
    // По структуре аналогичен тесту №2, только проверяем другие методы
    @Test
    fun `deleteReview should call data source`() = runBlocking {
        // given
        coJustRun {
            reviewDataSource.deleteReview(
                targetProfileId = testProfileId,
                currentUserId = testCurrentUserId,
            )
        }
        every { authRepository.currentUserId() } returns testCurrentUserId
        // when
        reviewRepository.deleteReview(testProfileId)
        // then
        coVerify { reviewDataSource.deleteReview(testProfileId, testCurrentUserId) }
    }
}