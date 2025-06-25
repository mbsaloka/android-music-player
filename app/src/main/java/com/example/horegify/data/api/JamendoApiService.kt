package com.example.horegify.data.api

import com.example.horegify.data.api.response.TrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface JamendoApiService {

    @GET("tracks")
    suspend fun getTracks(
        @Query("client_id") clientId: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): TrackSearchResponse

    @GET("tracks")
    suspend fun searchTracks(
        @Query("client_id") clientId: String,
        @Query("format") format: String = "json",
        @Query("search") query: String,
        @Query("limit") limit: Int = 10
    ): TrackSearchResponse

    @GET("tracks")
    suspend fun getPopularTracks(
        @Query("client_id") clientId: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 10,
        @Query("order") order: String = "popularity_week"
    ): TrackSearchResponse

    @GET("tracks")
    suspend fun getTrackById(
        @Query("client_id") clientId: String,
        @Query("format") format: String = "json",
        @Query("id") trackId: String
    ): TrackSearchResponse

    @GET("tracks")
    suspend fun getTracksByGenre(
        @Query("client_id") clientId: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 30,
        @Query("search") genreQuery: String,
    ): TrackSearchResponse
}
