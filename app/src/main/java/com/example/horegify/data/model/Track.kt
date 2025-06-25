package com.example.horegify.data.model

data class Track(
    val id: String,
    val title: String = "",
    val artist: String = "",
    val genre: String = "",
    val album: String = "",
    val duration: Int = 0,           // in seconds
    val currentTime: Int = 0,        // in seconds
    val isPlaying: Boolean = false,
    val isShuffled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val volume: Int = 75,            // 0–100
    val artworkUrl: String = "",
    val thumbnailUrl: String = "",
    val isFavorite: Boolean = false,
)
