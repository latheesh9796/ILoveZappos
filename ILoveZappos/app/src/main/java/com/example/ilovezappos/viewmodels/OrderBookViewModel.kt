package com.example.ilovezappos.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ilovezappos.models.OrderBook
import com.example.ilovezappos.networking.BitStampRepository

class OrderBookViewModel : ViewModel() {

    var orderBookData: MutableLiveData<OrderBook>? = null
    var bitStampRepository: BitStampRepository? = null

    fun init() {
        if (orderBookData != null) {
            return
        }
        bitStampRepository = BitStampRepository().getInstance()
    }

    fun getOrderBookEntries() {
        orderBookData = bitStampRepository!!.getOrderBookEntries()
    }

    fun orderBookData(): LiveData<OrderBook> {
        return orderBookData as LiveData<OrderBook>
    }
}