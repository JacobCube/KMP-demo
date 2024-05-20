package io

import com.russhwolf.settings.PropertiesSettings
import com.russhwolf.settings.Settings
import java.util.Properties

actual fun createPreferenceSettings(): Settings {
    val delegate = Properties()
    return PropertiesSettings(delegate)
}