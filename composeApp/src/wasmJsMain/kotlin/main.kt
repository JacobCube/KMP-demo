
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import kotlinx.browser.document
import kotlinx.browser.window
import navigation.WorkerViewModel
import navigation.getRouteWithArguments
import org.w3c.dom.COMPLETE
import org.w3c.dom.DocumentReadyState
import ui.casino.VendorsViewModel
import ui.dashboard.DashboardViewModel
import ui.demo.character.CharactersViewModel
import ui.demo.episode.EpisodesViewModel
import ui.demo.location.LocationsViewModel
import kotlin.reflect.KClass

/** Wasm (Web) app gets initialized here */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val lifecycle = LifecycleRegistry()

    // Attach the LifecycleRegistry to document
    lifecycle.attachToDocument()

    // Render the UI
    CanvasBasedWindow(
        title = "Rododendron",
        canvasElementId = "ComposeTarget",
    ) {
        CompositionLocalProvider(
            LocalViewModelStoreOwner provides rememberComposeViewModelStoreOwner(),
            content = {
                val navController = rememberNavController()
                val currentEntry = navController.currentBackStackEntryAsState()
                val currentRoute = currentEntry.value?.getRouteWithArguments() ?: window.location.search

                CompositionLocalProvider(
                    LocalOnBackPress provides {
                        window.history.go(-1)
                    }
                ) {
                    App(
                        navController = navController,
                        startDestination = if(window.location.search.contains("?/")) {
                            window.location.search.replace("?/", "/")
                        }else null
                    )
                }

                LaunchedEffect(currentEntry.value) {
                    if(currentEntry.value != null
                        && window.location.search.contains(currentRoute).not()
                    ) {
                        if(currentEntry.value?.destination?.route != navController.graph.startDestinationRoute) {
                            window.location.search = currentRoute
                        }
                    }
                }

                //https://developer.mozilla.org/en-US/docs/Web/API/Location
                LaunchedEffect(window.location.search) {
                    // navigate to correct route only if arguments are required, otherwise startDestination is sufficient
                    if(window.location.search.contains("?/")
                        && currentEntry.value?.arguments?.isEmpty() == false
                    ) {
                        navController.navigate(
                            route = window.location.search.replace("?/", "/")
                        )
                    }
                }
            }
        )
    }
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

// Attaches the LifecycleRegistry to the document
private fun LifecycleRegistry.attachToDocument() {
    fun onVisibilityChanged() {
        this.create()
        if (document.readyState == DocumentReadyState.Companion.COMPLETE) {
            resume()
        } else {
            stop()
        }
    }

    onVisibilityChanged()

    document.addEventListener(type = "visibilitychange", callback = { onVisibilityChanged() })
}