package com.example.horegify.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.horegify.data.repository.MusicRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = MusicRepository()

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        _uiState.value = HomeUiState(isLoading = true)

        viewModelScope.launch {
            delay(500) // loading simulation

            val allTracks = repository.getAllTracks()

            _uiState.value = HomeUiState(
                isLoading = false,
                popularTracks = allTracks.shuffled().take(3),
                recentTracks = allTracks.shuffled().take(4),
                recommendedTracks = allTracks.shuffled().take(8)
            )
        }
    }
}
