package com.example.ilovezappos.networking

import com.example.ilovezappos.models.CurrentPrice
import com.example.ilovezappos.models.OrderBook
import com.example.ilovezappos.models.Price
import retrofit2.Call
import retrofit2.http.GET

interface BitStampAPI  {

    @GET("order_book/btcusd/")
    fun getOrderBookEntries(): Call<OrderBook>

    @GET("transactions/btcusd/")
    fun getPriceList() : Call<List<Price>>

    @GET("ticker_hour/btcusd/")
    fun getPresentPrice() : Call<CurrentPrice>
}