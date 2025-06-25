package com.example.horegify.ui.screen.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import com.example.horegify.data.model.RepeatMode
import com.example.horegify.data.model.Track
import com.example.horegify.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class PlayerViewModel(
    navBackStackEntry: NavBackStackEntry,
    private val repository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState())
    open val uiState: StateFlow<PlayerUiState> = _uiState

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    init {
        val id = navBackStackEntry.arguments?.getString("id")
        println("ID vm: $id")

        viewModelScope.launch {
            id?.let {
                val track = repository.getTrackById(it)
                if (track != null) {
                    _uiState.value = track.toPlayerUiState()
                    repository.isFavorite(it).collect { isFav ->
                        _isFavorite.value = isFav ?: false
                    }
                }
            }
        }
    }

    fun togglePlayPause() {
        _uiState.value = _uiState.value.copy(isPlaying = !_uiState.value.isPlaying)
    }

    fun toggleShuffle() {
        _uiState.value = _uiState.value.copy(isShuffled = !_uiState.value.isShuffled)
    }

    fun toggleRepeat() {
        val next = when (_uiState.value.repeatMode) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
        _uiState.value = _uiState.value.copy(repeatMode = next)
    }

    fun seekTo(seconds: Int) {
        _uiState.value = _uiState.value.copy(currentTime = seconds)
    }

    fun setVolume(vol: Int) {
        _uiState.value = _uiState.value.copy(volume = vol.coerceIn(0, 100))
    }

    fun toggleFavorite() {
        val id = _uiState.value.id ?: return
        viewModelScope.launch {
            repository.toggleFavorite(id)
            _isFavorite.value = !(_isFavorite.value)
        }
    }
}

private fun Track.toPlayerUiState(): PlayerUiState {
    return PlayerUiState(
        id = id,
        title = title,
        artist = artist,
        album = album,
        duration = duration,
        currentTime = currentTime,
        isPlaying = isPlaying,
        isShuffled = isShuffled,
        repeatMode = repeatMode,
        volume = volume,
        artworkUrl = artworkUrl,
        thumbnailUrl = thumbnailUrl
    )
}

class PlayerViewModelFactory(
    private val repository: MusicRepository,
    private val navBackStackEntry: NavBackStackEntry,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayerViewModel(navBackStackEntry, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
