package theme

import androidx.compose.ui.graphics.Color

/** Hlavni barvy pro svetly theme */
class BaseLightColors(
    override val primary: Color = Color(0xff1A2C3E),
    override val secondary: Color = Color(0xff66727E),
    override val backgroundLighter: Color = Color.White,
    override val backgroundDarker: Color = Color(0xfff5f5f5),
    override val shimmerDarkColor: Color = Color(0xff403f3b),
    override val shimmerColor: Color = Color(0xff212121)
): TipsportColors