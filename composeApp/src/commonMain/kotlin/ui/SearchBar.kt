package ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.LocalTheme

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    elevation: Dp = 8.dp
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Card(
        modifier = modifier,
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            contentColor = LocalTheme.current.colors.backgroundDarker,
            containerColor = LocalTheme.current.colors.primary
        ),
        elevation = CardDefaults.cardElevation(elevation)
    ) {
        val inputValue = remember { mutableStateOf("") }

        TextField(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .padding(horizontal = 8.dp),
            value = inputValue.value,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            onValueChange = { newValue ->
                inputValue.value = newValue
            },
            textStyle = TextStyle(
                fontSize = 18.sp,
                color = LocalTheme.current.colors.backgroundDarker
            ),
            shape = CircleShape,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = LocalTheme.current.colors.backgroundDarker,
                errorCursorColor = LocalTheme.current.colors.backgroundDarker
            )
        )
    }
}