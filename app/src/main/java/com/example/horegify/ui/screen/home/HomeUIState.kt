package com.example.horegify.ui.screen.home

import com.example.horegify.data.model.Track

data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val popularTracks: List<Track> = emptyList(),
    val recentTracks: List<Track> = emptyList(),
    val recommendedTracks: List<Track> = emptyList()
)
