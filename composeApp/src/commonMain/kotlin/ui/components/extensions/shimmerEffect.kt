package ui.components.extensions

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import theme.LocalTheme

/** Vytvori na widgetu shimmer nacitaci efekt
 * @param stripeColor barva animovaneho prouzku
 * @param startEndColor pocatecni a konecna barva
 */
fun Modifier.shimmerEffect(
    stripeColor: Color,
    startEndColor: Color,
    shape: Shape = RectangleShape
): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val startOffsetX by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = (-2 * size.width).toFloat(),
        targetValue = (2 * size.width).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(800)
        ), label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                startEndColor,
                stripeColor,
                startEndColor
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        ),
        shape = shape
    ).onGloballyPositioned {
        size = it.size
    }
}

/** Vytvori na widgetu shimmer nacitaci efekt */
fun Modifier.brandShimmerEffect(
    shape: Shape? = null
): Modifier = composed {
    shimmerEffect(
        stripeColor = LocalTheme.current.colors.shimmerDarkColor,
        startEndColor = LocalTheme.current.colors.shimmerColor,
        shape = shape ?: RoundedCornerShape(16.dp)
    )
}