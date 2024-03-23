package ru.arinae_va.lensa.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.arinae_va.lensa.data.datasource.local.FavouritesStorage
import ru.arinae_va.lensa.data.datasource.local.IFavouritesStorage
import ru.arinae_va.lensa.data.datasource.local.ISettingsStorage
import ru.arinae_va.lensa.data.datasource.local.SettingsStorage
import ru.arinae_va.lensa.data.datasource.remote.FirebaseUserInfoDataSource
import ru.arinae_va.lensa.data.datasource.remote.IUserInfoDataSource

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
    abstract fun userInfoDataSource(userInfoStorage: FirebaseUserInfoDataSource): IUserInfoDataSource

    // endregion
}