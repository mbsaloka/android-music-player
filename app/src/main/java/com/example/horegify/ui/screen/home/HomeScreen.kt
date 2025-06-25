package com.example.horegify.ui.screen.home

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.horegify.data.local.AppDatabase
import com.example.horegify.data.model.Track
import com.example.horegify.data.repository.MusicRepository
import com.example.horegify.ui.components.HoregifyTopBar
import com.example.horegify.ui.components.SectionTitle
import com.example.horegify.ui.components.TrackCard
import com.example.horegify.ui.components.TrackCardGrid
import com.example.horegify.ui.components.TrackCardHalf
import com.example.horegify.ui.theme.HoregifyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToPlayer: (Track) -> Unit,
    onNavigateToGenre: (String) -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val database = AppDatabase.getDatabase(application)
    val trackDao = database.trackDao()

    val repository = remember { MusicRepository(trackDao) }

    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(repository)
    )

    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(
        uiState = uiState,
        isDarkMode = isDarkTheme,
        onToggleTheme = onToggleTheme,
        onRefresh = { viewModel.refresh() },
        onNavigateToPlayer = onNavigateToPlayer,
        onNavigateToGenre = onNavigateToGenre
    )
}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    onRefresh: () -> Unit,
    onNavigateToPlayer: (Track) -> Unit,
    onNavigateToGenre: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background,
                    )
                )
            )
    ) {
        HoregifyTopBar(
            isDarkMode = isDarkMode,
            onThemeToggle = onToggleTheme,
            onRefresh = onRefresh
        )

        when {
            uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }

            uiState.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                    Button(onClick = onRefresh, Modifier.padding(top = 16.dp)) {
                        Text("Retry", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }

            else -> LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Recently Played
                if (uiState.recentTracks.isNotEmpty()) {
                    item { SectionTitle("Recently Played") }
                    item {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 100.dp, max = 200.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 4.dp)
                        ) {
                            items(uiState.recentTracks.shuffled().take(4)) { track ->
                                TrackCardHalf(track = track, onTrackClick = onNavigateToPlayer)
                            }
                        }
                    }
                }

                // Popular
                if (uiState.popularTracks.isNotEmpty()) {
                    item { SectionTitle("Popular Right Now") }
                    items(uiState.popularTracks.shuffled()) { track ->
                        TrackCard(track = track, onTrackClick = onNavigateToPlayer, onMoreClick = {}, modifier = Modifier.padding(bottom = 0.dp))
                    }
                }

                // Recommended
                if (uiState.recommendedTracks.isNotEmpty()) {
                    item {
                        SectionTitle("Recommended for You")
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(end = 16.dp)) {
                            items(uiState.recommendedTracks) { track ->
                                TrackCardGrid(track = track, onTrackClick = onNavigateToPlayer)
                            }
                        }
                    }
                }

                // Genres
                item {
                    SectionTitle("Browse Genres")
                    Spacer(modifier = Modifier.height(16.dp))

                    val genres = listOf(
                        "Pop" to listOf(Color(0xFFEC4899), Color(0xFF9333EA)),
                        "Rock" to listOf(Color(0xFF4B5563), Color(0xFF1F2937)),
                        "Jazz" to listOf(Color(0xFFF59E0B), Color(0xFFEA580C)),
                        "Electronic" to listOf(Color(0xFF06B6D4), Color(0xFF2563EB))
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(end = 16.dp)) {
                        items(genres) { (genre, colors) ->
                            Card(
                                modifier = Modifier.width(140.dp).height(80.dp),
                                shape = RoundedCornerShape(16.dp),
                                onClick = { onNavigateToGenre(genre.lowercase()) }
                            ) {
                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.radialGradient(
                                                colors = colors,
                                                center = Offset(0f, 0f),
                                                radius = 300f
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(genre, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreenContent() {
    val dummyTracks = listOf(
        Track("1", "Horeg Anthem", "DJ Koplo", "https://picsum.photos/200?1"),
        Track("2", "Goyang Lagi", "MC Dangdut", "https://picsum.photos/200?2"),
        Track("3", "Bass Galau", "Remix Lord", "https://picsum.photos/200?3"),
        Track("4", "Koplo In The Sky", "DJ Woles", "https://picsum.photos/200?4"),
//        Track("5", "Santuy Beat", "Lofi Dangdut", "https://picsum.photos/200?5"),
//        Track("6", "Pagi Pagi Party", "Remix Oke", "https://picsum.photos/200?6"),
//        Track("7", "Ngebeat Santai", "DJ Malam", "https://picsum.photos/200?7"),
//        Track("8", "Goyang Kopi", "Koplo Bros", "https://picsum.photos/200?8")
    )

    val uiState = HomeUiState(
        isLoading = false,
        error = null,
        popularTracks = dummyTracks,
        recentTracks = dummyTracks,
        recommendedTracks = dummyTracks
    )

    HoregifyTheme  {
        HomeScreenContent(
            uiState = uiState,
            isDarkMode = false,
            onToggleTheme = {},
            onRefresh = {},
            onNavigateToPlayer = {},
            onNavigateToGenre = {}
        )
    }
}

