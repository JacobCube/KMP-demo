package ui.dashboard

import LocalDeviceType
import LocalNavController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import navigation.AppBarCategories
import org.jetbrains.compose.resources.stringResource
import raw.emptyLottieJson
import rododendron.composeapp.generated.resources.Res
import rododendron.composeapp.generated.resources.category_dashboard
import rododendron.composeapp.generated.resources.dashboard_demo_title
import rododendron.composeapp.generated.resources.dashboard_history_title
import rododendron.composeapp.generated.resources.exception_empty_navigation_description
import rododendron.composeapp.generated.resources.exception_empty_navigation_title
import theme.LocalTheme
import ui.ClickableTextButton
import ui.base.BaseScreen
import ui.components.ExceptionLayout
import ui.components.lifecycle.rememberLifecycleEvent

/** Main screen for displaying history */
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = remember { DashboardViewModel() }
) {
    val deviceType = LocalDeviceType.current

    val lifecycleEvent = rememberLifecycleEvent()

    val pastItems = viewModel.pastItems.collectAsState()

    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            viewModel.requestPastItems()
        }
    }

    BaseScreen(
        title = stringResource(Res.string.category_dashboard)
    ) {
        if(deviceType == WindowWidthSizeClass.Compact) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                DemoScreensContent(
                    modifier = Modifier.fillMaxWidth()
                )
                HistoryContent(
                    modifier = Modifier.fillMaxWidth(),
                    viewModel = viewModel,
                    pastItems = pastItems
                )
            }
        }else {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DemoScreensContent(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                )
                HistoryContent(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    viewModel = viewModel,
                    pastItems = pastItems
                )
            }
        }
    }
}

@Composable
private fun HistoryContent(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel,
    pastItems: State<List<String>?>
) {
    val navController = LocalNavController.current

    LazyColumn(modifier = modifier) {
        item {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.dashboard_history_title),
                style = TextStyle(
                    fontSize = 24.sp,
                    color = LocalTheme.current.colors.primary,
                    textAlign = TextAlign.Center
                )
            )
        }
        if(pastItems.value.orEmpty().isEmpty()) {
            item {
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.JsonString(emptyLottieJson)
                )

                ExceptionLayout(
                    composition = composition,
                    title = stringResource(Res.string.exception_empty_navigation_title),
                    description = stringResource(Res.string.exception_empty_navigation_description),
                )
            }
        }else {
            itemsIndexed(pastItems.value.orEmpty()) { _, item ->
                ClickableTextButton(
                    text = item,
                    onClick = {
                        navController?.navigate(item)
                    },
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        color = LocalTheme.current.colors.primary
                    )
                )
            }
            item {
                ClickableTextButton(
                    text = "clear history",
                    onClick = {
                        viewModel.clearPastItems()
                    },
                    suffixVector = Icons.Outlined.Delete,
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = Color.Red
                    )
                )
            }
        }
    }
}

@Composable
private fun DemoScreensContent(modifier: Modifier = Modifier) {
    val navController = LocalNavController.current

    Column(
        modifier = modifier
            .padding(bottom = 8.dp)
            .background(
                color = LocalTheme.current.colors.backgroundLighter,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.dashboard_demo_title),
            style = TextStyle(
                fontSize = 24.sp,
                color = LocalTheme.current.colors.primary,
                textAlign = TextAlign.Center
            )
        )
        AppBarCategories.Demo.children.forEach { category ->
            category.titleRes.let { categoryName ->
                Column {
                    if(categoryName != null) {
                        ClickableTextButton(
                            text = stringResource(categoryName),
                            textStyle = TextStyle(
                                fontSize = 24.sp,
                                color = LocalTheme.current.colors.primary
                            )
                        ) {
                            category.navigationNode?.let { node ->
                                navController?.navigate(node.route)
                            }
                        }
                    }
                }
            }
        }
    }
}