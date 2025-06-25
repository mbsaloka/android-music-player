package com.example.horegify.data.local.mapper

import com.example.horegify.data.local.entity.TrackEntity
import com.example.horegify.data.model.Track

fun Track.toEntity(isFavorite: Boolean = false, playedAt: Long = 0L) = TrackEntity(
    id = id,
    title = title,
    artist = artist,
    genre = genre,
    album = album,
    duration = duration,
    artworkUrl = artworkUrl,
    thumbnailUrl = thumbnailUrl,
    isFavorite = isFavorite,
    playedAt = playedAt
)

fun TrackEntity.toTrack() = Track(
    id = id,
    title = title,
    artist = artist,
    genre = genre,
    album = album,
    duration = duration,
    artworkUrl = artworkUrl,
    thumbnailUrl = thumbnailUrl,
    isFavorite = isFavorite
)
