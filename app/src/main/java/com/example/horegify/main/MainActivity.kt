package com.example.horegify.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.horegify.data.local.AppDatabase
import com.example.horegify.data.repository.MusicRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val database = AppDatabase.getDatabase(this)
            val repository = MusicRepository(database.trackDao())

            HoregifyApp(repository)
        }
    }
}
