package com.example.mynotes.screens

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.mynotes.domain.model.PhoneModel
import com.example.mynotes.domain.model.TagModel
import com.example.mynotes.routing.Screen
import com.example.mynotes.ui.components.AppDrawer
//import com.example.mynotes.ui.components.Note
import com.example.mynotes.ui.components.Phone
import com.example.mynotes.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun NotesScreen(viewModel: MainViewModel) {
    val phones by viewModel.phonesNotInTrash.observeAsState(listOf())
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    var headTitle by remember { mutableStateOf("My Phone Book") }

    val tags: List<TagModel> by viewModel.tags.observeAsState(listOf())
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = headTitle,
                        color = MaterialTheme.colors.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch { scaffoldState.drawerState.open() }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Drawer Button"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        expanded = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Drawer Button"
                        )
                    }
                    DropdownMenu(
                        modifier = Modifier.width(width = 150.dp),
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        },
                        // adjust the position
                        offset = DpOffset(x = (-102).dp, y = (-64).dp),
                        properties = PopupProperties()
                    ) {
                        tags.forEach { label ->
                            DropdownMenuItem(onClick = {
                                headTitle = if (label.tagName == "no tag") {
                                    "My Phone Book"
                                } else {
                                    label.tagName
                                }
                                expanded = false
                            }) {
                                if (label.tagName == "no tag") {
                                    Text(text = "All")
                                } else {
                                    Text(text = label.tagName)
                                }
                            }
                        }
                    }
                }
            )
        },
        drawerContent = {
            AppDrawer(
                currentScreen = Screen.Notes,
                closeDrawerAction = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onCreateNewPhoneClick() },
                contentColor = MaterialTheme.colors.background,
                content = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Note Button"
                    )
                }
            )
        }
    ) {
        if (phones.isNotEmpty()) {
            PhonesList(
                phones = phones,
                onPhoneClick = { viewModel.onPhoneClick(it) },
                headTitle = headTitle
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun PhonesList(
    phones: List<PhoneModel>,
    onPhoneClick: (PhoneModel) -> Unit,
    headTitle: String
) {
    val phoneSort = phones.sortedBy { it.name }
    LazyColumn {
        items(count = phoneSort.size ) { phoneIndex ->
            val phone = phoneSort[phoneIndex]
            if (headTitle == "My Phone Book" || phone.tag.tagName == headTitle) {
                Phone(
                    phone = phone,
                    onPhoneClick = onPhoneClick,
                    isSelected = false
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun PhonesListPreview() {
    PhonesList(
        phones = listOf(
            PhoneModel(1, "A", "08123456789"),
            PhoneModel(2, "C", "08123456789"),
            PhoneModel(3, "B", "08123456789")
        ),
        onPhoneClick = {},
        headTitle = "My Phone Book"
    )
}
//
//@ExperimentalMaterialApi
//@Composable
//private fun NotesList(
//    notes: List<NoteModel>,
//    onNoteCheckedChange: (NoteModel) -> Unit,
//    onNoteClick: (NoteModel) -> Unit
//) {
//    LazyColumn {
//        items(count = notes.size) { noteIndex ->
//            val note = notes[noteIndex]
//            Note(
//                note = note,
//                onNoteClick = onNoteClick,
//                onNoteCheckedChange = onNoteCheckedChange,
//                isSelected = false
//            )
//        }
//    }
//}
//
//@ExperimentalMaterialApi
//@Preview
//@Composable
//private fun NotesListPreview() {
//    NotesList(
//        notes = listOf(
//            NoteModel(1, "Note 1", "Content 1", null),
//            NoteModel(2, "Note 2", "Content 2", false),
//            NoteModel(3, "Note 3", "Content 3", true)
//        ),
//        onNoteCheckedChange = {},
//        onNoteClick = {}
//    )
//}