package ru.arinae_va.lensa.di.module

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {
    @Provides
    fun providesFirebaseDatabase() = Firebase.firestore

    @Provides
    fun providesFirebaseStorage() = Firebase.storage.reference
}