package com.example.surovets_kotlin_lv1_2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GifsRetrofit(url: String) {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val gifsService: GifsService by lazy {
        retrofit.create(GifsService::class.java)
    }

    suspend fun requestGifs(url: String): Gifs? {
        val response = gifsService.getGifs()
        if (response.isSuccessful) {

            val body = response.body()
            if (body != null) {
                return body
            }
        }
        return null
    }
}

