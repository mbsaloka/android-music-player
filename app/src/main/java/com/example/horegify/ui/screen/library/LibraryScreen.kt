package com.example.horegify.ui.screen.library

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.horegify.data.model.Track
import com.example.horegify.ui.components.TrackCard

@Composable
fun LibraryScreen(
    onNavigateToPlayer: (Track) -> Unit,
    viewModel: LibraryViewModel = viewModel()
) {
    val tracks by viewModel.savedTracks.collectAsState()

    Column(modifier = Modifier
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

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(tracks) { track ->
                TrackCard(track = track, onTrackClick = onNavigateToPlayer, onMoreClick = {}, modifier = Modifier.padding(horizontal = 16.dp))
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
            LibraryScreen(onNavigateToPlayer = {})
        }
    }
}
