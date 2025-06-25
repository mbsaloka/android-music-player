package com.example.horegify.ui.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.horegify.data.local.AppDatabase
import com.example.horegify.data.repository.MusicRepository
import com.example.horegify.ui.screen.home.HomeScreen
import com.example.horegify.ui.screen.library.LibraryScreen
import com.example.horegify.ui.screen.player.PlayerScreen
import com.example.horegify.ui.screen.search.SearchScreen
import com.example.horegify.ui.screen.search.SearchScreenWithViewModel
import com.example.horegify.ui.screen.search.SearchViewModel

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
                    println("Track ID: ${track.id}")
                },
                onNavigateToGenre = { genre ->
//                    navController.navigate("genre/$genre")
                    navController.navigate("library")
                },
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable(BottomNavItem.Search.route) {
            val context = LocalContext.current
            val application = context.applicationContext as Application
            val database = AppDatabase.getDatabase(application)
            val trackDao = database.trackDao()
            val repository = remember { MusicRepository(trackDao) }

            val factory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(repository) as T
                }
            }

            val viewModel: SearchViewModel = viewModel(factory = factory)

            SearchScreenWithViewModel(
                viewModel = viewModel,
                onTrackClick = { track ->
                    navController.navigate("player/${track.id}")
                },
                onGenreClick = { genre ->
                    navController.navigate("library")
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
                onBack = { navController.popBackStack() },
                navBackStackEntry = backStackEntry
            )
            println("ID: ${backStackEntry.arguments?.getString("id")}")
        }
    }
}
