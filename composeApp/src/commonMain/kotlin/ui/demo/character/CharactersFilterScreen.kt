package ui.demo.character

import LocalDeviceType
import LocalNavController
import LocalOnBackPress
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import navigation.NavigationArguments
import org.jetbrains.compose.resources.stringResource
import rododendron.composeapp.generated.resources.Res
import rododendron.composeapp.generated.resources.characters_filter_name
import rododendron.composeapp.generated.resources.characters_filter_species
import rododendron.composeapp.generated.resources.characters_filter_type
import rododendron.composeapp.generated.resources.characters_list_filter
import theme.LocalTheme
import ui.base.BaseScreen

@Composable
fun CharactersFilterScreen(
    defaultName: String? = null,
    defaultStatus: String? = null,
    defaultSpecies: String? = null,
    defaultType: String? = null,
    defaultGender: String? = null
) {
    val name = remember { mutableStateOf(defaultName) }
    val status = remember { mutableStateOf(defaultStatus) }
    val species = remember { mutableStateOf(defaultSpecies) }
    val type = remember { mutableStateOf(defaultType) }
    val gender = remember { mutableStateOf(defaultGender) }

    BaseScreen(
        title = stringResource(Res.string.characters_list_filter)
    ) {
        CharactersFilterContent(
            modifier = Modifier.fillMaxSize(),
            name = name,
            status = status,
            type = type,
            species = species,
            gender = gender
        )
    }
}

@Composable
fun CharactersFilterContent(
    modifier: Modifier = Modifier,
    name: MutableState<String?>,
    status: MutableState<String?>,
    type: MutableState<String?>,
    species: MutableState<String?>,
    gender: MutableState<String?>
) {
    val navController = LocalNavController.current

    Scaffold(
        modifier = modifier,
        contentColor = Color.Transparent,
        containerColor = Color.Transparent,
        floatingActionButton = {
            val localOnBackPress = LocalOnBackPress.current

            if(LocalDeviceType.current == WindowWidthSizeClass.Compact) {
                FloatingActionButton(
                    containerColor = LocalTheme.current.colors.primary,
                    contentColor = LocalTheme.current.colors.backgroundLighter,
                    content = {
                        Image(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(color = LocalTheme.current.colors.backgroundLighter)
                        )
                    },
                    onClick = {
                        // TODO mobile web doesn't work - add function setResult which will pop and add arguments to url.search
                        navController?.previousBackStackEntry
                            ?.savedStateHandle
                            ?.apply {
                                set(
                                    NavigationArguments.CHARACTER_FILTER_NAME,
                                    name.value
                                )
                                set(
                                    NavigationArguments.CHARACTER_FILTER_STATUS,
                                    status.value
                                )
                                set(
                                    NavigationArguments.CHARACTER_FILTER_SPECIES,
                                    species.value
                                )
                                set(
                                    NavigationArguments.CHARACTER_FILTER_TYPE,
                                    type.value
                                )
                                set(
                                    NavigationArguments.CHARACTER_FILTER_GENDER,
                                    gender.value
                                )
                            }
                        localOnBackPress?.invoke() ?: navController?.popBackStack()
                    }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            if(LocalDeviceType.current != WindowWidthSizeClass.Compact) {
                Text(
                    text = stringResource(Res.string.characters_list_filter),
                    style = TextStyle(
                        fontSize = 24.sp,
                        color = LocalTheme.current.colors.primary
                    )
                )
            }

            TextField(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(
                        color = LocalTheme.current.colors.backgroundDarker,
                        shape = RoundedCornerShape(8.dp)
                    ),
                value = name.value ?: "",
                onValueChange = { name.value = it },
                label = {
                    Text(
                        text = stringResource(Res.string.characters_filter_name)
                    )
                },
                colors = getOutlinedTextFieldColors()
            )
            TextField(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(
                        color = LocalTheme.current.colors.backgroundDarker,
                        shape = RoundedCornerShape(8.dp)
                    ),
                value = species.value ?: "",
                onValueChange = { species.value = it },
                label = {
                    Text(
                        text = stringResource(Res.string.characters_filter_species)
                    )
                },
                colors = getOutlinedTextFieldColors()
            )
            TextField(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(
                        color = LocalTheme.current.colors.backgroundDarker,
                        shape = RoundedCornerShape(8.dp)
                    ),
                value = type.value ?: "",
                onValueChange = { type.value = it },
                label = {
                    Text(
                        text = stringResource(Res.string.characters_filter_type)
                    )
                },
                colors = getOutlinedTextFieldColors()
            )
            val states = listOf("alive", "dead", "unknown")
            val genders = listOf("female", "male", "unknown")

            Text(
                modifier = Modifier.padding(8.dp),
                text = "Status",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalTheme.current.colors.primary
                )
            )
            states.forEach { value ->
                Row(
                    modifier = Modifier.clickable {
                        status.value = value
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = status.value == value,
                        onClick = {
                            status.value = value
                        }
                    )
                    Text(
                        text = value,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = LocalTheme.current.colors.primary
                        )
                    )
                }
            }


            Text(
                modifier = Modifier.padding(8.dp),
                text = "Gender",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalTheme.current.colors.primary
                )
            )
            genders.forEach { value ->
                Row(
                    modifier = Modifier.clickable {
                        gender.value = value
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = gender.value == value,
                        onClick = {
                            gender.value = value
                        }
                    )
                    Text(
                        text = value,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = LocalTheme.current.colors.primary
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun getOutlinedTextFieldColors(
    unfocusedBorderColor: Color = Color.Transparent,
    focusedBorderColor: Color = LocalTheme.current.colors.secondary,
    unfocusedLabelColor: Color = LocalTheme.current.colors.secondary,
    focusedLabelColor: Color = LocalTheme.current.colors.secondary,
    cursorColor: Color = LocalTheme.current.colors.secondary,
    errorBorderColor: Color = Color.Red,
    errorCursorColor: Color = Color.Red,
    errorLabelColor: Color = Color.Red,
    disabledBorderColor: Color = LocalTheme.current.colors.secondary,
    disabledLabelColor: Color = LocalTheme.current.colors.secondary,
) = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor = unfocusedBorderColor,
    focusedBorderColor = focusedBorderColor,
    disabledBorderColor = disabledBorderColor,
    cursorColor = cursorColor,
    errorBorderColor = errorBorderColor,
    errorCursorColor = errorCursorColor,
    disabledLabelColor = disabledLabelColor,
    unfocusedLabelColor = unfocusedLabelColor,
    focusedLabelColor = focusedLabelColor,
    errorLabelColor = errorLabelColor,
    unfocusedTextColor = LocalTheme.current.colors.primary,
    focusedTextColor = LocalTheme.current.colors.primary,
    errorTextColor = Color.Red,
    disabledTextColor = LocalTheme.current.colors.secondary,
)