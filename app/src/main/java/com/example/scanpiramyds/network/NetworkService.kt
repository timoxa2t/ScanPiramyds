package com.example.scanpiramyds.network


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkService {


    private var mRetrofit: Retrofit


    constructor(retrofit: Retrofit) {
        mRetrofit = retrofit
    }

    companion object {
        private var INSTANCE: NetworkService? = null
        private val BASE_URL = "https://193.151.89.131:3000"

        fun getNetworkInstance(): NetworkService {
            if (INSTANCE === null) {
                INSTANCE = NetworkService(
                    Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                )
            }
            return INSTANCE as NetworkService
        }
    }

    fun getJSONApi(): JSONPlaceholderAPI {
        return mRetrofit.create(JSONPlaceholderAPI::class.java)
    }
}

