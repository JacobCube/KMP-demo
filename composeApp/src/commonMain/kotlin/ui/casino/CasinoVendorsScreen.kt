package ui.casino

import LocalDefaultPageSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
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
import rododendron.composeapp.generated.resources.category_casino_jackpot_banner_vendors
import theme.LocalTheme
import ui.base.BaseScreen

/** Main screen for casino vendors */
@Composable
fun CasinoVendorsScreen(viewModel: VendorsViewModel = remember { VendorsViewModel() }) {
    val pageSize = LocalDefaultPageSize.current

    val vendors = viewModel.vendors.collectAsState()

    LaunchedEffect(Unit) {
    }
    viewModel.requestAllVendors()

    BaseScreen(
        title = stringResource(Res.string.category_casino_jackpot_banner_vendors)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(
                vendors.value ?: arrayOfNulls<RestApiNode.Casino.Vendors.VendorsIO>(pageSize).toList(),
                key = { index, item -> item?.id ?: index }
            ) { _, item ->
                Text(
                    item?.name ?: "",
                    style = TextStyle(
                        fontSize = 40.sp,
                        color = LocalTheme.current.colors.primary
                    )
                )
            }
        }
    }
}

class VendorsViewModel(
    private val repository: VendorsRepository = VendorsRepository()
): BaseViewModel() {

    private val _vendors = MutableStateFlow<List<RestApiNode.Casino.Vendors.VendorsIO>?>(null)

    /** list of all vendors */
    val vendors = _vendors.asStateFlow()

    /** Makes a new request to get all vendors */
    fun requestAllVendors() {
        viewModelScope.launch {
            _vendors.value = repository.getAllVendors()
        }
    }
}

class VendorsRepository: BaseRepository() {

    suspend fun getAllVendors(): List<RestApiNode.Casino.Vendors.VendorsIO>? {
        return withContext(Dispatchers.IO) {
            httpClient.get(RestApiNode.Casino.Vendors.path) {
                contentType(ContentType.Application.Json)
            }.body()
        }
    }
}