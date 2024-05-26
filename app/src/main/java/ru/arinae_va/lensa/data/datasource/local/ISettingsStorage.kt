package ru.arinae_va.lensa.data.datasource.local

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject


interface ISettingsStorage {
    var isDarkModeOn: Boolean
    var isNeedToShowOnboarding: Boolean
    var lastLoggedInUser: String?
}

class SettingsStorage @Inject constructor(
    private val prefs: SharedPreferences,
): ISettingsStorage {

    companion object {
        val KEY_FLAG_DARK_MODE = "dark_mode_flag"
        val KEY_FLAG_ONBOARDING = "onboarding_flag"
        val KEY_LAST_LOGGED_IN_USER = "last_logged_in_user"
    }

    override var isDarkModeOn: Boolean
        get() = prefs.getBoolean(KEY_FLAG_DARK_MODE, false)
        set(value) {
            prefs.edit {
                putBoolean(KEY_FLAG_DARK_MODE, value)
            }
        }

    override var isNeedToShowOnboarding: Boolean
        get() = prefs.getBoolean(KEY_FLAG_ONBOARDING, true)
        set(value) {
            prefs.edit {
                putBoolean(KEY_FLAG_ONBOARDING, value)
            }
        }
    override var lastLoggedInUser: String?
        get() = prefs.getString(KEY_LAST_LOGGED_IN_USER, "")
        set(value) {
            prefs.edit {
                putString(KEY_LAST_LOGGED_IN_USER, value)
            }
        }
}