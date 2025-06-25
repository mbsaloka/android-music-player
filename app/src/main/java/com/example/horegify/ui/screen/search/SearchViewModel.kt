package com.example.horegify.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.horegify.data.model.Track
import com.example.horegify.data.repository.MusicRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: MusicRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableStateFlow<List<Track>>(emptyList())
    val searchResults: StateFlow<List<Track>> = _searchResults

    private var searchJob: Job? = null

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            if (newQuery.isNotBlank()) {
                val results = repository.searchTracks(newQuery)
                _searchResults.value = results
            } else {
                _searchResults.value = emptyList()
            }
        }
    }
}