package com.example.horegify.ui.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.horegify.ui.components.SearchBar
import com.example.horegify.ui.screen.search.SearchViewModel
import com.example.horegify.ui.theme.HoregifyTheme

@Composable
fun SearchScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    onGenreClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val genres = listOf(
        "Pop" to listOf(Color(0xFFEC4899), Color(0xFF9333EA)),
        "Rock" to listOf(Color(0xFF4B5563), Color(0xFF1F2937)),
        "Jazz" to listOf(Color(0xFFF59E0B), Color(0xFFEA580C)),
        "Electronic" to listOf(Color(0xFF06B6D4), Color(0xFF2563EB)),
        "Hip-Hop" to listOf(Color(0xFF10B981), Color(0xFF047857)),
        "Classical" to listOf(Color(0xFFA855F7), Color(0xFF6B21A8))
    )

    Column(
        modifier = modifier
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
            "Search",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Text(
            "Browse All",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(genres) { (genre, colors) ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    onClick = { onGenreClick(genre.lowercase()) },
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    colors = colors,
                                    center = Offset(0f, 0f),
                                    radius = 600f
                                )
                            ),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Text(
                            genre,
                            fontSize = 20.sp,
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchScreenWithViewModel(
    viewModel: SearchViewModel = viewModel(),
    onGenreClick: (String) -> Unit
) {
    val query by viewModel.searchQuery.collectAsState()
    SearchScreen(
        query = query,
        onQueryChange = viewModel::onSearchQueryChange,
        onGenreClick = onGenreClick
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    var query by remember { mutableStateOf("") }

    HoregifyTheme {
        SearchScreen(
            query = query,
            onQueryChange = { query = it },
            onGenreClick = {}
        )
    }
}
