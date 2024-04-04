package ru.arinae_va.lensa.data.repositroy

import ru.arinae_va.lensa.data.datasource.remote.IFeedbackDataSource
import ru.arinae_va.lensa.domain.repository.IFeedbackRepository
import javax.inject.Inject

class FeedbackRepository @Inject constructor(
    private val feedbackDataSource: IFeedbackDataSource,
): IFeedbackRepository {
    override suspend fun sendFeedback(userUid: String?, text: String) =
        feedbackDataSource.sendFeedback(userUid, text)
}