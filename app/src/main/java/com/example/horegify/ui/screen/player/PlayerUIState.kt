package com.example.horegify.ui.screen.player

import com.example.horegify.data.model.RepeatMode

data class PlayerUiState(
    val id: String = "",
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val duration: Int = 0,
    val currentTime: Int = 0,
    val isPlaying: Boolean = false,
    val isShuffled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val volume: Int = 75,
    val artworkUrl: String = "",
    val thumbnailUrl: String = ""
)
