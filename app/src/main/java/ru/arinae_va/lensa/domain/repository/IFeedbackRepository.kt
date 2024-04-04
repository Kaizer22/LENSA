package ru.arinae_va.lensa.domain.repository

interface IFeedbackRepository {
    suspend fun sendFeedback(userUid: String?, text: String)
}