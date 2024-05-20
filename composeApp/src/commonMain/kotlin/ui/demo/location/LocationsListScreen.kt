package ui.demo.location

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import navigation.NavigationArguments
import navigation.NavigationNode
import org.jetbrains.compose.resources.stringResource
import rododendron.composeapp.generated.resources.Res
import rododendron.composeapp.generated.resources.category_demo_locations
import theme.LocalTheme
import ui.ClickableTextButton
import ui.base.BaseScreen

/** List of all locations */
@Composable
fun LocationsListScreen(
    viewModel: LocationsViewModel = viewModel(LocationsViewModel::class, factory = viewModelFactory {
        initializer {
            LocationsViewModel()
        }
    })
) {
    val pageSize = LocalDefaultPageSize.current
    val navController = LocalNavController.current

    val locations = viewModel.locations.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.requestAllLocations()
    }

    BaseScreen(
        title = stringResource(Res.string.category_demo_locations)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(
                locations.value ?: arrayOfNulls<RestApiNode.Demo.Locations.LocationItem>(pageSize).toList(),
                key = { index, item -> item?.url ?: index.toString() }
            ) { _, item ->
                ClickableTextButton(
                    text = item?.name ?: "",
                    suffixVector = if(item?.url != null) Icons.AutoMirrored.Outlined.ArrowForwardIos else null,
                    onClick = {
                        navController?.navigate(
                            NavigationNode.Demo.Locations.LocationDetail.createRoute(
                                NavigationArguments.ARG_ID to item?.id.toString()
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


class LocationsViewModel(
    private val repository: LocationsRepository = LocationsRepository()
): BaseViewModel() {

    private val _locations = MutableStateFlow<List<RestApiNode.Demo.Locations.LocationItem>?>(null)

    /** list of all locations */
    val locations = _locations.asStateFlow()

    /** Makes a new request to get all pokemon */
    fun requestAllLocations(page: Int = 1) {
        viewModelScope.launch {
            _locations.value = repository.getAllLocations(page = page)?.results
        }
    }
}

class LocationsRepository: BaseRepository() {

    suspend fun getAllLocations(page: Int = 1): RestApiNode.Demo.Locations.LocationsList? {
        return withContext(Dispatchers.IO) {
            httpClient.get(RestApiNode.Demo.Locations.path) {
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("page", page.toString())
                }
            }.body()
        }
    }
}