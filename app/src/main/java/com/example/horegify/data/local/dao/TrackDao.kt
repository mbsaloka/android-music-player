package com.example.horegify.data.local.dao

import androidx.room.*
import com.example.horegify.data.local.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    // Recently Played
    @Query("SELECT * FROM tracks WHERE playedAt > 0 ORDER BY playedAt DESC LIMIT 4")
    fun getRecentlyPlayed(): Flow<List<TrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(track: TrackEntity)

    // Favorites
    @Query("SELECT * FROM tracks WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Query("UPDATE tracks SET isFavorite = 1 WHERE id = :trackId")
    suspend fun addToFavorites(trackId: String)

    @Query("UPDATE tracks SET isFavorite = 0 WHERE id = :trackId")
    suspend fun removeFromFavorites(trackId: String)

    @Query("SELECT CASE WHEN isFavorite IS NULL THEN 0 ELSE isFavorite END FROM tracks WHERE id = :trackId LIMIT 1")
    fun isFavoriteTrack(trackId: String): Flow<Boolean?>

    @Query("SELECT CASE WHEN isFavorite IS NULL THEN 0 ELSE isFavorite END FROM tracks WHERE id = :trackId LIMIT 1")
    suspend fun isFavoriteTrackImmediate(trackId: String): Boolean?

    // Get Track by ID
    @Query("SELECT * FROM tracks WHERE id = :trackId LIMIT 1")
    suspend fun getTrackById(trackId: String): TrackEntity?

}
