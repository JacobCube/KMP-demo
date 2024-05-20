package ui.dashboard

import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: DashboardRepository = DashboardRepository()
): BaseViewModel() {

    private val _pastItems = MutableStateFlow<List<String>?>(null)

    /** last visited sections */
    val pastItems = _pastItems.asStateFlow()

    /** whether this app is in dark mode */
    val isDarkTheme
        get() = repository.isDarkTheme()

    /** Makes a new request to get all past visited items */
    fun requestPastItems() {
        viewModelScope.launch {
            _pastItems.value = repository.getPastItems()
        }
    }

    fun clearPastItems() {
        viewModelScope.launch {
            repository.clearPastItems()
            _pastItems.value = null
        }
    }

    /** sets theme of the app */
    fun setTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            repository.setTheme(isDarkMode)
        }
    }
}
