package ru.arinae_va.lensa.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.arinae_va.lensa.data.repositroy.AuthRepository
import ru.arinae_va.lensa.data.repositroy.ChatRepository
import ru.arinae_va.lensa.data.repositroy.FavouritesRepository
import ru.arinae_va.lensa.data.repositroy.FeedbackRepository
import ru.arinae_va.lensa.data.repositroy.MessageRepository
import ru.arinae_va.lensa.data.repositroy.ReviewRepository
import ru.arinae_va.lensa.data.repositroy.SettingsRepository
import ru.arinae_va.lensa.data.repositroy.UserProfileRepository
import ru.arinae_va.lensa.data.repositroy.legacy.ChatRequestRepository
import ru.arinae_va.lensa.domain.repository.IAuthRepository
import ru.arinae_va.lensa.domain.repository.IChatRepository
import ru.arinae_va.lensa.domain.repository.legacy.IChatRequestRepository
import ru.arinae_va.lensa.domain.repository.IFavouritesRepository
import ru.arinae_va.lensa.domain.repository.IFeedbackRepository
import ru.arinae_va.lensa.domain.repository.IMessageRepository
import ru.arinae_va.lensa.domain.repository.IReviewRepository
import ru.arinae_va.lensa.domain.repository.ISettingsRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun settingsRepository(settingsRepository: SettingsRepository): ISettingsRepository

    @Singleton
    @Binds
    abstract fun userProfileRepository(userProfileRepository: UserProfileRepository): IUserProfileRepository

    @Singleton
    @Binds
    abstract fun authRepository(authRepository: AuthRepository): IAuthRepository

    @Singleton
    @Binds
    abstract fun favouritesRepository(favouritesRepository: FavouritesRepository): IFavouritesRepository

    @Singleton
    @Binds
    abstract fun reviewRepository(reviewRepository: ReviewRepository): IReviewRepository

    @Singleton
    @Binds
    abstract fun feedbackRepository(feedbackRepository: FeedbackRepository): IFeedbackRepository

    @Singleton
    @Binds
    abstract fun chatRepository(chatRepository: ChatRepository): IChatRepository

    @Singleton
    @Binds
    abstract fun messageRepository(messageRepository: MessageRepository): IMessageRepository

    @Singleton
    @Binds
    abstract fun chatRequestRepository(chatRequestRepository: ChatRequestRepository): IChatRequestRepository
}