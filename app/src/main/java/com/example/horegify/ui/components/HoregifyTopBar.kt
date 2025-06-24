package com.example.horegify.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horegify.ui.theme.HoregifyTheme

@Composable
fun HoregifyTopBar(
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            val greeting = getGreeting()

            Text(
                text = greeting,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 32.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Have a vibe! 🎵",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp
                )
            )
        }

        Row {
            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh"
                )
            }
            IconButton(onClick = onThemeToggle) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.Brightness7 else Icons.Default.Brightness4,
                    contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode"
                )
            }
        }
    }
}

private fun getGreeting(): String {
    val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5..11 -> "Good morning"
        in 12..17 -> "Good afternoon"
        in 18..21 -> "Good evening"
        else -> "Good night"
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHoregifyTopBar() {
    var isDark by remember { mutableStateOf(false) }

    HoregifyTheme {
        Surface {
            HoregifyTopBar(
                isDarkMode = isDark,
                onThemeToggle = { isDark = !isDark },
                onRefresh = {}
            )
        }
    }
}
