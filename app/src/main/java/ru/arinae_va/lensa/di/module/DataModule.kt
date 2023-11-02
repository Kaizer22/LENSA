package ru.arinae_va.lensa.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    companion object {
        const val PREFERENCES_DATA_STORE = "preferences"
        const val USER_PREFERENCES = "user_preferences"
    }

    @Singleton
    @Provides
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        val datastore = PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(PREFERENCES_DATA_STORE)
        }
        return datastore
    }

    @Singleton
    @Provides
    fun providesUserPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences(
        USER_PREFERENCES,
        Context.MODE_PRIVATE
    )
}