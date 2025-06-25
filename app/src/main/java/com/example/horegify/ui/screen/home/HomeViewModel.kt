package com.example.horegify.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.horegify.data.local.dao.TrackDao
import com.example.horegify.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MusicRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)

            try {
                val popularTracks = repository.getPopularTracks()
                val recommendedTracks = repository.getRecommendedTracks()
//                val recent = repository.getRecentlyPlayedTracks().first()
                val recent = repository.getRecommendedTracks()

                _uiState.value = HomeUiState(
                    isLoading = false,
                    popularTracks = popularTracks,
                    recentTracks = recent,
                    recommendedTracks = recommendedTracks
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
}

class HomeViewModelFactory(private val repository: MusicRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
