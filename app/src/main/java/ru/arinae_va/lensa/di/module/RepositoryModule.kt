package ru.arinae_va.lensa.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.arinae_va.lensa.data.repositroy.SettingsRepository
import ru.arinae_va.lensa.data.repositroy.UserInfoRepository
import ru.arinae_va.lensa.domain.repository.ISettingsRepository
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun settingsRepository(settingsRepository: SettingsRepository): ISettingsRepository

    @Singleton
    @Binds
    abstract fun userInfoRepository(userInfoRepository: UserInfoRepository): IUserInfoRepository
}