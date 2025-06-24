package com.example.horegify.ui.screen.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import com.example.horegify.data.model.Track
import com.example.horegify.data.model.RepeatMode
import com.example.horegify.data.repository.MusicRepository

open class PlayerViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = MusicRepository()
    private val _uiState = MutableStateFlow(PlayerUiState())
    open val uiState: StateFlow<PlayerUiState> = _uiState

    init {
        val id = savedStateHandle.get<String>("id")
        if (id != null) {
            val track = repository.getTrackById(id)
            if (track != null) {
                _uiState.value = track.toPlayerUiState()
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
