package com.example.timekeepingapp

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class GroupItem(
    val id: Int,
    var name: String,
    var activity: String,
    var isEditing: Boolean = false,
)

@Composable
fun GroupScreen(navigationToChoiceScreen:() -> Unit) {

    val activityContext = LocalContext.current

    // Remember variables
    var gItems by remember { mutableStateOf(listOf<GroupItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("John Doe") }
    var itemActivity by remember { mutableStateOf("None") }

    // Title of Page
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 42.dp)
        .wrapContentWidth(align = Alignment.CenterHorizontally)) {
        Text("Welcome to Group Mode", fontSize = 18.sp)
    }
    // Column of "profiles"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Space for adding "profiles" (Lazy Column)
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(gItems){
                item ->
                if (item.isEditing) {
                    GroupItemEditor(item = item, onEditComplete = {
                        editedName, editedActivity ->
                        gItems = gItems.map { it.copy(isEditing = false) }
                        val editedItem = gItems.find{it.id == item.id}
                        editedItem?.let {
                            it.name = editedName
                            it.activity = editedActivity
                        }
                    })
                }else{
                    GroupListItem(item = item,
                        onEditClick = {
                            // Finding out which item we're editing and changing 'isEditing' to True
                            gItems = gItems.map { it.copy(isEditing = it.id==item.id) }
                        },
                        onDeleteClick = {gItems = gItems-item})
                }
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
                                    id = gItems.size+1,
                                    name = itemName,
                                    activity = itemActivity,
                                )
                                gItems += newItem
                                showDialog = false
                                itemName = ""
                                itemActivity = ""
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
                    Column() {
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

    //Add item Button
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .wrapContentSize(align = Alignment.BottomEnd)) {
        Button(
            onClick = {
                // If number of items in LazyColumn
                // reaches limit (5 for testing),
                // generate pop-up message
                if (gItems.size+1 > 5) {
                        Toast.makeText(activityContext,
                            "Item Limit Reached (Max. 5)",
                            Toast.LENGTH_LONG).show()
                }else {
                    //Display AlertDialog
                    showDialog = true
                }
            }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }


    // 'Back' button
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .wrapContentSize(align = Alignment.BottomStart)) {
        Button(onClick = {
            navigationToChoiceScreen()
        }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
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
        .background(Color.White)
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
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(width = 2.dp, color = Color(0XFF018786)),
                shape = RoundedCornerShape(0),
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier
            .weight(1f)
            .padding(18.dp)) {
            Text(text = item.name, modifier = Modifier.padding(8.dp))
            Text(text = "Act: ${item.activity}", modifier = Modifier.padding(8.dp))

        }
        // Add navigation to new files (maybe files are pre-made?? but identical, like 10 empties)
        // OR MAYBE only one pre-made file (like prefab?) that you make copy of when creating
        // new profile and then delete copy when you delete profile??

        // Use only one Icon (Icons.Default.KeyboardArrowRight) <- Deprecated
        // Alternative: Icons.AutoMirrored.Filled.KeyboardArrowRight
        Column(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = {  }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupPreview() {
    GroupScreen({})
}