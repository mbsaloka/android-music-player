package com.example.horegify.ui.screen.library

import androidx.lifecycle.ViewModel
import com.example.horegify.data.model.Track
import com.example.horegify.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LibraryViewModel : ViewModel() {

    private val repository = MusicRepository()

    private val _savedTracks = MutableStateFlow<List<Track>>(
        repository.getAllTracks().take(10)
    )
    val savedTracks: StateFlow<List<Track>> = _savedTracks
}
