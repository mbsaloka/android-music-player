package com.example.horegify.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.horegify.data.model.Track
import com.example.horegify.data.repository.MusicRepository
import com.example.horegify.ui.components.BottomNavBar
import com.example.horegify.ui.components.NowPlayingBar
import com.example.horegify.ui.navigation.NavigationGraph
import com.example.horegify.ui.theme.HoregifyTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun HoregifyApp(repository: MusicRepository) {
    val navController = rememberNavController()
    var isDarkTheme by rememberSaveable { mutableStateOf(false) }

    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory(repository))
    val playingTrack by appViewModel.playingTrack.collectAsState()

    HoregifyTheme(darkTheme = isDarkTheme) {
        Scaffold(
            bottomBar = {
                Column {
                    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route.orEmpty()
                    if (!currentRoute.startsWith("player") && playingTrack != null) {
                        NowPlayingBar(
                            track = playingTrack!!,
                            isPlaying = playingTrack!!.isPlaying,
                            onPlayPause = { appViewModel.togglePlayPause() },
                            onClick = {
                                navController.navigate("player/${playingTrack!!.id}")
                            }
                        )
                    }
                    BottomNavBar(navController)
                }
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

class AppViewModel(
    private val repository: MusicRepository
) : ViewModel() {

    private val _playingTrack = MutableStateFlow<Track?>(null)
    val playingTrack: StateFlow<Track?> = _playingTrack

    init {
        viewModelScope.launch {
            val recommended = repository.getRecommendedTracks()
            // Simulasikan satu lagu sedang dimainkan
            _playingTrack.value = recommended.firstOrNull()?.copy(
                isPlaying = true,
                currentTime = 45,
                duration = 200
            )
        }
    }

    fun togglePlayPause() {
        _playingTrack.value = _playingTrack.value?.copy(isPlaying = !_playingTrack.value!!.isPlaying)
    }
}

class AppViewModelFactory(private val repository: MusicRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
