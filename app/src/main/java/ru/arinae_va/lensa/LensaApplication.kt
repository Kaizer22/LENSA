package ru.arinae_va.lensa

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LensaApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}