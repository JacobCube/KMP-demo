package navigation

import LocalNavController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import base.BaseRepository
import base.BaseViewModel
import io.IO
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import raw.emptyLottieJson
import rododendron.composeapp.generated.resources.Res
import rododendron.composeapp.generated.resources.exception_empty_navigation_description
import rododendron.composeapp.generated.resources.exception_empty_navigation_title
import ui.base.BaseScreen
import ui.casino.CasinoGamesListScreen
import ui.casino.CasinoVendorsScreen
import ui.components.ExceptionLayout
import ui.dashboard.DashboardRepository.Companion.PAST_ITEMS_KEY
import ui.dashboard.DashboardScreen
import ui.demo.character.CharacterDetailScreen
import ui.demo.character.CharactersFilterScreen
import ui.demo.character.CharactersListScreen
import ui.demo.episode.EpisodeDetailScreen
import ui.demo.episode.EpisodesListScreen
import ui.demo.location.LocationDetailScreen
import ui.demo.location.LocationsListScreen

val DEFAULT_START_DESTINATION = NavigationNode.Dashboard.route

/** Container with main navigation tree */
@Composable
fun NavigationContainer(
    modifier: Modifier = Modifier,
    startDestination: String? = null,
    viewModel: WorkerViewModel = viewModel(WorkerViewModel::class)
) {
    val navController = LocalNavController.current
    val currentEntry = navController?.currentBackStackEntryFlow?.collectAsState(null)

    currentEntry?.value?.getRouteWithArguments()?.let { currentRoute ->
        if(currentRoute != DEFAULT_START_DESTINATION) {
            LaunchedEffect(Unit) {
                viewModel.putPastItem(currentRoute)
            }
        }
    }

    if(navController == null) return

    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = startDestination ?: DEFAULT_START_DESTINATION
    ) {
        composable(NavigationNode.Dashboard.route) {
            DashboardScreen()
        }
        composable(NavigationNode.Casino.Games.List.route) {
            CasinoGamesListScreen()
        }
        composable(NavigationNode.Casino.JackpotBanners.Vendors.route) {
            CasinoVendorsScreen()
        }
        composable(NavigationNode.Demo.Characters.route) {
            CharactersListScreen()
        }
        composable(NavigationNode.Demo.Locations.route) {
            LocationsListScreen()
        }
        composable(NavigationNode.Demo.Episodes.route) {
            EpisodesListScreen()
        }
        composable(
            NavigationNode.Demo.Characters.CharactersFilter.route,
            arguments = NavigationNode.Demo.Characters.CharactersFilter.navArguments
        ) { navBackStackEntry ->
            CharactersFilterScreen(
                defaultName = navBackStackEntry.arguments?.getString(NavigationArguments.CHARACTER_FILTER_NAME),
                defaultStatus = navBackStackEntry.arguments?.getString(NavigationArguments.CHARACTER_FILTER_STATUS),
                defaultSpecies = navBackStackEntry.arguments?.getString(NavigationArguments.CHARACTER_FILTER_SPECIES),
                defaultType = navBackStackEntry.arguments?.getString(NavigationArguments.CHARACTER_FILTER_TYPE),
                defaultGender = navBackStackEntry.arguments?.getString(NavigationArguments.CHARACTER_FILTER_GENDER)
            )
        }
        composable(
            NavigationNode.Demo.Episodes.EpisodeDetail.route,
            arguments = NavigationNode.Demo.Episodes.EpisodeDetail.navArguments
        ) { navBackStackEntry ->
            EpisodeDetailScreen(
                id = navBackStackEntry.arguments?.getString(NavigationArguments.ARG_ID)?.toLongOrNull() ?: 0
            )
        }
        composable(
            NavigationNode.Demo.Characters.CharactersDetail.route,
            arguments = NavigationNode.Demo.Characters.CharactersDetail.navArguments
        ) { navBackStackEntry ->
            CharacterDetailScreen(
                id = navBackStackEntry.arguments?.getString(NavigationArguments.ARG_ID)?.toLongOrNull() ?: 0
            )
        }
        composable(
            NavigationNode.Demo.Locations.LocationDetail.route,
            arguments = NavigationNode.Demo.Locations.LocationDetail.navArguments
        ) { navBackStackEntry ->
            LocationDetailScreen(
                id = navBackStackEntry.arguments?.getString(NavigationArguments.ARG_ID)?.toLongOrNull() ?: 0
            )
        }
        composable("/") {
            BaseScreen {
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.JsonString(emptyLottieJson)
                )

                ExceptionLayout(
                    modifier = Modifier.fillMaxWidth(),
                    composition = composition,
                    title = stringResource(Res.string.exception_empty_navigation_title),
                    description = stringResource(Res.string.exception_empty_navigation_description),
                )
            }
        }
    }
}

/** returns a route from an entry including its current arguments */
fun NavBackStackEntry.getRouteWithArguments(): String? {
    var route = destination.route
    arguments?.keySet()?.forEach { key ->
        arguments?.getString(key)?.let { value ->
            route = route?.replaceFirst("{$key}", value)
        }
    }
    return route
}

@Composable
fun <T>NavController?.collectFlow(
    key: String,
    defaultValue: T,
    listener: (T) -> Unit
) {
    LaunchedEffect(Unit) {
        this@collectFlow?.currentBackStackEntry
            ?.savedStateHandle
            ?.run {
                getStateFlow(key, defaultValue).collectLatest {
                    if (it != null) listener(it)
                }
            }
    }
}


class WorkerViewModel(
    private val repository: WorkerRepository = WorkerRepository()
): BaseViewModel() {

    /** puts a new past item into the stack */
    fun putPastItem(value: String) {
        if(value.isNotBlank()) {
            viewModelScope.launch {
                repository.putPastItem(value)
            }
        }
    }
}

class WorkerRepository: BaseRepository() {

    suspend fun putPastItem(value: String) {
        return withContext(Dispatchers.IO) {
            settings.putString(
                PAST_ITEMS_KEY,
                settings.getStringOrNull(PAST_ITEMS_KEY)?.split(",")
                    .orEmpty()
                    .toMutableList()
                    .apply {
                        remove(value)
                        add(0, value)
                    }
                    .joinToString(",")
            )
        }
    }
}