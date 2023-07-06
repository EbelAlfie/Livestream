package com.madeean.livestream.data.remotesource

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObj {
    private val BASE_URL = "https://1dcd6b126c49-12390209840656915252.ngrok-free.app/livestreamapi/"
    /*https://1dcd6b126c49-12390209840656915252.ngrok-free.app
    * https://f8177cac75aa-8008867362113113571.ngrok-free.app*/
    private fun retrofitClient(): Retrofit {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun apiService(): ApiService{
        return retrofitClient().create(ApiService::class.java)
    }
}