package com.example.surovets_kotlin_lv1_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    val BASE_URL =
        "https://api.giphy.com/"

    private val gifsRetrofit by lazy { GifsRetrofit(BASE_URL) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GifsScreen(gifsRetrofit)
        }
    }
}