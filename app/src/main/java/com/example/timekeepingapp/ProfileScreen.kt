package com.example.timekeepingapp

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(navigationToGroupScreen:() -> Unit) {

    val activityContext = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Text("Name: John Doe", fontSize = 24.sp)
                Text("Current Activity: None", fontSize = 24.sp)
            }
        }
        Column(
            modifier = Modifier.fillMaxSize().weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(16.dp),
            ) {

            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                navigationToGroupScreen()
            }) {
                Text("Back")
            }
            Button(onClick = {
                Toast.makeText(activityContext,
                    "You can't delete profiles yet",
                    //"Whoopsies! You can't delete profiles yet! " +
                    //"(˶˃\uD800\uDCF7˂˶) (˶˃\uD800\uDCF7˂˶) (˶˃\uD800\uDCF7˂˶) (˶˃\uD800\uDCF7˂˶)",
                    Toast.LENGTH_LONG).show()
            }) {
                Text("Delete Profile")
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileScreen({})
}