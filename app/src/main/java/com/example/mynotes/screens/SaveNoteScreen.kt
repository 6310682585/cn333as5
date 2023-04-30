package com.example.mynotes.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.mynotes.routing.MyNotesRouter
import com.example.mynotes.routing.Screen
import com.example.mynotes.viewmodel.MainViewModel
import com.example.mynotes.R
import com.example.mynotes.domain.model.ColorModel
//import com.example.mynotes.domain.model.NEW_NOTE_ID
import com.example.mynotes.domain.model.NEW_PHONE_ID
import com.example.mynotes.domain.model.PhoneModel
import com.example.mynotes.domain.model.TagModel
//import com.example.mynotes.domain.model.NoteModel
import com.example.mynotes.ui.components.NoteColor
import com.example.mynotes.util.fromHex
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun SaveNoteScreen(viewModel: MainViewModel) {
    val phoneEntry by viewModel.phoneEntry.observeAsState(PhoneModel())

    val colors: List<ColorModel> by viewModel.colors.observeAsState(listOf())

    val tags: List<TagModel> by viewModel.tags.observeAsState(listOf())

    val bottomDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val movePhoneToTrashDialogShownState = rememberSaveable { mutableStateOf(false) }

    BackHandler {
        if (bottomDrawerState.isOpen) {
            coroutineScope.launch { bottomDrawerState.close() }
        } else {
            MyNotesRouter.navigateTo(Screen.Notes)
        }
    }

    Scaffold(
        topBar = {
            val isEditingMode: Boolean = phoneEntry.id != NEW_PHONE_ID
            SavePhoneTopAppBar(
                isEditingMode = isEditingMode,
                onBackClick = { MyNotesRouter.navigateTo(Screen.Notes) },
                onSavePhoneClick = { viewModel.savePhone(phoneEntry) },
//                onOpenColorPickerClick = {
//                    coroutineScope.launch { bottomDrawerState.open() }
//                },
                onDeletePhoneClick = {
                    movePhoneToTrashDialogShownState.value = true
                }
            )
        }
    ) {
        BottomDrawer(
            drawerState = bottomDrawerState,
            drawerContent = {
                ColorPicker(
                    colors = colors,
                    onColorSelect = { color ->
                        viewModel.onPhoneEntryChange(phoneEntry.copy(color = color))
                    }
                )
            }
        ) {
            SavePhoneContent(
                phone = phoneEntry,
                tags = tags,
                onPhoneChange = { updatePhoneEntry ->
                    viewModel.onPhoneEntryChange(updatePhoneEntry)
                },
                onOpenColorPickerClick = {
                    coroutineScope.launch { bottomDrawerState.open() }
                },
            )
        }

        if (movePhoneToTrashDialogShownState.value) {
            AlertDialog(
                onDismissRequest = {
                    movePhoneToTrashDialogShownState.value = false
                },
                title = {
                    Text("Move phone number to the trash?")
                },
                text = {
                    Text(
                        "Are you sure you want to " +
                                "move this phone number to the trash?"
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.movePhoneToTrash(phoneEntry)
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        movePhoneToTrashDialogShownState.value = false
                    }) {
                        Text("Dismiss")
                    }
                }
            )
        }
    }
}

@Composable
fun SavePhoneTopAppBar(
    isEditingMode: Boolean,
    onBackClick: () -> Unit,
    onSavePhoneClick: () -> Unit,
//    onOpenColorPickerClick: () -> Unit,
    onDeletePhoneClick: () -> Unit
) {
    TopAppBar(
        title = {

        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
        actions = {
            IconButton(onClick = onSavePhoneClick) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save Phone Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }

            if (isEditingMode) {
                IconButton(onClick = onDeletePhoneClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Phone Button",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    )
}

@Composable
private fun SavePhoneContent(
    phone: PhoneModel,
    tags: List<TagModel>,
    onPhoneChange: (PhoneModel) -> Unit,
    onOpenColorPickerClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ContentTextField(
            label = "Name",
            text = phone.name,
            onTextChange = { newName ->
                onPhoneChange.invoke(phone.copy(name = newName))
            }
        )

        ContentNumField(
            label = "Phone number",
            text = phone.phoneNum,
            onTextChange = { newPhoneNum ->
                onPhoneChange.invoke(phone.copy(phoneNum = newPhoneNum))
            }
        )

        PickedColor(color = phone.color, onOpenColorPickerClick)

        //  Dropdown category list
        var expanded by remember { mutableStateOf(false) }
        var selectedCategory by remember { mutableStateOf(phone.tag.tagName) }
        var textfieldSize by remember { mutableStateOf(Size.Zero) }

        val icon = if (expanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown


        Column(Modifier.padding(20.dp)) {
            OutlinedTextField(
                readOnly = true,
                value = selectedCategory,
                onValueChange = { selectedCategory = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        //This value is used to assign to the DropDown the same width
                        textfieldSize = coordinates.size.toSize()
                    },
                label = {Text("choose tag")},
                trailingIcon = {
                    Icon(
                        icon,"click for choose category",
                        Modifier.clickable { expanded = !expanded })
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current){textfieldSize.width.toDp()})
            ) {
                tags.forEach { label ->
                    DropdownMenuItem(onClick = {
                        onPhoneChange.invoke(phone.copy(tag = label))
                        selectedCategory = label.tagName
                        expanded = false
                    }) {
                        Text(text = label.tagName)
                    }
                }
            }
        }
    }
}

@Composable
private fun ContentTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    onTextChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        )
    )
}

@Composable
private fun ContentNumField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    onTextChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        ),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
    )
}

@Composable
private fun PickedColor(
    color: ColorModel,
    onOpenColorPickerClick: () -> Unit,
) {
    Row(
        Modifier
            .padding(8.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Picked color",
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        IconButton(onClick = onOpenColorPickerClick) {
            NoteColor(
                color = Color.fromHex(color.hex),
                size = 40.dp,
                border = 1.dp,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
private fun ColorPicker(
    colors: List<ColorModel>,
    onColorSelect: (ColorModel) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().height(400.dp)) {
        Text(
            text = "Color picker",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(colors.size) { itemIndex ->
                val color = colors[itemIndex]
                ColorItem(
                    color = color,
                    onColorSelect = onColorSelect
                )
            }
        }
    }
}

@Composable
fun ColorItem(
    color: ColorModel,
    onColorSelect: (ColorModel) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onColorSelect(color)
                }
            )
    ) {
        NoteColor(
            modifier = Modifier.padding(10.dp),
            color = Color.fromHex(color.hex),
            size = 80.dp,
            border = 2.dp
        )
        Text(
            text = color.name,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Preview
@Composable
fun ColorItemPreview() {
    ColorItem(ColorModel.DEFAULT) {}
}

@Preview
@Composable
fun ColorPickerPreview() {
    ColorPicker(
        colors = listOf(
            ColorModel.DEFAULT,
            ColorModel.DEFAULT,
            ColorModel.DEFAULT
        )
    ) { }
}

