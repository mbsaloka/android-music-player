package com.example.horegify.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.horegify.ui.components.BottomNavBar
import com.example.horegify.ui.navigation.NavigationGraph
import com.example.horegify.ui.theme.HoregifyTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun HoregifyApp() {
    val navController = rememberNavController()

    var isDarkTheme by rememberSaveable { mutableStateOf(false) }

    HoregifyTheme(darkTheme = isDarkTheme) {
        Scaffold(
            bottomBar = {
                BottomNavBar(navController = navController)
            }
        ) { innerPadding ->
            NavigationGraph(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                isDarkTheme = isDarkTheme,
                onToggleTheme = { isDarkTheme = !isDarkTheme }
            )
        }
    }
}