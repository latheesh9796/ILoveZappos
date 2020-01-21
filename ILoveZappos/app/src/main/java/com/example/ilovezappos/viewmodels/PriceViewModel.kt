package com.example.ilovezappos.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ilovezappos.models.CurrentPrice
import com.example.ilovezappos.models.Price
import com.example.ilovezappos.networking.BitStampRepository

class PriceViewModel : ViewModel() {

    var priceData: MutableLiveData<List<Price>>? = null
    var bitStampRepository: BitStampRepository? = null

    fun init() {
        if (priceData != null) {
            return
        }
        bitStampRepository = BitStampRepository().getInstance()
    }

    fun getPriceList() {
        priceData = bitStampRepository!!.getPriceList()
    }

    fun priceData(): LiveData<List<Price>> {
        return priceData as LiveData<List<Price>>
    }

}