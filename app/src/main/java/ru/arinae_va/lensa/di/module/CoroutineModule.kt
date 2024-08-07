package ru.arinae_va.lensa.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class CoroutineModule {
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}