package io

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings

@OptIn(ExperimentalSettingsImplementation::class)
actual fun createPreferenceSettings(): Settings {
    val serviceName: String = SETTINGS_SERVICE_NAME
    return KeychainSettings(serviceName)
}