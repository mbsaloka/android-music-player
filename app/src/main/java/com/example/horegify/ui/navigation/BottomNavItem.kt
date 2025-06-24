package com.example.horegify.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Search : BottomNavItem("search", Icons.Default.Search, "Search")
    object Library : BottomNavItem("library", Icons.Default.LibraryMusic, "Library")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")

    companion object {
        val items = listOf(Home, Search, Library, Profile)
    }
}
