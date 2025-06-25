package com.example.horegify.ui.screen.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.horegify.data.model.Track
import com.example.horegify.data.repository.MusicRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class GenreUiState(
    val genreTitle: String = "",
    val genreDescription: String = "",
    val forYouTracks: List<Track> = emptyList(),
    val discoverTracks: List<Track> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)

class GenreViewModel(
    private val repository: MusicRepository,
    private val genre: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(GenreUiState())
    val uiState: StateFlow<GenreUiState> = _uiState.asStateFlow()

    init {
        loadGenreData()
    }

    private fun loadGenreData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val genreTitle = genre.capitalize()
                val genreDescription = getGenreDescription(genre)

                val (forYouTracks, discoverTracks) = repository.getTracksByGenre(genre)

                _uiState.value = GenreUiState(
                    genreTitle = genreTitle,
                    genreDescription = genreDescription,
                    forYouTracks = forYouTracks,
                    discoverTracks = discoverTracks,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Unknown error"
                )
            }
        }
    }

    private fun getGenreDescription(genre: String): String {
        return when (genre.lowercase()) {
            "rock" -> "Rock music is characterized by a strong rhythm and often revolves around the electric guitar."
            "pop" -> "Pop music is catchy and designed to appeal to a broad audience."
            "jazz" -> "Jazz features improvisation, complex chords, and syncopated rhythms."
            "hip hop", "rap" -> "Hip hop and rap emphasize rhythm, rhyme, and street vernacular with strong beats."
            "classical" -> "Classical music is known for its rich orchestration and complex compositions."
            "electronic" -> "Electronic music features synthetic sounds and beats produced by electronic instruments."
            else -> "Discover the sounds and stories of $genre music."
        }
    }
}
