package com.madeean.livestream.data.remotesource

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObj {
  private const val BASE_URL =
    "https://1dcd6b126c49-12390209840656915252.ngrok-free.app/livestreamapi/"
  private const val BASE_URLM =
    "http://10.4.76.139:3001/livestreamapi/"

  private fun retrofitClient(): Retrofit {
    val loggingInterceptor =
      HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val client = OkHttpClient.Builder()
      .addInterceptor(loggingInterceptor)
      .build()

    return Retrofit.Builder()
      .baseUrl(BASE_URLM)
      .addConverterFactory(GsonConverterFactory.create())
      .client(client)
      .build()
  }

  fun apiService(): ApiService {
    return retrofitClient().create(ApiService::class.java)
  }
}