package ui.demo.episode

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import rododendron.composeapp.generated.resources.Res
import rododendron.composeapp.generated.resources.category_demo_characters
import rododendron.composeapp.generated.resources.episode_detail_air_date
import rododendron.composeapp.generated.resources.episode_detail_characters
import rododendron.composeapp.generated.resources.episode_detail_created
import rododendron.composeapp.generated.resources.episode_detail_episode
import rododendron.composeapp.generated.resources.episode_detail_name
import ui.base.BaseScreen
import ui.demo.character.DoubleTextLine

/**
 * Character detail screen
 * @param id id of the character
 * @param viewModel view model for the screen
 */
@Composable
fun EpisodeDetailScreen(
    id: Long,
    viewModel: EpisodeDetailViewModel = viewModel(EpisodeDetailViewModel::class, factory = viewModelFactory {
        initializer {
            EpisodeDetailViewModel()
        }
    })
) {
    val episodeDetail = viewModel.episodeDetail.collectAsState()

    val informationList = listOf(
        stringResource(Res.string.episode_detail_name) to episodeDetail.value?.name,
        stringResource(Res.string.episode_detail_air_date) to episodeDetail.value?.airDate,
        stringResource(Res.string.episode_detail_episode) to episodeDetail.value?.episode,
        stringResource(Res.string.episode_detail_characters) to episodeDetail.value?.characters?.joinToString(separator = ", "),
        stringResource(Res.string.episode_detail_created) to episodeDetail.value?.created
    )

    LaunchedEffect(Unit) {
        viewModel.requestCharacterDetail(id = id)
    }

    BaseScreen(
        title = episodeDetail.value?.name,
        subtitle = stringResource(Res.string.category_demo_characters)
    ) {
        if(episodeDetail.value == null) {
            //TODO shimmer
        }else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(32.dp))
                informationList.forEach {
                    DoubleTextLine(
                        title = it.first,
                        value = it.second?.ifBlank { "-" } ?: "-"
                    )
                }
            }
        }
    }
}


class EpisodeDetailViewModel(
    private val repository: EpisodeDetailRepository = EpisodeDetailRepository()
): BaseViewModel() {

    private val _episodeDetail = MutableStateFlow<RestApiNode.Demo.Episodes.EpisodeItem?>(null)

    /** character detail */
    val episodeDetail = _episodeDetail.asStateFlow()

    /** Makes a new request to detail of a character */
    fun requestCharacterDetail(id: Long) {
        viewModelScope.launch {
            _episodeDetail.value = repository.getEpisodeDetail(id = id)
        }
    }
}

class EpisodeDetailRepository: BaseRepository() {

    suspend fun getEpisodeDetail(id: Long): RestApiNode.Demo.Episodes.EpisodeItem? {
        return withContext(Dispatchers.IO) {
            httpClient.get(RestApiNode.Demo.Episodes.path + id) {
                contentType(ContentType.Application.Json)
            }.body()
        }
    }
}