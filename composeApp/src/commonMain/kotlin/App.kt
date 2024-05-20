
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import navigation.NavigationContainer
import navigation.withViewModelStoreOwner
import org.jetbrains.compose.ui.tooling.preview.Preview

/** Current device frame */
val LocalDeviceType = staticCompositionLocalOf { WindowWidthSizeClass.Medium }

/** Whether device is a web */
val LocalOnBackPress = staticCompositionLocalOf<(() -> Unit)?> { null }

/** Default page size based on current device tye */
val LocalDefaultPageSize = staticCompositionLocalOf { 8 }

/** Default page size based on current device tye */
val LocalNavController = staticCompositionLocalOf<NavHostController?> { null }


/** Main */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
@Preview
fun App(
    navController: NavHostController,
    widthSizeClass: WindowWidthSizeClass? = null,
    startDestination: String? = null
) {
    // https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
    val windowSizeClass = calculateWindowSizeClass()

    CompositionLocalProvider(
        LocalDeviceType provides (widthSizeClass ?: windowSizeClass.widthSizeClass),
        LocalDefaultPageSize provides when(LocalDeviceType.current) {
            WindowWidthSizeClass.Expanded -> 20
            WindowWidthSizeClass.Medium -> 12
            //WindowWidthSizeClass.Compact
            else -> 8
        },
        LocalNavController provides navController
    ) {
        withViewModelStoreOwner {
            NavigationContainer(
                startDestination = startDestination,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}