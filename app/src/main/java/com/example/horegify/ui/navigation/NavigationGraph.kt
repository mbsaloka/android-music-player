package com.example.horegify.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.horegify.ui.screen.home.HomeScreen
import com.example.horegify.ui.screen.library.LibraryScreen
import com.example.horegify.ui.screen.player.PlayerScreen
import com.example.horegify.ui.screen.search.SearchScreen

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
        composable(BottomNavItem.Search.route) {
            SearchScreen(
                query = "",
                onQueryChange = { query ->
                    // Handle search query change
                },
                onGenreClick = { genre ->
                    navController.navigate("genre/$genre")
                }
            )
        }
        composable(BottomNavItem.Library.route) {
            LibraryScreen(
                onNavigateToPlayer = { track ->
                    navController.navigate("player/${track.id}")
                }
            )
        }
        composable(
            route = "player/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            PlayerScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
