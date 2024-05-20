package ui.demo.location

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
import rododendron.composeapp.generated.resources.location_detail_created
import rododendron.composeapp.generated.resources.location_detail_dimension
import rododendron.composeapp.generated.resources.location_detail_name
import rododendron.composeapp.generated.resources.location_detail_residents
import rododendron.composeapp.generated.resources.location_detail_type
import ui.base.BaseScreen
import ui.demo.character.DoubleTextLine

/**
 * Character detail screen
 * @param id id of the character
 * @param viewModel view model for the screen
 */
@Composable
fun LocationDetailScreen(
    id: Long,
    viewModel: LocationDetailViewModel = viewModel(LocationDetailViewModel::class, factory = viewModelFactory {
        initializer {
            LocationDetailViewModel()
        }
    })
) {
    val locationDetail = viewModel.locationDetail.collectAsState()

    val informationList = listOf(
        stringResource(Res.string.location_detail_name) to locationDetail.value?.name,
        stringResource(Res.string.location_detail_type) to locationDetail.value?.type,
        stringResource(Res.string.location_detail_dimension) to locationDetail.value?.dimension,
        stringResource(Res.string.location_detail_residents) to locationDetail.value?.residents?.joinToString(separator = ", "),
        stringResource(Res.string.location_detail_created) to locationDetail.value?.created
    )

    LaunchedEffect(Unit) {
        viewModel.requestLocationDetail(id = id)
    }

    BaseScreen(
        title = locationDetail.value?.name,
        subtitle = stringResource(Res.string.category_demo_characters)
    ) {
        if(locationDetail.value == null) {
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


class LocationDetailViewModel(
    private val repository: LocationDetailRepository = LocationDetailRepository()
): BaseViewModel() {

    private val _locationDetail = MutableStateFlow<RestApiNode.Demo.Locations.LocationItem?>(null)

    /** location detail */
    val locationDetail = _locationDetail.asStateFlow()

    /** Makes a new request to detail of a character */
    fun requestLocationDetail(id: Long) {
        viewModelScope.launch {
            _locationDetail.value = repository.getLocationDetail(id = id)
        }
    }
}

class LocationDetailRepository: BaseRepository() {

    suspend fun getLocationDetail(id: Long): RestApiNode.Demo.Locations.LocationItem? {
        return withContext(Dispatchers.IO) {
            httpClient.get(RestApiNode.Demo.Locations.path + id) {
                contentType(ContentType.Application.Json)
            }.body()
        }
    }
}