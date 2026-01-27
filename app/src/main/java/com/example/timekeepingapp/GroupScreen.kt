package com.example.timekeepingapp

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class GroupItem(
    val id: Int,
    var name: String,
    var activity: String,
    var isEditing: Boolean,
)

@Composable
fun GroupScreen(navigationToChoiceScreen:() -> Unit, navigationToProfileScreen:(Int) -> Unit,
                viewGroupList: GroupListViewModel) {

    val activityContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val idTracker by activityContext.groupIdTrackerFlow().collectAsState(initial = 0)

    // Remember variables
    val gItems = viewGroupList.listItems.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("John Doe") }
    var itemActivity by remember { mutableStateOf("None") }

    // Column of "profiles"
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title of Page
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 28.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally)) {
            Text("Welcome to Group Mode", fontSize = 18.sp)
        }
        // Space for adding "profiles" (Lazy Column)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(gItems.value){
                item ->
                GroupListItem(
                    item = item,
                    navigationToProfileScreen = navigationToProfileScreen
                )
            }
        }
        // Back Button & Add "Profile" Button
        Row(modifier = Modifier.fillMaxWidth(),
            Arrangement.SpaceBetween) {
            // 'Back' button
            Button(onClick = {
                navigationToChoiceScreen()
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
            // 'Add Profile' Button
            Button(
                onClick = {
                    //Display AlertDialog
                    showDialog = true
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }

        if (showDialog) {
            AlertDialog(onDismissRequest = {showDialog = false},
                confirmButton = {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(onClick = {
                            if (itemName.isNotBlank()) {
                                val newItem = GroupItem(
                                    id = idTracker,
                                    name = itemName,
                                    activity = itemActivity,
                                    isEditing = false,
                                )
                                coroutineScope.launch {
                                    activityContext.saveGroupIdTracker(idTracker + 1)
                                }
                                viewGroupList.AddItem(newItem)
                                showDialog = false
                                itemName = "John Doe"
                                itemActivity = "None"
                            }
                        }) {
                            Text("Add")
                        }
                        Button(onClick = {showDialog = false}) {
                            Text("Cancel")
                        }
                    }
                },
                title = {Text("Add Group Item")},
                text = {
                    Column {
                        OutlinedTextField(
                            value = itemName,
                            onValueChange = {itemName = it},
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        OutlinedTextField(
                            value = itemActivity,
                            onValueChange = {itemActivity = it},
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun GroupItemEditor(item: GroupItem, onEditComplete: (String, String) -> Unit) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedActivity by remember { mutableStateOf(item.activity) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.LightGray)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly)
    {
        Column {
            BasicTextField(
                value = editedName,
                onValueChange = {editedName = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(
                value = editedActivity,
                onValueChange = {editedActivity = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }
        Button(onClick = {
            isEditing = false
            onEditComplete(editedName, editedActivity)
        }) {
            Text("Save")
        }
    }
}

@Composable
fun GroupListItem(
    item: GroupItem,
    navigationToProfileScreen: (Int) -> Unit
){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(width = 2.dp, color = Color(0XFF018786)),
                shape = RoundedCornerShape(0),
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(Modifier
            .weight(1f)
            .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(imageVector = Icons.Default.AccountCircle,
                contentDescription = null, modifier = Modifier.size(48.dp))
            Text(text = item.name, fontSize = 16.sp, modifier = Modifier.padding(horizontal = 12.dp))
        }
        Column(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = { navigationToProfileScreen(item.id) }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Go to profile page",
                    tint = Color(0xFF5D28A8))
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun GroupPreview() {
    val application = LocalContext.current.applicationContext as Application
    GroupScreen({}, {}, GroupListViewModel(application))
}