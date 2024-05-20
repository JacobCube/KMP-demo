import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import navigation.WorkerViewModel
import ui.casino.VendorsViewModel
import ui.dashboard.DashboardViewModel
import ui.demo.character.CharactersViewModel
import ui.demo.episode.EpisodesViewModel
import ui.demo.location.LocationsViewModel
import kotlin.reflect.KClass

fun MainViewController() =  ComposeUIViewController {
    CompositionLocalProvider(
        LocalViewModelStoreOwner provides rememberComposeViewModelStoreOwner(),
        content = {
            App(navController = rememberNavController())
        }
    )
}

//hotfix for lack of Kotlin reflection
//The ViewModel class works out of the box only for Android and desktop,
// where objects of the needed class can be created through class reflection.
// For iOS and web targets, you have to implement factories that explicitly create new ViewModel instances.
// for more see https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-lifecycle.html#viewmodel-implementation
private class ComposeViewModelStoreOwner : ViewModelStoreOwner, HasDefaultViewModelProviderFactory {
    override val viewModelStore = ViewModelStore()

    /** disposes of viewmodel */
    fun dispose() { viewModelStore.clear() }

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
                return when(modelClass) {
                    WorkerViewModel::class -> WorkerViewModel()
                    CharactersViewModel::class -> CharactersViewModel()
                    VendorsViewModel::class -> VendorsViewModel()
                    DashboardViewModel::class -> DashboardViewModel()
                    EpisodesViewModel::class -> EpisodesViewModel()
                    LocationsViewModel::class -> LocationsViewModel()
                    else -> {}
                } as T
            }
        }
}

@Composable
private fun rememberComposeViewModelStoreOwner(): ViewModelStoreOwner {
    val viewModelStoreOwner = remember {
        ComposeViewModelStoreOwner()
    }
    DisposableEffect(viewModelStoreOwner) {
        onDispose { viewModelStoreOwner.dispose() }
    }
    return viewModelStoreOwner
}