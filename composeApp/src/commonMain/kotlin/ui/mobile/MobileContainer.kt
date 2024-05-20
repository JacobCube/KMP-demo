package ui.mobile

import LocalNavController
import LocalOnBackPress
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.LunchDining
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobileContainer(
    viewModel: DashboardViewModel = remember { DashboardViewModel() },
    title: String? = null,
    subtitle: String? = null,
    content: @Composable () -> Unit
) {
    val isDefaultDarkTheme = isSystemInDarkTheme()
    val isDarkTheme = remember {
        mutableStateOf(viewModel.isDarkTheme ?: isDefaultDarkTheme)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current

    TipsportTheme(
        isDarkTheme = isDarkTheme.value
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        ModalNavigationDrawerContent(
                            onNavigate = { node ->
                                coroutineScope.launch {
                                    drawerState.close()
                                }
                                navController?.navigate(node.createRoute())
                            },
                            viewModel = viewModel,
                            isDarkTheme = isDarkTheme
                        )
                    }
                },
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    val focusManager = LocalFocusManager.current

                    Scaffold(
                        contentColor = LocalTheme.current.colors.backgroundDarker,
                        backgroundColor = LocalTheme.current.colors.backgroundDarker,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    focusManager.clearFocus()
                                })
                            }
                            .fillMaxSize(),
                        topBar = {
                            val currentEntry = navController?.currentBackStackEntryFlow?.collectAsState(null)

                            TopAppBar(
                                colors = TopAppBarColors(
                                    containerColor = LocalTheme.current.colors.backgroundLighter,
                                    titleContentColor = LocalTheme.current.colors.primary,
                                    scrolledContainerColor = LocalTheme.current.colors.backgroundLighter,
                                    actionIconContentColor = LocalTheme.current.colors.primary,
                                    navigationIconContentColor = LocalTheme.current.colors.primary
                                ),
                                navigationIcon = {
                                    val localOnBackPress = LocalOnBackPress.current

                                    AnimatedVisibility(
                                        visible = currentEntry?.value?.destination?.route != DEFAULT_START_DESTINATION
                                    ) {
                                        ImageButton(
                                            imageVector = Icons.AutoMirrored.Outlined.ArrowBackIos,
                                            onClick = {
                                                localOnBackPress?.invoke() ?: navController?.popBackStack()
                                            }
                                        )
                                    }
                                },
                                title = {
                                    Text(
                                        text = title ?: "",
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            color = LocalTheme.current.colors.primary
                                        )
                                    )
                                },
                                actions = {
                                    ImageButton(
                                        imageVector = Icons.Outlined.LunchDining,
                                        onClick = {
                                            coroutineScope.launch {
                                                drawerState.open()
                                            }
                                        }
                                    )
                                }
                            )
                        }
                    ) { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            content()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ModalNavigationDrawerContent(
    modifier: Modifier = Modifier,
    isDarkTheme: MutableState<Boolean>,
    viewModel: DashboardViewModel,
    onNavigate: (NavigationNode) -> Unit
) {
    val focusManager = LocalFocusManager.current

    ModalDrawerSheet(
        modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .background(color = LocalTheme.current.colors.backgroundLighter)
                .fillMaxSize()
                .padding(start = 8.dp, top = 32.dp)
        ) {
            item {
                ThemeSwitch(
                    modifier = Modifier.fillMaxWidth(),
                    onChange = { isChecked ->
                        isDarkTheme.value = isChecked
                        viewModel.setTheme(isChecked)
                    },
                    isChecked = isDarkTheme
                )
            }
            item {
                val showSearchBar = remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    AnimatedVisibility(
                        showSearchBar.value,
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        SearchBar(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = 0.dp
                        )
                    }
                    ImageButton(
                        modifier = Modifier.weight(1f, fill = false),
                        imageVector = Icons.Outlined.Search,
                        onClick = {
                            showSearchBar.value = true
                        }
                    )
                }
            }
            items(AppBarCategories.entries.toTypedArray()) { category ->
                category.titleRes.let { categoryName ->
                    val showChildren = rememberSaveable { mutableStateOf(false) }

                    if(categoryName != null) {
                        Column {
                            ClickableTextButton(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(categoryName),
                                suffixVector = if(category.children.isNotEmpty()) {
                                    Icons.Outlined.ExpandMore
                                }else null
                            ) {
                                if(category.children.isNotEmpty()) {
                                    showChildren.value = showChildren.value.not()
                                }else {
                                    category.navigationNode?.let { node ->
                                        onNavigate(node)
                                    }
                                }
                            }
                            AnimatedVisibility(showChildren.value) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 8.dp)
                                ) {
                                    category.children.forEach { child ->
                                        MobileNavigationChild(
                                            child = child,
                                            onClick = { node ->
                                                onNavigate(node)
                                            }
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
}

@Composable
private fun MobileNavigationChild(
    modifier: Modifier = Modifier,
    child: CategoryNode,
    onClick: (NavigationNode) -> Unit
) {
    (child.titleRes ?: child.navigationNode?.titleRes)?.let { categoryName ->
        val showChildren = rememberSaveable { mutableStateOf(false) }

        Column(modifier) {
            ClickableTextButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(categoryName),
                suffixVector = if(child.children.isNotEmpty()) {
                    Icons.Outlined.ExpandMore
                }else null
            ) {
                if(child.children.isNotEmpty()) {
                    showChildren.value = showChildren.value.not()
                }else {
                    child.navigationNode?.let {
                        onClick(it)
                    }
                }
            }
            AnimatedVisibility(showChildren.value) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                ) {
                    child.children.forEach { nestedChild ->
                        MobileNavigationChild(
                            child = nestedChild,
                            onClick = onClick
                        )
                    }
                }
            }
        }
    }
}