package com.example.horegify.data.model

data class Track(
    val id: String,
    val name: String,
    val artist: String,
    val image: String
)

val dummyTracks = listOf(
    Track("1", "Horeg Anthem", "DJ Koplo", "https://picsum.photos/200?1"),
    Track("2", "Goyang Lagi", "MC Dangdut", "https://picsum.photos/200?2"),
    Track("3", "Bass Galau", "Remix Lord", "https://picsum.photos/200?3"),
    Track("4", "Koplo In The Sky", "DJ Woles", "https://picsum.photos/200?4"),
    Track("5", "Santuy Beat", "Lofi Dangdut", "https://picsum.photos/200?5"),
    Track("6", "Pagi Pagi Party", "Remix Oke", "https://picsum.photos/200?6"),
    Track("7", "Ngebeat Santai", "DJ Malam", "https://picsum.photos/200?7"),
    Track("8", "Goyang Kopi", "Koplo Bros", "https://picsum.photos/200?8")
)