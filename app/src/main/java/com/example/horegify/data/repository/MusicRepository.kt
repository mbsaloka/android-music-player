package com.example.horegify.data.repository

import com.example.horegify.data.api.ApiClient
import com.example.horegify.data.api.mapper.toModel
import com.example.horegify.data.local.dao.TrackDao
import com.example.horegify.data.local.mapper.toEntity
import com.example.horegify.data.local.mapper.toTrack
import com.example.horegify.data.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

open class MusicRepository(
    private val dao: TrackDao,
    private val clientId: String = "e83eeada"
) {
    private val api = ApiClient.apiService

    // 1. GET Recommended Tracks (Random)
    suspend fun getRecommendedTracks(): List<Track> = withContext(Dispatchers.IO) {
        val limit = 10
        val totalTrackCount = 1000
        val maxOffset = totalTrackCount - limit
        val randomOffset = (0..maxOffset).random()
        api.getTracks(clientId, limit = limit, offset = randomOffset).results.map { it.toModel() }
    }

    // 2. GET Popular Tracks (fallback to latest if not available)
    suspend fun getPopularTracks(): List<Track> = withContext(Dispatchers.IO) {
        api.getTracks(clientId, limit = 3).results.map { it.toModel() }
    }

    // 3. SEARCH Tracks
    suspend fun searchTracks(query: String): List<Track> = withContext(Dispatchers.IO) {
        api.searchTracks(clientId, query = query).results.map { it.toModel() }
    }

    // 4. Recently Played (GET)
    fun getRecentlyPlayedTracks(): Flow<List<Track>> {
        return dao.getRecentlyPlayed().map { list -> list.map { it.toTrack() } }
    }

    // 5. Recently Played (ADD)
    suspend fun addToRecentlyPlayed(track: Track) {
        val entity = track.toEntity(playedAt = System.currentTimeMillis())
        dao.insertOrUpdate(entity)
    }

    // 6. Favorite Tracks (GET)
    fun getFavoriteTracks(): Flow<List<Track>> {
        return dao.getFavoriteTracks().map { list -> list.map { it.toTrack() } }
    }

    // 7. Favorite Tracks (ADD)
    suspend fun addToFavorites(trackId: String) {
        dao.addToFavorites(trackId)
    }

    // 8. Favorite Tracks (REMOVE)
    suspend fun removeFromFavorites(trackId: String) {
        dao.removeFromFavorites(trackId)
    }

    // 9. Get Track by ID
    suspend fun getTrackById(id: String): Track? = withContext(Dispatchers.IO) {
        api.getTrackById(clientId, trackId = id).results.firstOrNull()?.toModel()
    }

    // 10. Favorite Toggle
    fun isFavorite(trackId: String): Flow<Boolean> {
        return dao.isFavoriteTrack(trackId)
            .map { it ?: false }
    }

    suspend fun toggleFavorite(id: String) {
        val isCurrentlyFavorite = dao.isFavoriteTrackImmediate(id) ?: false
        if (isCurrentlyFavorite) {
            removeFromFavorites(id)
        } else {
            addToFavorites(id)
        }
    }
}
