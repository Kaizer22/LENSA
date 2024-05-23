package ru.arinae_va.lensa.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.arinae_va.lensa.data.datasource.local.FavouritesStorage
import ru.arinae_va.lensa.data.datasource.local.IFavouritesStorage
import ru.arinae_va.lensa.data.datasource.local.ISettingsStorage
import ru.arinae_va.lensa.data.datasource.local.SettingsStorage
import ru.arinae_va.lensa.data.datasource.remote.FirebaseChatsDataSource
import ru.arinae_va.lensa.data.datasource.remote.FirebaseFeedbackDataSource
import ru.arinae_va.lensa.data.datasource.remote.FirebaseReviewDataSource
import ru.arinae_va.lensa.data.datasource.remote.FirebasePresenceDataSource
import ru.arinae_va.lensa.data.datasource.remote.FirebaseUserProfileDataSource
import ru.arinae_va.lensa.data.datasource.remote.IChatsDataSource
import ru.arinae_va.lensa.data.datasource.remote.IFeedbackDataSource
import ru.arinae_va.lensa.data.datasource.remote.IPresenceDataSource
import ru.arinae_va.lensa.data.datasource.remote.IReviewDataSource
import ru.arinae_va.lensa.data.datasource.remote.IUserProfileDataSource
import ru.arinae_va.lensa.data.datasource.remote.legacy.FirebaseChatRequestsDataSource
import ru.arinae_va.lensa.data.datasource.remote.legacy.IChatRequestsDataSource

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    // region local
    @Binds
    abstract fun settingsStorage(settingsStorage: SettingsStorage): ISettingsStorage

    @Binds
    abstract fun favouritesStorage(favouritesStorage: FavouritesStorage): IFavouritesStorage

    // endregion

    // region remote
    @Binds
    abstract fun userInfoDataSource(userInfoStorage: FirebaseUserProfileDataSource): IUserProfileDataSource

    @Binds
    abstract fun feedbackDataSource(feedbackDataSource: FirebaseFeedbackDataSource): IFeedbackDataSource

    @Binds
    abstract fun reviewDataSource(reviewDataSource: FirebaseReviewDataSource): IReviewDataSource

    @Binds
    abstract fun chatsDataSource(chatsDataSource: FirebaseChatsDataSource): IChatsDataSource

    @Binds
    abstract fun chatRequestsDataSource(chatRequestsDataSource: FirebaseChatRequestsDataSource): IChatRequestsDataSource

    @Binds
    abstract fun presenceDataSource(presenceDataSource: FirebasePresenceDataSource): IPresenceDataSource
    // endregion
}