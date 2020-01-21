package com.example.ilovezappos.networking

import androidx.lifecycle.MutableLiveData
import com.example.ilovezappos.models.OrderBook
import com.example.ilovezappos.models.Price
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BitStampRepository {
    var bitStampRepository: BitStampRepository? = null

    fun getInstance(): BitStampRepository {
        if (bitStampRepository == null) {
            return BitStampRepository() as BitStampRepository
        }
        return bitStampRepository as BitStampRepository
    }

    var bitStampAPI: BitStampAPI? = null

    init {
        bitStampAPI = RetrofitService().createService(BitStampAPI::class.java)
    }

    fun getOrderBookEntries(): MutableLiveData<OrderBook> {
        val call: Call<OrderBook> = bitStampAPI!!.getOrderBookEntries()
        val data: MutableLiveData<OrderBook> = MutableLiveData()
        call.enqueue(object : Callback<OrderBook> {
            override fun onFailure(call: Call<OrderBook>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<OrderBook>?, response: Response<OrderBook>?) {
                response?.let {
                    data.value = it.body()
                }
            }
        })
        return data
    }

    fun getPriceList(): MutableLiveData<List<Price>> {
        val call: Call<List<Price>> = bitStampAPI!!.getPriceList()
        val data: MutableLiveData<List<Price>> = MutableLiveData()
        call.enqueue(object : Callback<List<Price>> {
            override fun onFailure(call: Call<List<Price>>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<List<Price>>?, response: Response<List<Price>>?) {
                response?.let {
                    data.value = it.body()
                }
            }
        })
        return data
    }

}