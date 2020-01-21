package com.example.ilovezappos.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.ilovezappos.R
import com.example.ilovezappos.models.CurrentPrice
import com.example.ilovezappos.networking.BitStampRepository
import com.example.ilovezappos.utils.Utilities
import com.example.ilovezappos.viewmodels.PriceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : CustomActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        CoroutineScope(IO).launch {
            saveCurrentPrice()
        }
    }

    private suspend fun saveCurrentPrice() {
        if (Utilities().isInternetAvailable()) {
            var priceViewModel = ViewModelProviders.of(this).get(PriceViewModel::class.java)
            priceViewModel.init()


            val call = BitStampRepository().bitStampAPI!!.getPresentPrice()
            call.enqueue(object : Callback<CurrentPrice> {
                override fun onFailure(call: Call<CurrentPrice>?, t: Throwable?) {

                }

                override fun onResponse(
                    call: Call<CurrentPrice>?,
                    response: Response<CurrentPrice>?
                ) {
                    response?.let {
                        val data = it.body()
                        val sharedPreference =
                            getSharedPreferences("com.example.ilovezappos", Context.MODE_PRIVATE)
                        var editor = sharedPreference.edit()
                        editor.putFloat("currentPrice", data.last.toFloat())
                        editor.commit()
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }
                }
            })


        }
    }
}
