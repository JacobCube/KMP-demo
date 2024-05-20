package theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Globalni Tipsport tema obsahujici rozsireni pro MaterialTheme
 */
@Composable
fun TipsportTheme(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colors = remember(isDarkTheme) {
        if(isDarkTheme) BaseDarkColors()
        else BaseLightColors()
    }
    CompositionLocalProvider(
        LocalTipsportColors provides colors,
        LocalTheme provides object: BaseTheme {}
    ) {
        MaterialTheme(
            content = content
        )
    }
}

private val LocalTipsportColors = staticCompositionLocalOf<TipsportColors> {
    BaseLightColors()
}

/** Aktualni tema */
val LocalTheme = staticCompositionLocalOf<BaseTheme> {
    object: BaseTheme {

    }
}

interface BaseTheme {
    /** Farby */
    val colors: TipsportColors
        @Composable
        @ReadOnlyComposable
        get() = LocalTipsportColors.current
}