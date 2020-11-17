package com.example.scanpiramyds.network


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.InputStream
import java.net.URL
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.security.cert.X509Certificate

class NetworkService {


    private var mRetrofit: Retrofit


    constructor(retrofit: Retrofit) {
        mRetrofit = retrofit
    }

    companion object {
        private var INSTANCE: NetworkService? = null
        private val BASE_URL = ""

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

