package com.example.surovets_kotlin_lv1_2

import retrofit2.http.GET

interface GifsService {
    @GET("v1/gifs/search?api_key=zIzLK1XZS3iTh2nWVe1LhMdS3QyQcVsj&q=animal")
    suspend fun getGifs(): retrofit2.Response<Gifs>
}