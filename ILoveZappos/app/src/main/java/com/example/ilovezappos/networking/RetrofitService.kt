package com.example.ilovezappos.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    private val url = "https://www.bitstamp.net/api/v2/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    public fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }
}