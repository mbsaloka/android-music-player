package com.example.horegify.data.api.mapper

import com.example.horegify.data.api.response.TrackDto
import com.example.horegify.data.model.Track

fun TrackDto.toModel(): Track = Track(
    id = id,
    title = name,
    artist = artist_name,
    album = album_name,
    duration = duration,
    artworkUrl = image,
    thumbnailUrl = image
)
