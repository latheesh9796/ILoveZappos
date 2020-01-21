package com.example.ilovezappos.models

data class OrderBook(
    val asks: List<List<Float>>,
    val bids: List<List<Float>>,
    val timestamp: String
)