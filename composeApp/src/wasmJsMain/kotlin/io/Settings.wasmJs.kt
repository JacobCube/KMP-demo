package io

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings

actual fun createPreferenceSettings(): Settings = StorageSettings()