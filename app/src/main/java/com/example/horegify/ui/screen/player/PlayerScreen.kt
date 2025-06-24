package com.example.horegify.ui.screen.player

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.horegify.data.model.RepeatMode
import com.example.horegify.ui.theme.HoregifyTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun PlayerScreen(
    onBack: () -> Unit,
    viewModel: PlayerViewModel = viewModel()
) {
    val ui = viewModel.uiState.collectAsState().value

    val gradientBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to MaterialTheme.colorScheme.primary, // 0% - Dark Blue
            0.2f to MaterialTheme.colorScheme.primary, // 40% - Dark Blue
            0.4f to Color(0xFF864FDA),                 // 50% - Purple
            0.9f to MaterialTheme.colorScheme.primary, // 60% - Dark Blue
            1.0f to MaterialTheme.colorScheme.primary  // 100% - Dark Blue
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
                Text("RECOMMENDED FOR YOU", style = MaterialTheme.typography.labelLarge.copy(color = Color.White))
                IconButton(onClick = { /* More */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.White)
                }
            }

            Spacer(Modifier.height(24.dp))

            val painter: Painter = rememberAsyncImagePainter(model = ui.thumbnailUrl)
            val isPlaceholder = painter is AsyncImagePainter && painter.state is AsyncImagePainter.State.Empty

            Box(
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.LightGray)
            ) {
                if (!isPlaceholder) {
                    Image(
                        painter = painter,
                        contentDescription = ui.title,
                        contentScale = ContentScale.Crop,
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
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = ui.title,
                        style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = ui.artist,
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.7f)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = { /* Favorite toggle */ }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite",
                        tint = Color.Red
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Seek Bar (music progress)
            Slider(
                value = ui.currentTime.toFloat(),
                onValueChange = { viewModel.seekTo(it.toInt()) },
                valueRange = 0f..ui.duration.toFloat(),
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
                Text(formatTime(ui.currentTime), style = MaterialTheme.typography.bodySmall.copy(color = Color.White))
                Text(formatTime(ui.duration), style = MaterialTheme.typography.bodySmall.copy(color = Color.White))
            }

            Spacer(Modifier.height(16.dp))

            // Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = viewModel::toggleShuffle) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (ui.isShuffled) Color.White else Color.White.copy(alpha = 0.5f)
                    )
                }
                IconButton(onClick = { /* prev */ }) {
                    Icon(Icons.Default.SkipPrevious, contentDescription = "Prev", tint = Color.White)
                }
                IconButton(
                    onClick = viewModel::togglePlayPause,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = if (ui.isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                        contentDescription = "Play/Pause",
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                }
                IconButton(onClick = { /* next */ }) {
                    Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = Color.White)
                }
                IconButton(onClick = viewModel::toggleRepeat) {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "Repeat",
                        tint = if (ui.repeatMode != RepeatMode.OFF) Color.White else Color.White.copy(alpha = 0.5f)
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
                        value = ui.volume.toFloat(),
                        onValueChange = { viewModel.setVolume(it.toInt()) },
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
                        "${ui.volume}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

private fun formatTime(sec: Int): String {
    val m = sec / 60
    val s = sec % 60
    return "%d:%02d".format(m, s)
}

@Preview(showBackground = true)
@Composable
fun PreviewPlayerScreen() {
    val previewViewModel = object : ViewModel() {
        val uiState = MutableStateFlow(
            PlayerUiState(
                id = "5",
                title = "Blinding Lights",
                artist = "The Weeknd",
                album = "After Hours",
                duration = 200,
                currentTime = 45,
                isPlaying = true,
                isShuffled = false,
                repeatMode = com.example.horegify.data.model.RepeatMode.ONE,
                volume = 80,
                artworkUrl = "https://picsum.photos/300",
                thumbnailUrl = "https://picsum.photos/100"
            )
        )
    }

    HoregifyTheme {
        PlayerScreen(
            onBack = {},
            viewModel = object : PlayerViewModel(SavedStateHandle()) {
                override val uiState = previewViewModel.uiState
            }
        )
    }
}
