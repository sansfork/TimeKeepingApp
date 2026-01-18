package com.example.timekeepingapp

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.fragment.app.FragmentActivity

@Composable
fun ProfileScreen(id: Int, navigationToGroupScreen:() -> Unit, viewGroupList: GroupListViewModel) {

    val activityContext = LocalContext.current
    val profile = viewGroupList.GetItemById(id)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Text("Name: ${profile?.name}", fontSize = 24.sp)
                Text("", fontSize = 24.sp)
            }
        }
        Column(
            modifier = Modifier.fillMaxSize().weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp).background(Color.LightGray),
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
                //Toast.makeText(activityContext,
                //    "Whoopsies! You can't delete profiles yet! " +
                //    "(˶˃\uD800\uDCF7˂˶) (˶˃\uD800\uDCF7˂˶) (˶˃\uD800\uDCF7˂˶) (˶˃\uD800\uDCF7˂˶)",
                //    Toast.LENGTH_LONG).show()
                showFingerprintPrompt(
                    activity = activityContext as FragmentActivity,
                    onConfirm = {
                        viewGroupList.RemoveItemById(id)
                        navigationToGroupScreen()
                    })
            }) {
                Text("Delete Profile")
            }
        }

    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileScreen(0, {}, GroupListViewModel())
}