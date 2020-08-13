package com.example.scanpiramyds.network

import com.example.scanpiramyds.database.Piramyd
import retrofit2.Call
import retrofit2.http.GET

interface JSONPlaceholderAPI {
    @GET("/piramyds")
    fun getPiramyds(): Call<List<Piramyd>>
}