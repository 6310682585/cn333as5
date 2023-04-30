package com.example.mynotes.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.example.mynotes.domain.model.PhoneModel
import com.example.mynotes.routing.Screen
import com.example.mynotes.ui.components.AppDrawer
//import com.example.mynotes.ui.components.Note
import com.example.mynotes.ui.components.Phone
import com.example.mynotes.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun TagScreen(viewModel: MainViewModel) {
    val phones by viewModel.phonesNotInTrash.observeAsState(listOf())
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Phone Book",
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
                        coroutineScope.launch { scaffoldState.drawerState.open() }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Drawer Button"
                        )
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
                onPhoneClick = { viewModel.onPhoneClick(it) }
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun PhonesList(
    phones: List<PhoneModel>,
    onPhoneClick: (PhoneModel) -> Unit
) {
    val phoneSort = phones.sortedBy { it.name }
    LazyColumn {
        items(count = phoneSort.size ) { phoneIndex ->
            val phone = phoneSort[phoneIndex]
            Phone(
                phone = phone,
                onPhoneClick = onPhoneClick,
                isSelected = false
            )
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
        onPhoneClick = {}
    )
}