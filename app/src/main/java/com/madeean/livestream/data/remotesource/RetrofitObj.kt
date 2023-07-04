package com.madeean.livestream.data.remotesource

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitObj {
    private val BASE_URL = "www.dns.com/livestreamapi/obs"
    val retrofit: Retrofit.Builder by lazy {
        retrofit.apply {
            baseUrl(BASE_URL)
            converterFactories()
            addConverterFactory(GsonConverterFactory.create())
            build()
        }
    }
}