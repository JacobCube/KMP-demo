package ui.demo.character

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import base.BaseRepository
import base.BaseViewModel
import coil3.compose.AsyncImage
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
import rododendron.composeapp.generated.resources.character_detail_created
import rododendron.composeapp.generated.resources.character_detail_episodes
import rododendron.composeapp.generated.resources.character_detail_gender
import rododendron.composeapp.generated.resources.character_detail_location
import rododendron.composeapp.generated.resources.character_detail_name
import rododendron.composeapp.generated.resources.character_detail_origin
import rododendron.composeapp.generated.resources.character_detail_species
import rododendron.composeapp.generated.resources.character_detail_status
import rododendron.composeapp.generated.resources.character_detail_type
import theme.LocalTheme
import ui.base.BaseScreen

/**
 * Character detail screen
 * @param id id of the character
 * @param viewModel view model for the screen
 */
@Composable
fun CharacterDetailScreen(
    id: Long,
    viewModel: CharacterDetailViewModel = viewModel(CharacterDetailViewModel::class, factory = viewModelFactory {
        initializer {
            CharacterDetailViewModel()
        }
    })
) {
    val characterDetail = viewModel.characterDetail.collectAsState()

    BaseScreen(
        title = characterDetail.value?.name,
        subtitle = stringResource(Res.string.category_demo_characters)
    ) {
        CharacterDetailContent(
            id = id,
            viewModel = viewModel
        )
    }
}

@Composable
fun CharacterDetailContent(
    modifier: Modifier = Modifier,
    id: Long,
    viewModel: CharacterDetailViewModel = viewModel(CharacterDetailViewModel::class, factory = viewModelFactory {
        initializer {
            CharacterDetailViewModel()
        }
    })
) {
    val characterDetail = viewModel.characterDetail.collectAsState()

    val informationList = listOf(
        stringResource(Res.string.character_detail_name) to characterDetail.value?.name,
        stringResource(Res.string.character_detail_status) to characterDetail.value?.status,
        stringResource(Res.string.character_detail_species) to characterDetail.value?.species,
        stringResource(Res.string.character_detail_type) to characterDetail.value?.type,
        stringResource(Res.string.character_detail_gender) to characterDetail.value?.gender,
        stringResource(Res.string.character_detail_origin) to characterDetail.value?.origin?.name,
        stringResource(Res.string.character_detail_location) to characterDetail.value?.location?.name,
        stringResource(Res.string.character_detail_episodes) to characterDetail.value?.episode?.joinToString(separator = ", "),
        stringResource(Res.string.character_detail_created) to characterDetail.value?.created
    )

    LaunchedEffect(id) {
        viewModel.requestCharacterDetail(id = id)
    }

    if(characterDetail.value == null) {
        //TODO shimmer
    }else {
        characterDetail.value?.let { data ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight(0.3f)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    model = data.image,
                    contentDescription = characterDetail.value?.name
                )
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

@Composable
fun DoubleTextLine(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 14.sp,
                color = LocalTheme.current.colors.secondary
            )
        )
        Text(
            text = value,
            style = TextStyle(
                fontSize = 16.sp,
                color = LocalTheme.current.colors.primary
            )
        )
    }
}


class CharacterDetailViewModel(
    private val repository: CharactersDetailRepository = CharactersDetailRepository()
): BaseViewModel() {

    private val _characterDetail = MutableStateFlow<RestApiNode.Demo.Characters.CharacterItem?>(null)

    /** character detail */
    val characterDetail = _characterDetail.asStateFlow()

    /** Makes a new request to detail of a character */
    fun requestCharacterDetail(id: Long) {
        viewModelScope.launch {
            _characterDetail.value = repository.getCharacterDetail(id = id)
        }
    }
}

class CharactersDetailRepository: BaseRepository() {

    suspend fun getCharacterDetail(id: Long): RestApiNode.Demo.Characters.CharacterItem? {
        return withContext(Dispatchers.IO) {
            httpClient.get(RestApiNode.Demo.Characters.path + id) {
                contentType(ContentType.Application.Json)
            }.body()
        }
    }
}