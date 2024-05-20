package ui.base

import LocalDeviceType
import LocalNavController
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.desktop.DesktopContainer
import ui.mobile.MobileContainer

/** Screen with the most basic interface common for all screen */
@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    content: @Composable () -> Unit
) {
    val navController = LocalNavController.current
    val deviceType = LocalDeviceType.current

    Box(modifier = modifier) {
        when(LocalDeviceType.current) {
            WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> {
                MobileContainer(
                    content = content,
                    title = title,
                    subtitle = subtitle,
                )
            }
            WindowWidthSizeClass.Expanded -> {
                DesktopContainer(
                    content = content,
                    title = title,
                    subtitle = subtitle,
                )
            }
        }
    }
}