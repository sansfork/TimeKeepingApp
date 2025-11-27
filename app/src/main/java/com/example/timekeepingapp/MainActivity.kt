package com.example.timekeepingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.timekeepingapp.ui.theme.TimeKeepingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimeKeepingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScreenDisplay(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenDisplay(modifier: Modifier) {
    MyApp()
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController, "choicescreen") {
        composable("choicescreen") {
            ChoiceScreen(
                {navController.navigate("groupscreen")},
                {navController.navigate("personalscreen")}
            )
        }
        composable("groupscreen") {
            GroupScreen(
                {navController.navigate("choicescreen")}
            )
        }
        composable("choicescreen") {
            PersonalScreen(
                {navController.navigate("choicescreen")}
            )
        }
    }
}