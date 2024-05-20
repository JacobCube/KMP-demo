package ui.dashboard

import base.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DashboardRepository: BaseRepository() {

    companion object {
        const val PAST_ITEMS_KEY = "past_items_key"
        private const val IS_DARK_THEME_KEY = "is_dark_theme_key"
    }

    suspend fun getPastItems(): List<String>? {
        return withContext(Dispatchers.Default) {
            settings.getStringOrNull(PAST_ITEMS_KEY)?.split(",")
                ?.filter { it.isNotBlank() }
        }
    }

    suspend fun clearPastItems() {
        return withContext(Dispatchers.Default) {
            settings.remove(PAST_ITEMS_KEY)
        }
    }

    /** @return whether app is in dark theme */
    fun isDarkTheme(): Boolean? = settings.getBooleanOrNull(IS_DARK_THEME_KEY)

    /** sets theme of the app */
    suspend fun setTheme(isDarkTheme: Boolean) {
        withContext(Dispatchers.Default) {
            settings.putBoolean(IS_DARK_THEME_KEY, isDarkTheme)
        }
    }
}