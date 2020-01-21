package com.example.ilovezappos.utils

import java.net.InetAddress
import java.net.UnknownHostException
import java.text.SimpleDateFormat

class Utilities {

    fun isInternetAvailable(): Boolean {
        try {
            val address = InetAddress.getByName("www.google.com")
            return !address.equals("")
        } catch (e: UnknownHostException) {

        }
        return false
    }

    fun convertMillisToDateString(seconds: Float): String {
        val formatter = SimpleDateFormat("hh:mm:ss");
        val dateString = formatter.format(seconds);
        return dateString
    }
}