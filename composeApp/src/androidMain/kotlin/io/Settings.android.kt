package io

import android.content.Context
import android.content.SharedPreferences
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import cz.tipsport.rododendron.App

actual fun createPreferenceSettings(): Settings {
    val delegate: SharedPreferences = App.instance.getSharedPreferences(
        SETTINGS_SERVICE_NAME,
        Context.MODE_PRIVATE
    )
    return SharedPreferencesSettings(delegate)
}