package ui.desktop

import LocalNavController
import LocalOnBackPress
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import navigation.AppBarCategories
import navigation.CategoryNode
import navigation.DEFAULT_START_DESTINATION
import navigation.NavigationNode
import org.jetbrains.compose.resources.stringResource
import theme.LocalTheme
import theme.TipsportTheme
import ui.ClickableTextButton
import ui.ImageButton
import ui.SearchBar
import ui.components.ThemeSwitch
import ui.dashboard.DashboardViewModel

/** Desktop container for the whole application */
@Composable
fun DesktopContainer(
    viewModel: DashboardViewModel = viewModel(DashboardViewModel::class, factory = viewModelFactory {
        initializer {
            DashboardViewModel()
        }
    }),
    title: String? = null,
    subtitle: String? = null,
    content: @Composable () -> Unit
) {
    val isDefaultDarkTheme = isSystemInDarkTheme()
    val isDarkTheme = remember {
        mutableStateOf(viewModel.isDarkTheme ?: isDefaultDarkTheme)
    }

    val showSearchBar = remember { mutableStateOf(false) }
    val mainFocusRequester = remember { FocusRequester() }

    val shiftKeyScope = rememberCoroutineScope()

    // ten shift key se aktivuje 2x za sebou, pocitame tedy pocet, ne boolean
    var shiftKeyEvents by remember { mutableStateOf(0) }

    TipsportTheme(
        isDarkTheme = isDarkTheme.value
    ) {
        Box(
            modifier = Modifier
                .focusRequester(mainFocusRequester)
                .fillMaxSize()
                .onKeyEvent {
                    when(it.key) {
                        Key.ShiftLeft -> {
                            shiftKeyEvents++
                            if(shiftKeyEvents > 2) {
                                showSearchBar.value = true
                                shiftKeyEvents = 0
                                true
                            }else {
                                shiftKeyScope.coroutineContext.cancelChildren()
                                shiftKeyScope.launch {
                                    delay(500)
                                    shiftKeyEvents = 0
                                }
                                false
                            }
                        }
                        Key.Escape -> {
                            showSearchBar.value = false
                            true
                        }
                        else -> false
                    }
                }
        ) {
            if(showSearchBar.value) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight(0.4f)
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                ) {
                    Popup(
                        onDismissRequest = {
                            showSearchBar.value = false
                        },
                        alignment = Alignment.Center
                    ) {
                        SearchBar(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth(0.6f)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .background(color = LocalTheme.current.colors.backgroundDarker)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .shadow(4.dp)
                        .background(color = LocalTheme.current.colors.backgroundLighter)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopStart
                ) {
                    LazyRow {
                        item {
                            ImageButton(
                                modifier = Modifier
                                    .height(IntrinsicSize.Max)
                                    .widthIn(min = 48.dp),
                                imageVector = Icons.Outlined.Search,
                                onClick = {
                                    showSearchBar.value = true
                                }
                            )
                        }
                        items(AppBarCategories.entries.toTypedArray()) { category ->
                            category.titleRes.let { categoryName ->
                                val showDropDown = remember { mutableStateOf(false) }
                                val navController = LocalNavController.current

                                if(categoryName != null) {
                                    Column {
                                        ClickableTextButton(text = stringResource(categoryName)) {
                                            if(category.children.isNotEmpty()) {
                                                showDropDown.value = true
                                            }else {
                                                category.navigationNode?.let { node ->
                                                    showDropDown.value = false
                                                    navController?.navigate(node.createRoute())
                                                }
                                            }
                                        }
                                        if(showDropDown.value) {
                                            Box {
                                                NestedChildrenPopUp(
                                                    children = category.children,
                                                    onDismissRequest = {
                                                        showDropDown.value = false
                                                    },
                                                    onClick = { node ->
                                                        showDropDown.value = false
                                                        navController?.navigate(node.createRoute())
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            ThemeSwitch(
                                onChange = { isChecked ->
                                    isDarkTheme.value = isChecked
                                    viewModel.setTheme(isChecked)
                                },
                                isChecked = isDarkTheme
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .heightIn(min = 40.dp)
                            .padding(start = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val localOnBackPress = LocalOnBackPress.current

                        val navController = LocalNavController.current
                        val currentEntry = navController?.currentBackStackEntryFlow?.collectAsState(null)

                        androidx.compose.animation.AnimatedVisibility(
                            visible = currentEntry?.value?.destination?.route != DEFAULT_START_DESTINATION
                        ) {
                            ImageButton(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBackIos,
                                onClick = {
                                    localOnBackPress?.invoke() ?: navController?.popBackStack()
                                }
                            )
                        }

                        SelectionContainer {
                            Text(
                                text = title ?: "",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = LocalTheme.current.colors.secondary
                                )
                            )
                        }
                    }
                    content()
                }
            }
        }
    }
}

/**
 * Popup displaying menu nested children within navigation
 */
@Composable
private fun NestedChildrenPopUp(
    children: List<CategoryNode>,
    onDismissRequest: (() -> Unit)? = null,
    onClick: (NavigationNode) -> Unit
) {
    Popup(
        onDismissRequest = onDismissRequest
    ) {
        Row {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    contentColor = LocalTheme.current.colors.backgroundLighter,
                    containerColor = LocalTheme.current.colors.backgroundLighter
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                val maxWidth = remember { mutableStateOf(0.dp) }
                val showNestedDropDownIndex = remember { mutableStateOf(-1) }

                LazyColumn(
                    modifier = Modifier.onGloballyPositioned {
                        maxWidth.value = it.size.width.dp
                    }
                ) {
                    itemsIndexed(children) { index, nestedNode ->
                        Row {
                            val title = nestedNode.titleRes ?: nestedNode.navigationNode?.titleRes

                            ClickableTextButton(
                                modifier = Modifier.widthIn(
                                    min = if(maxWidth.value == 0.dp) Dp.Unspecified else maxWidth.value
                                ),
                                text = if(title != null) stringResource(title) else "",
                                suffixVector = if(nestedNode.children.isNotEmpty()) {
                                    Icons.AutoMirrored.Outlined.ArrowForwardIos
                                }else null,
                                verticalPadding = 8.dp
                            ) {
                                if(nestedNode.children.isNotEmpty()) {
                                    showNestedDropDownIndex.value = index
                                }else {
                                    nestedNode.navigationNode?.let { node ->
                                        onClick(node)
                                    }
                                }
                            }
                            if(showNestedDropDownIndex.value == index) {
                                Box {
                                    NestedChildrenPopUp(
                                        children = nestedNode.children,
                                        onClick = onClick
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}