package com.example.horegify.ui.screen.genre

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.horegify.data.model.Track
import com.example.horegify.ui.components.SectionTitle
import com.example.horegify.ui.components.TrackCard
import com.example.horegify.ui.components.TrackCardGrid

private val genreColors = mapOf(
    "pop" to listOf(Color(0xFFEC4899), Color(0xFF9333EA)),
    "rock" to listOf(Color(0xFF4B5563), Color(0xFF1F2937)),
    "jazz" to listOf(Color(0xFFF59E0B), Color(0xFFEA580C)),
    "electronic" to listOf(Color(0xFF06B6D4), Color(0xFF2563EB)),
    "hip-hop" to listOf(Color(0xFF10B981), Color(0xFF047857)),
    "classical" to listOf(Color(0xFFA855F7), Color(0xFF6B21A8))
)

@Composable
fun GenreScreen(
    viewModel: GenreViewModel,
    onTrackClick: (Track) -> Unit,
    onRefresh: () -> Unit,
    onBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()

    val genreKey = uiState.value.genreTitle.lowercase()
    val topColors = genreColors[genreKey] ?: listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.primary
    )
    val fullGradient = topColors + List(6) { MaterialTheme.colorScheme.background }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(fullGradient))
    ) {
        when {
            uiState.value.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }

            uiState.value.errorMessage != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error: ${uiState.value.errorMessage}", color = MaterialTheme.colorScheme.error)
                    Button(onClick = onRefresh, Modifier.padding(top = 16.dp)) {
                        Text("Retry", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }

            else -> LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header with back button
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { onBack() }
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = uiState.value.genreTitle,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Text(
                        text = uiState.value.genreDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Section For You
                if (uiState.value.forYouTracks.isNotEmpty()) {
                    item { SectionTitle("For you") }
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(end = 16.dp)
                        ) {
                            items(uiState.value.forYouTracks) { track ->
                                TrackCardGrid(track = track, onTrackClick = onTrackClick)
                            }
                        }
                    }
                }

                // Section Discover
                if (uiState.value.discoverTracks.isNotEmpty()) {
                    item { SectionTitle("Discover") }
                    items(uiState.value.discoverTracks) { track ->
                        TrackCard(
                            track = track,
                            onTrackClick = onTrackClick,
                            onMoreClick = {},
                            modifier = Modifier.padding(bottom = 0.dp)
                        )
                    }
                }
            }
        }
    }
}
