package com.example.horegify.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.horegify.ui.screen.home.HomeScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(
                onNavigateToPlayer = { track ->
                    navController.navigate("player/${track.id}")
                },
                onNavigateToGenre = { genre ->
                    navController.navigate("genre/$genre")
                },
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        // TODO: tambahkan screen lain di sini, jangan lupa teruskan parameter tema jika perlu
    }
}
