package com.example.ilovezappos.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ilovezappos.R
import com.example.ilovezappos.adapters.OrderBookAdapter
import com.example.ilovezappos.utils.Utilities
import com.example.ilovezappos.viewmodels.OrderBookViewModel
import kotlinx.android.synthetic.main.fragment_order_book.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class OrderBookFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order_book, container, false)
    }

    companion object {
        fun newInstance(): OrderBookFragment =
            OrderBookFragment()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        CoroutineScope(IO).launch {
            setupRecyclerView()
        }
    }

    private suspend fun setupRecyclerView() {

        if(Utilities().isInternetAvailable()){
            hideView()
            var orderBookViewModel = ViewModelProviders.of(this).get(OrderBookViewModel::class.java)
            orderBookViewModel.init()
            orderBookViewModel.getOrderBookEntries()
            CoroutineScope(Main).launch {
                orderBookViewModel.orderBookData().observe(viewLifecycleOwner, Observer {
                    it?.let {
                        if(it != null) {
                            orderBookRecyclerview.layoutManager = LinearLayoutManager(context)
                            orderBookRecyclerview.adapter = OrderBookAdapter(context!!,it!!)
                        }
                        alert_progress_bar.visibility = View.GONE
                        tableHeader.visibility = View.VISIBLE
                    }
                })
            }

        }
    }

    private suspend fun hideView(){
        CoroutineScope(Main).launch {
            alert_progress_bar.visibility = View.VISIBLE
            tableHeader.visibility = View.GONE
        }
    }
}
