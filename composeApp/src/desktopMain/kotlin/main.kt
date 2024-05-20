
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.key
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.navigation.compose.rememberNavController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create

/** Desktop app get initialized here */
fun main() = application {
    val lifecycle = LifecycleRegistry()

    // Attach the LifecycleRegistry to document
    lifecycle.create()

    val showSearchBar = remember { mutableStateOf(false) }

    Window(
        onCloseRequest = {
            exitApplication()
        },
        state = rememberWindowState(
            placement = WindowPlacement.Maximized,
        ),
        //transparent = true,
        title = "Tipsys 3 DEMO",
        visible = true,
        undecorated = false,
        resizable = true,
        alwaysOnTop = false
    ) {
        App(navController = rememberNavController())
    }
}