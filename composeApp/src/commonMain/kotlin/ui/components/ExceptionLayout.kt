package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.alexzhirkevich.compottie.LottieAnimation
import io.github.alexzhirkevich.compottie.LottieComposition
import theme.LocalTheme

/** Layout/component displayed for exceptional situations */
@Composable
fun ExceptionLayout(
    modifier: Modifier = Modifier,
    composition: LottieComposition?,
    title: String? = null,
    description: String? = null,
    footerContent: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxHeight(0.55f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        LottieAnimation(
            modifier = Modifier,
            composition = composition,
            restartOnPlay = true,
            iterations = Int.MAX_VALUE
        )
        title?.let { textContent ->
            SelectionContainer {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(0.8f)
                        .padding(top = 24.dp),
                    text = textContent,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        //fontFamily = LocalTheme.current.fonts.robotoMedium,
                        fontSize = 20.sp,
                        color = LocalTheme.current.colors.primary
                    )
                )
            }
        }
        description?.let { textContent ->
            SelectionContainer {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(0.8f)
                        .padding(top = 4.dp),
                    text = textContent,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        //fontFamily = LocalTheme.current.fonts.robotoRegular,
                        fontSize = 14.sp,
                        color = LocalTheme.current.colors.primary
                    )
                )
            }
        }
        footerContent()
    }
}