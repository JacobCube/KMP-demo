package ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.LocalTheme

@Composable
fun BrandChip(
    modifier: Modifier = Modifier,
    chipText: String,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    FilterChip(
        modifier = modifier.padding(horizontal = 4.dp),
        shape = RoundedCornerShape(18.dp),
        selected = selected,
        onClick = {
            onClick()
        },
        label = {
            Text(
                text = chipText,
                style = TextStyle(fontSize = 14.sp)
            )
        },
        trailingIcon = {
            if (selected) {
                Icon(
                    modifier = Modifier
                        .size(18.dp),
                    imageVector = Icons.Outlined.Close,
                    tint = LocalTheme.current.colors.backgroundLighter,
                    contentDescription = ""
                )
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = LocalTheme.current.colors.secondary,
            labelColor = LocalTheme.current.colors.backgroundLighter,
            selectedContainerColor = LocalTheme.current.colors.primary,
            selectedLabelColor = LocalTheme.current.colors.backgroundDarker,
        ),
        border = filterChipTransparentBorder()
    )
}

@Composable
fun filterChipTransparentBorder(): BorderStroke {
    return FilterChipDefaults.filterChipBorder(
        borderColor = Color.Transparent,
        selectedBorderColor = Color.Transparent,
        disabledBorderColor = Color.Transparent,
        disabledSelectedBorderColor = Color.Transparent,
        enabled = true,
        selected = true
    )
}