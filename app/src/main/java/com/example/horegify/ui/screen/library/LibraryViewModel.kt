package com.example.horegify.ui.screen.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.horegify.data.model.Track
import com.example.horegify.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(private val repository: MusicRepository) : ViewModel() {

    private val _savedTracks = MutableStateFlow<List<Track>>(emptyList())
    val savedTracks: StateFlow<List<Track>> = _savedTracks

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            try {
                _savedTracks.value = repository.getRecommendedTracks()
                _isLoading.value = false
            } catch (e: Exception) {
                _savedTracks.value = emptyList()
                _isLoading.value = false
            }
        }
    }
}

class LibraryViewModelFactory(
    private val repository: MusicRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LibraryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
