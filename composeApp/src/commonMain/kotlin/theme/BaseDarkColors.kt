package theme

import androidx.compose.ui.graphics.Color

/** Hlavni barvy pro tmavy theme */
class BaseDarkColors(
    override val primary: Color = Color.White,
    override val secondary: Color = Color(0xffBBBBBB),
    override val backgroundLighter: Color = Color(0xff1E1E1E),
    override val backgroundDarker: Color = Color(0xff121212),
    override val shimmerDarkColor: Color = Color(0xff403f3b),
    override val shimmerColor: Color = Color(0xff212121)
): TipsportColors