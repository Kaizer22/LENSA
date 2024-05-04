package ru.arinae_va.lensa.data.repository

import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.arinae_va.lensa.data.datasource.remote.IReviewDataSource
import ru.arinae_va.lensa.data.repositroy.ReviewRepository
import ru.arinae_va.lensa.domain.model.Review
import ru.arinae_va.lensa.domain.repository.IAuthRepository
import java.time.LocalDateTime

class ReviewRepositoryTest {

    private val reviewDataSource = mockk<IReviewDataSource>()
    private val authRepository = mockk<IAuthRepository>()

    private val testProfileId = "testProfileId"
    private val testCurrentUserId = "testCurrentUserId"

    private val reviewRepository = ReviewRepository(
        reviewDataSource = reviewDataSource,
        authRepository = authRepository,
    )

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

    @Test
    fun `getReviewsByProfileId should call data source and return list of reviews`() = runBlocking {
        // given
        coEvery { reviewDataSource.getReviewsByProfileId(testProfileId) } returns testReviews
        // when
        val reviews = reviewRepository.getReviewsByProfileId(testProfileId)
        // then
        coVerify { reviewDataSource.getReviewsByProfileId(testProfileId) }
    }

    @Test
    fun `upsertReview should call data source`() = runBlocking {
        // given
        coJustRun {
            reviewDataSource.upsertReview(
                targetProfileId = testProfileId,
                currentUserId = testCurrentUserId,
                review = testReview
            )
        }
        every { authRepository.currentUserId() } returns testCurrentUserId
        // when
        reviewRepository.upsertReview(testProfileId, testReview)
        // then
        coVerify { reviewDataSource.upsertReview(testProfileId, testCurrentUserId, testReview) }
    }

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