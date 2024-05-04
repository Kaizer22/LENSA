package ru.arinae_va.lensa.data.datasource.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ReviewDataSourceTest {

    private val database = mockk<FirebaseFirestore>()

    private val reviewDataSource = FirebaseReviewDataSource(
        database = database
    )

    private val testProfileId = "testProfileId"

    @Test
    fun `getReviewsByProfileId should map reviews and return list of reviews`() = runBlocking {
        // given
//        every { database.collection(any()) } returns any()
        // when
        //val result = reviewDataSource.getReviewsByProfileId(testProfileId)
        // then

    }

    @Test
    fun `upsertReview should map reviews and call firebase API`() {
        // given
        // when
        // then
    }

    @Test
    fun `deleteReview should call firebase API`() {
        // given
        // when
        // then
    }
}