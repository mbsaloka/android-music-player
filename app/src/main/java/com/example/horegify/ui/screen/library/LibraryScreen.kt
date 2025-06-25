package com.example.horegify.ui.screen.library

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.horegify.data.local.AppDatabase
import com.example.horegify.data.repository.MusicRepository
import com.example.horegify.ui.components.TrackCard
import com.example.horegify.data.model.Track

@Composable
fun LibraryScreen(
    onNavigateToPlayer: (Track) -> Unit,
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val database = AppDatabase.getDatabase(application)
    val trackDao = database.trackDao()

    val repository = remember { MusicRepository(trackDao) }

    val viewModel: LibraryViewModel = viewModel(
        factory = LibraryViewModelFactory(repository)
    )

    val tracks by viewModel.savedTracks.collectAsState()

    LibraryScreenContent(
        tracks = tracks,
        onNavigateToPlayer = onNavigateToPlayer
    )
}

@Composable
fun LibraryScreenContent(
    tracks: List<Track>,
    onNavigateToPlayer: (Track) -> Unit
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
        Text(
            text = "Your Library",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(tracks) { track ->
                TrackCard(
                    track = track,
                    onTrackClick = onNavigateToPlayer,
                    onMoreClick = {},
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewLibraryScreen() {
    com.example.horegify.ui.theme.HoregifyTheme {
        Surface {
            // Dummy data preview
            LibraryScreenContent(
                tracks = listOf(
                    Track(id = "1", title = "Sample Track 1", artist = "Artist A", album = "Album A", duration = 200),
                    Track(id = "2", title = "Sample Track 2", artist = "Artist B", album = "Album B", duration = 180),
                ),
                onNavigateToPlayer = {}
            )
        }
    }
}
