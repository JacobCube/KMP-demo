package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NoLiveLiterals
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import theme.LocalTheme

/**
 * Clickable text with suffix icon
 */
@NoLiveLiterals
@Preview
@Composable
fun ClickableTextButton(
    modifier: Modifier = Modifier,
    text: String = "",
    suffixVector: ImageVector? = null,
    isEnabled: Boolean = true,
    textStyle: TextStyle = TextStyle(
        color = LocalTheme.current.colors.primary
    ),
    verticalPadding: Dp = 14.dp,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable(
                //interactionSource = remember { MutableInteractionSource() },
                //TODO error deprecated indication = rememberRipple(color = textStyle.color),
                enabled = isEnabled,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = textStyle
        )
        if(suffixVector != null) {
            Image(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(14.dp),
                imageVector = suffixVector,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = textStyle.color)
            )
        }
    }
}