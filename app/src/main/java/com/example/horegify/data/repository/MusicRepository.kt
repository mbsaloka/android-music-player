package com.example.horegify.data.repository

import com.example.horegify.data.model.Track

class MusicRepository {
    private val tracks = listOf(
        Track(
            id = "1",
            title = "Shape of You",
            artist = "Ed Sheeran",
            album = "÷ (Divide)",
            duration = 233,
            artworkUrl = "https://picsum.photos/200?1",
            thumbnailUrl = "https://picsum.photos/300?1"
        ),
        Track(
            id = "2",
            title = "Blinding Lights",
            artist = "The Weeknd",
            album = "After Hours",
            duration = 200,
            artworkUrl = "https://picsum.photos/200?2",
            thumbnailUrl = "https://picsum.photos/300?2"
        ),
        Track(
            id = "3",
            title = "Levitating",
            artist = "Dua Lipa",
            album = "Future Nostalgia",
            duration = 203,
            artworkUrl = "https://picsum.photos/200?3",
            thumbnailUrl = "https://picsum.photos/300?3"
        ),
        Track(
            id = "4",
            title = "Bad Guy",
            artist = "Billie Eilish",
            album = "WHEN WE ALL FALL ASLEEP, WHERE DO WE GO?",
            duration = 194,
            artworkUrl = "https://picsum.photos/200?4",
            thumbnailUrl = "https://picsum.photos/300?4"
        ),
        Track(
            id = "5",
            title = "Senorita",
            artist = "Camila Cabello",
            album = "Senorita - Single",
            duration = 191,
            artworkUrl = "https://picsum.photos/200?5",
            thumbnailUrl = "https://picsum.photos/300?5"
        ),
        Track(
            id = "6",
            title = "Circles",
            artist = "Post Malone",
            album = "Hollywood's Bleeding",
            duration = 215,
            artworkUrl = "https://picsum.photos/200?6",
            thumbnailUrl = "https://picsum.photos/300?6"
        ),
        Track(
            id = "7",
            title = "Sunflower",
            artist = "Post Malone, Swae Lee",
            album = "Spider-Man: Into the Spider-Verse",
            duration = 158,
            artworkUrl = "https://picsum.photos/200?7",
            thumbnailUrl = "https://picsum.photos/300?7"
        ),
        Track(
            id = "8",
            title = "Dance Monkey",
            artist = "Tones and I",
            album = "The Kids Are Coming",
            duration = 209,
            artworkUrl = "https://picsum.photos/200?8",
            thumbnailUrl = "https://picsum.photos/300?8"
        )
    )

    fun getAllTracks(): List<Track> = tracks

    fun getTrackById(id: String): Track? = tracks.find { it.id == id }

    fun updatePlayingStatus(trackId: String, isPlaying: Boolean): Track? {
        val track = getTrackById(trackId) ?: return null
        return track.copy(isPlaying = isPlaying)
    }
}
