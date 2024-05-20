package ui.demo.episode

import LocalDefaultPageSize
import LocalNavController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import base.BaseRepository
import base.BaseViewModel
import io.IO
import io.RestApiNode
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.fullPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import navigation.NavigationArguments.ARG_ID
import navigation.NavigationNode
import org.jetbrains.compose.resources.stringResource
import rododendron.composeapp.generated.resources.Res
import rododendron.composeapp.generated.resources.category_demo_episodes
import theme.LocalTheme
import ui.ClickableTextButton
import ui.base.BaseScreen

/** List of all episodes */
@Composable
fun EpisodesListScreen(
    viewModel: EpisodesViewModel = viewModel(EpisodesViewModel::class, factory = viewModelFactory {
        initializer {
            EpisodesViewModel()
        }
    })
) {
    val pageSize = LocalDefaultPageSize.current
    val navController = LocalNavController.current

    val episodes = viewModel.episodes.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.requestAllEpisodes()
    }

    BaseScreen(
        title = stringResource(Res.string.category_demo_episodes)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(
                episodes.value ?: arrayOfNulls<RestApiNode.Demo.Episodes.EpisodeItem>(pageSize).toList(),
                key = { index, item -> item?.url ?: index.toString() }
            ) { _, item ->
                ClickableTextButton(
                    text = item?.name ?: "",
                    suffixVector = if(item?.url != null) Icons.AutoMirrored.Outlined.ArrowForwardIos else null,
                    onClick = {
                        navController?.navigate(
                            NavigationNode.Demo.Episodes.EpisodeDetail.createRoute(
                                ARG_ID to item?.id.toString()
                            )
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        color = LocalTheme.current.colors.primary
                    )
                )
            }
        }
    }
}


class EpisodesViewModel(
    private val repository: EpisodesRepository = EpisodesRepository()
): BaseViewModel() {

    private val _episodes = MutableStateFlow<List<RestApiNode.Demo.Episodes.EpisodeItem>?>(null)

    /** list of all episodes */
    val episodes = _episodes.asStateFlow()

    /** Makes a new request to get all pokemon */
    fun requestAllEpisodes(page: Int = 1) {
        viewModelScope.launch {
            _episodes.value = repository.getAllEpisodes(page = page)?.results
        }
    }
}

class EpisodesRepository: BaseRepository() {

    suspend fun getAllEpisodes(page: Int = 1): RestApiNode.Demo.Episodes.EpisodesList? {
        return withContext(Dispatchers.IO) {
            httpClient.get(RestApiNode.Demo.Episodes.path) {
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("page", page.toString())
                    println(it.build().fullPath)
                }
            }.body()
        }
    }
}