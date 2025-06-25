package com.example.horegify.ui.screen.player

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.horegify.data.local.AppDatabase
import com.example.horegify.data.model.RepeatMode
import com.example.horegify.data.repository.MusicRepository
import com.example.horegify.ui.theme.HoregifyTheme
import com.example.horegify.utils.formatTime
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun PlayerScreen(
    onBack: () -> Unit,
    navBackStackEntry: NavBackStackEntry
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val database = AppDatabase.getDatabase(application)
    val trackDao = database.trackDao()

    val repository = remember { MusicRepository(trackDao) }

    val viewModel: PlayerViewModel = viewModel(
        navBackStackEntry,
        factory = PlayerViewModelFactory(repository, navBackStackEntry)
    )

    val uiState by viewModel.uiState.collectAsState()

    PlayerScreenContent(
        uiState = uiState,
        onBack = onBack,
        onTogglePlayPause = viewModel::togglePlayPause,
        onToggleShuffle = viewModel::toggleShuffle,
        onToggleRepeat = viewModel::toggleRepeat,
        onSeekTo = viewModel::seekTo,
        onSetVolume = viewModel::setVolume,
        onFavoriteClick = viewModel::toggleFavorite,
        isFavorite = viewModel.isFavorite.collectAsState().value
    )
}

@Composable
fun PlayerScreenContent(
    uiState: PlayerUiState,
    onBack: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onToggleShuffle: () -> Unit,
    onToggleRepeat: () -> Unit,
    onSeekTo: (Int) -> Unit,
    onSetVolume: (Int) -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean = false
) {
    val gradientBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to MaterialTheme.colorScheme.primary,
            0.2f to MaterialTheme.colorScheme.primary,
            0.4f to Color(0xFF864FDA),
            0.9f to MaterialTheme.colorScheme.primary,
            1.0f to MaterialTheme.colorScheme.primary
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { onBack() }) {
                    Icon(Icons.Default.ExpandMore, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    "RECOMMENDED FOR YOU",
                    style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
                )
                IconButton(onClick = { /* More */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.White)
                }
            }

            Spacer(Modifier.height(24.dp))

            val painter = rememberAsyncImagePainter(model = uiState.thumbnailUrl)
            val isPlaceholder = painter is AsyncImagePainter && painter.state is AsyncImagePainter.State.Empty

            Box(
                modifier = Modifier
                    .size(300.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(24.dp))
            ) {
                if (!isPlaceholder) {
                    Image(
                        painter = painter,
                        contentDescription = uiState.title,
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(Modifier.height(64.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = uiState.title,
                        style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Text(
                        text = uiState.artist,
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.7f)),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.White.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Seek Bar
            Slider(
                value = uiState.currentTime.toFloat(),
                onValueChange = { onSeekTo(it.toInt()) },
                valueRange = 0f..uiState.duration.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                )
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    formatTime(uiState.currentTime),
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                )
                Text(
                    formatTime(uiState.duration),
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onToggleShuffle) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (uiState.isShuffled) Color.White else Color.White.copy(alpha = 0.5f)
                    )
                }
                IconButton(onClick = { /* prev */ }) {
                    Icon(Icons.Default.SkipPrevious, contentDescription = "Prev", tint = Color.White)
                }
                IconButton(
                    onClick = onTogglePlayPause,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = if (uiState.isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                        contentDescription = "Play/Pause",
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                }
                IconButton(onClick = { /* next */ }) {
                    Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = Color.White)
                }
                IconButton(onClick = onToggleRepeat) {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "Repeat",
                        tint = if (uiState.repeatMode != RepeatMode.OFF) Color.White else Color.White.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Volume
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.VolumeUp,
                        contentDescription = "Volume",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Slider(
                        value = uiState.volume.toFloat(),
                        onValueChange = { onSetVolume(it.toInt()) },
                        valueRange = 0f..100f,
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = Color.White,
                            inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "${uiState.volume}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewPlayerScreen() {
//    val previewViewModel = object : ViewModel() {
//        val uiState = MutableStateFlow(
//            PlayerUiState(
//                id = "5",
//                title = "Blinding Lights",
//                artist = "The Weeknd",
//                album = "After Hours",
//                duration = 200,
//                currentTime = 45,
//                isPlaying = true,
//                isShuffled = false,
//                repeatMode = com.example.horegify.data.model.RepeatMode.ONE,
//                volume = 80,
//                artworkUrl = "https://picsum.photos/300",
//                thumbnailUrl = "https://picsum.photos/100"
//            )
//        )
//    }
//
//    HoregifyTheme {
//        PlayerScreen(
//            onBack = {},
//            viewModel = object : PlayerViewModel(SavedStateHandle(), ) {
//                override val uiState = previewViewModel.uiState
//            }
//        )
//    }
//}
