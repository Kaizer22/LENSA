package ru.arinae_va.lensa.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.arinae_va.lensa.data.datasource.local.ISettingsStorage
import ru.arinae_va.lensa.data.datasource.local.SettingsStorage

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {
    @Binds
    abstract fun settingsStorage(settingsStorage: SettingsStorage): ISettingsStorage
}