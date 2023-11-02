package ru.arinae_va.lensa.data.datasource.local

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject


interface ISettingsStorage {
    var isNeedToShowOnboarding: Boolean
}

class SettingsStorage @Inject constructor(
    private val prefs: SharedPreferences,
): ISettingsStorage {

    companion object {
        val KEY_FLAG_ONBOARDING = "onboarding_flag"
    }

    override var isNeedToShowOnboarding: Boolean
        get() = prefs.getBoolean(KEY_FLAG_ONBOARDING, true)
        set(value) {
            prefs.edit {
                putBoolean(KEY_FLAG_ONBOARDING, value)
            }
        }

}