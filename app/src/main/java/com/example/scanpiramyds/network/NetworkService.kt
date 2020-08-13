package com.example.scanpiramyds.network


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkService{

    private var INSTANCE: NetworkService? = null
    private lateinit var mRetrofit: Retrofit
    private val BASE_URL = "https://jsonplaceholder.typicode.com"

    constructor(retrofit: Retrofit)  {
        mRetrofit = retrofit
    }

    fun getNetworkInstance(): NetworkService{
        if(INSTANCE === null){
            INSTANCE = NetworkService(
                Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build())
        }
        return INSTANCE as NetworkService
    }

    fun getJSONApi(): JSONPlaceholderAPI{
        return mRetrofit.create(JSONPlaceholderAPI::class.java)
    }

//    companion object {
//        private var INSTANCE: NetworkService? = null
//
//
//        fun getInstance(context: Context, coroutineScope: CoroutineScope): NetworkService {
//            return INSTANCE? : synchronized(this) {
//                val instance = NetworkService()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}
