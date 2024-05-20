package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import theme.LocalTheme

@Composable
fun ThemeSwitch(
    modifier: Modifier = Modifier,
    onChange: (isChecked: Boolean) -> Unit,
    isChecked: State<Boolean>
) {
    Row(
        modifier = modifier.padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = Icons.Outlined.LightMode,
            contentDescription = null,
            tint = LocalTheme.current.colors.primary
        )
        Switch(
            checked = isChecked.value,
            onCheckedChange = onChange
        )
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = Icons.Outlined.DarkMode,
            contentDescription = null,
            tint = LocalTheme.current.colors.primary
        )
    }
}