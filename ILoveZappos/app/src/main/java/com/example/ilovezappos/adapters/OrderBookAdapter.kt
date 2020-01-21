package com.example.ilovezappos.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ilovezappos.R
import com.example.ilovezappos.models.OrderBook
import kotlinx.android.synthetic.main.cell_orderbook_entry.view.*

class OrderBookAdapter(val context: Context, val orderBook: OrderBook) :
    RecyclerView.Adapter<OrderBookAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.cell_orderbook_entry, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        if (position < orderBook.asks.size) {
            holder.askAmount.text = "%.8f".format(orderBook.asks.get(position).get(1))
            holder.askPrice.text = "%.2f".format(orderBook.asks.get(position).get(0))
        } else {
            holder.askAmount.text = "--"
            holder.askPrice.text = "--"
        }
        if (position < orderBook.bids.size) {
            holder.bidAmount.text = "%.8f".format(orderBook.bids.get(position).get(1))
            holder.bidPrice.text = "%.2f".format(orderBook.bids.get(position).get(0))
        } else {
            holder.bidAmount.text = "--"
            holder.bidPrice.text = "--"
        }
    }

    override fun getItemCount(): Int {
        return if (orderBook.asks.size > orderBook.bids.size) orderBook.asks.size else orderBook.bids.size
    }

    class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bidAmount = view.bidAmountTV
        val bidPrice = view.bidPriceTV
        val askAmount = view.askAmountTV
        val askPrice = view.asklPriceTV
    }
}
