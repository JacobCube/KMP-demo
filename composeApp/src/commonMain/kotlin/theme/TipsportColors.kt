package theme

import androidx.compose.ui.graphics.Color

/**
 * Barvy tipsportu pro tema.
 */
interface TipsportColors {

    /** nejvyraznejsi text */
    val primary: Color

    /** druhy nejvyraznejsi text */
    val secondary: Color

    /** svetle pozadi obrazovky */
    val backgroundLighter: Color

    /** tmavsi pozadi obrazovky */
    val backgroundDarker: Color
    val shimmerDarkColor: Color
    val shimmerColor: Color
}