package com.example.horegify.data.api.response

data class TrackSearchResponse(
    val results: List<TrackDto>
)

data class TrackDto(
    val id: String,
    val name: String,
    val artist_name: String,
    val album_name: String,
    val duration: Int,
    val image: String,
    val audiodownload: String
)
