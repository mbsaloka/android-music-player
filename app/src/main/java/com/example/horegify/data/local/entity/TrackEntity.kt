package com.example.horegify.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.horegify.data.model.Track

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Int,
    val artworkUrl: String,
    val thumbnailUrl: String,
    val isFavorite: Boolean = false,
    val playedAt: Long = 0L
)
