package ui.demo.character

import LocalDefaultPageSize
import LocalDeviceType
import LocalNavController
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
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
import io.PagingInfo
import io.RestApiNode
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import navigation.NavigationArguments
import navigation.NavigationArguments.CHARACTER_FILTER_GENDER
import navigation.NavigationArguments.CHARACTER_FILTER_NAME
import navigation.NavigationArguments.CHARACTER_FILTER_SPECIES
import navigation.NavigationArguments.CHARACTER_FILTER_STATUS
import navigation.NavigationArguments.CHARACTER_FILTER_TYPE
import navigation.NavigationNode
import navigation.collectFlow
import org.jetbrains.compose.resources.stringResource
import raw.emptyLottieJson
import rododendron.composeapp.generated.resources.Res
import rododendron.composeapp.generated.resources.category_demo_characters
import rododendron.composeapp.generated.resources.characters_list_filter
import rododendron.composeapp.generated.resources.characters_list_page
import rododendron.composeapp.generated.resources.exception_empty_navigation_description
import rododendron.composeapp.generated.resources.exception_empty_navigation_title
import theme.LocalTheme
import ui.ClickableTextButton
import ui.base.BaseScreen
import ui.components.BrandChip
import ui.components.ExceptionLayout
import ui.components.extensions.brandShimmerEffect

const val DATA_REQUEST_DELAY = 50L

/** List of all characters */
@Composable
fun CharactersListScreen(
    viewModel: CharactersViewModel = viewModel(CharactersViewModel::class, factory = viewModelFactory {
        initializer {
            CharactersViewModel()
        }
    })
) {
    val pageSize = LocalDefaultPageSize.current
    val navController = LocalNavController.current
    val deviceType = LocalDeviceType.current

    val requestScope = rememberCoroutineScope()

    val characters = viewModel.characters.collectAsState()

    val currentPage = rememberSaveable { mutableStateOf(1) }
    val name = rememberSaveable { mutableStateOf<String?>(null) }
    val status = rememberSaveable { mutableStateOf<String?>(null) }
    val species = rememberSaveable { mutableStateOf<String?>(null) }
    val type = rememberSaveable { mutableStateOf<String?>(null) }
    val gender = rememberSaveable { mutableStateOf<String?>(null) }

    val isFilterVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val shownDetailId = rememberSaveable {
        mutableStateOf<Long?>(null)
    }

    LaunchedEffect(
        name.value,
        status.value,
        species.value,
        type.value,
        gender.value,
        currentPage.value
    ) {
        requestScope.coroutineContext.cancelChildren()
        requestScope.launch {
            delay(DATA_REQUEST_DELAY)
            viewModel.requestAllCharacters(
                page = currentPage.value,
                name = name.value,
                status = status.value,
                species = species.value,
                type = type.value,
                gender = gender.value
            )
        }
    }

    LaunchedEffect(deviceType) {
        if(deviceType == WindowWidthSizeClass.Compact && (isFilterVisible.value || shownDetailId.value != null)) {
            isFilterVisible.value = false
            shownDetailId.value = null
        }
    }

    navController?.collectFlow(
        CHARACTER_FILTER_NAME,
        name.value
    ) {
        name.value = it
    }
    navController?.collectFlow(
        CHARACTER_FILTER_STATUS,
        status.value
    ) {
        status.value = it
    }
    navController?.collectFlow(
        CHARACTER_FILTER_SPECIES,
        species.value
    ) {
        species.value = it
    }
    navController?.collectFlow(
        CHARACTER_FILTER_TYPE,
        type.value
    ) {
        type.value = it
    }
    navController?.collectFlow(
        CHARACTER_FILTER_GENDER,
        gender.value
    ) {
        gender.value = it
    }

    BaseScreen(
        title = stringResource(Res.string.category_demo_characters)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ClickableTextButton(
                    suffixVector = Icons.Outlined.FilterList,
                    text = stringResource(Res.string.characters_list_filter)
                ) {
                    if(deviceType == WindowWidthSizeClass.Compact) {
                        val route =  NavigationNode.Demo.Characters.CharactersFilter.createRoute(
                            CHARACTER_FILTER_NAME to name.value,
                            CHARACTER_FILTER_STATUS to status.value,
                            CHARACTER_FILTER_SPECIES to species.value,
                            CHARACTER_FILTER_TYPE to type.value,
                            CHARACTER_FILTER_GENDER to gender.value
                        )
                        navController?.navigate(
                            route
                        )
                    }else {
                        if(shownDetailId.value != null) {
                            shownDetailId.value = null
                            isFilterVisible.value = true
                        }else {
                            isFilterVisible.value = isFilterVisible.value.not()
                        }
                    }
                }

                Spacer(Modifier.width(16.dp))
                Image(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                        .clickable {
                            currentPage.value = (currentPage.value - 1).coerceAtLeast(1)
                        },
                    contentDescription = "previous page",
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBackIos,
                    colorFilter = ColorFilter.tint(color = LocalTheme.current.colors.primary)
                )
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = stringResource(
                        Res.string.characters_list_page,
                        currentPage.value
                    ),
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = LocalTheme.current.colors.primary
                    )
                )
                Image(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                        .clickable {
                            if((characters.value?.size ?: 0) > 0) {
                                currentPage.value += 1
                            }
                        },
                    contentDescription = "next page",
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                    colorFilter = ColorFilter.tint(color = LocalTheme.current.colors.primary)
                )
                if(name.value.isNullOrBlank().not()) {
                    BrandChip(
                        modifier = Modifier.padding(4.dp),
                        chipText = name.value ?: "",
                        selected = true,
                        onClick = {
                            name.value = null
                        }
                    )
                }
                if(status.value.isNullOrBlank().not()) {
                    BrandChip(
                        modifier = Modifier.padding(4.dp),
                        chipText = status.value ?: "",
                        selected = true,
                        onClick = {
                            status.value = null
                        }
                    )
                }
                if(species.value.isNullOrBlank().not()) {
                    BrandChip(
                        modifier = Modifier.padding(4.dp),
                        chipText = species.value ?: "",
                        selected = true,
                        onClick = {
                            species.value = null
                        }
                    )
                }
                if(type.value.isNullOrBlank().not()) {
                    BrandChip(
                        modifier = Modifier.padding(4.dp),
                        chipText = type.value ?: "",
                        selected = true,
                        onClick = {
                            type.value = null
                        }
                    )
                }
                if(gender.value.isNullOrBlank().not()) {
                    BrandChip(
                        modifier = Modifier.padding(4.dp),
                        chipText = gender.value ?: "",
                        selected = true,
                        onClick = {
                            gender.value = null
                        }
                    )
                }
            }
            Row {
                Crossfade(
                    modifier = Modifier.weight(1f),
                    targetState = characters.value?.size == 0
                ) { isEmpty ->
                    if(isEmpty) {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.JsonString(emptyLottieJson)
                        )

                        ExceptionLayout(
                            modifier = Modifier.fillMaxSize(),
                            composition = composition,
                            title = stringResource(Res.string.exception_empty_navigation_title),
                            description = stringResource(Res.string.exception_empty_navigation_description),
                        )
                    }else {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(100.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(
                                items = characters.value ?: arrayOfNulls<RestApiNode.Demo.Characters.CharacterItem>(pageSize).toList(),
                                key = { index, item -> item?.id ?: index.toString() }
                            ) { _, item ->
                                CharacterTile(
                                    data = item,
                                    onClick = {
                                        if(deviceType == WindowWidthSizeClass.Compact) {
                                            navController?.navigate(
                                                NavigationNode.Demo.Characters.CharactersDetail.createRoute(
                                                    NavigationArguments.ARG_ID to (item?.id?.toString() ?: "")
                                                )
                                            )
                                        }else {
                                            shownDetailId.value = item?.id
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
                Crossfade(
                    modifier = Modifier
                        .animateContentSize()
                        .then(if(isFilterVisible.value || shownDetailId.value != null) Modifier.weight(1f) else Modifier),
                    targetState = shownDetailId.value != null
                ) { hasDetail ->
                    if(hasDetail) {
                        shownDetailId.value?.let { id ->
                            Column(
                                modifier = Modifier.background(color = LocalTheme.current.colors.backgroundLighter),
                                horizontalAlignment = Alignment.End
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .padding(end = 8.dp, top = 4.dp)
                                        .clip(CircleShape)
                                        .size(32.dp)
                                        .clickable {
                                            shownDetailId.value = null
                                        },
                                    imageVector = Icons.Outlined.Close,
                                    tint = LocalTheme.current.colors.primary,
                                    contentDescription = ""
                                )
                                CharacterDetailContent(id = id, modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }else {
                        AnimatedVisibility(visible = isFilterVisible.value) {
                            CharactersFilterContent(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                                    .background(
                                        color = LocalTheme.current.colors.backgroundLighter,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                name = name,
                                status = status,
                                type = type,
                                species = species,
                                gender = gender
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterTile(
    modifier: Modifier = Modifier,
    data: RestApiNode.Demo.Characters.CharacterItem? = null,
    onClick: () -> Unit
) {
    if(data == null) {
        Box(modifier = Modifier.size(100.dp).brandShimmerEffect())
    }else {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = modifier
                    .requiredSize(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onClick),
                contentScale = ContentScale.Crop,
                model = data.image,
                contentDescription = data.name
            )
            Text(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = data.name ?: "",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = LocalTheme.current.colors.primary
                )
            )
        }
    }
}

class CharactersViewModel(
    private val repository: CharactersListRepository = CharactersListRepository()
): BaseViewModel() {

    private val _characters = MutableStateFlow<List<RestApiNode.Demo.Characters.CharacterItem>?>(null)

    /** list of all characters */
    val characters = _characters.asStateFlow()

    /** Makes a new request to get all pokemon */
    fun requestAllCharacters(
        page: Int = 1,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        type: String? = null,
        gender: String? = null
    ) {
        viewModelScope.launch {
            _characters.value = repository.getAllCharacters(
                page = page,
                name = name,
                status = status,
                species = species,
                type = type,
                gender = gender
            ).results ?: listOf()
        }
    }
}

class CharactersListRepository: BaseRepository() {

    suspend fun getAllCharacters(
        page: Int = 1,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        type: String? = null,
        gender: String? = null,
    ): RestApiNode.Demo.Characters.CharactersList {
        return withContext(Dispatchers.IO) {
            httpClient.get(RestApiNode.Demo.Characters.path) {
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("page", page.toString())
                    name?.let {
                        parameters.append("name", it)
                    }
                    status?.let {
                        parameters.append("status", it)
                    }
                    species?.let {
                        parameters.append("species", it)
                    }
                    type?.let {
                        parameters.append("type", it)
                    }
                    gender?.let {
                        parameters.append("gender", it)
                    }
                }
            }.body() ?: RestApiNode.Demo.Characters.CharactersList(
                info = PagingInfo(
                    count = 0,
                    pages = 0,
                    next = null,
                    prev = null
                ),
                results = listOf()
            )
        }
    }
}