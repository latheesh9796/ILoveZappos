package com.example.ilovezappos.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class CustomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.let { actionBar ->
            actionBar.hide()
        }
    }
}