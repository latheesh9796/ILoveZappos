package com.example.ilovezappos.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ilovezappos.R
import kotlinx.android.synthetic.main.fragment_price.*
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ilovezappos.models.Price
import com.example.ilovezappos.utils.CustomValueFormatter
import com.example.ilovezappos.utils.Utilities
import com.example.ilovezappos.viewmodels.PriceViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 */
class PriceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(com.example.ilovezappos.R.layout.fragment_price, container, false)
    }

    companion object {
        fun newInstance(): PriceFragment =
            PriceFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            setLineChart()
        }
    }

    private suspend fun setLineChart() {
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        if (Utilities().isInternetAvailable()) {
            hideView()
            var priceViewModel = ViewModelProviders.of(this).get(PriceViewModel::class.java)
            priceViewModel.init()
            priceViewModel.getPriceList()
            CoroutineScope(Main).launch {
                priceViewModel.priceData().observe(viewLifecycleOwner, Observer {
                    it?.let {
                        if (it != null) {
                            renderLineData(it)
                        }
                        price_list_progress_bar.visibility = View.GONE
                        lineChart.visibility = View.VISIBLE
                    }
                })
            }
        }
    }

    private suspend fun hideView() {
        CoroutineScope(Main).launch {
            price_list_progress_bar.visibility = View.VISIBLE
            lineChart.visibility = View.GONE
        }
    }

    private fun renderLineData(list: List<Price>) {
        val buyEntries = ArrayList<Entry>()
        val sellEntries = ArrayList<Entry>()
        for (price in list) {
            if (price.type.equals(("1"))) {
                sellEntries.add(Entry((price.date.toFloat() * 1000), price.price.toFloat()))
            } else {
                buyEntries.add(Entry((price.date.toFloat() * 1000), price.price.toFloat()))
            }
        }

        //Buy Price Entries
        val vl = LineDataSet(buyEntries, "Buy Price")
        vl.setDrawValues(true)
        vl.setDrawFilled(false)
        vl.lineWidth = 3f
        vl.color = Color.parseColor("#4BB473")
        vl.fillAlpha = Color.parseColor("#4BB473");
        vl.setCircleColor(R.color.colorGreen)
        vl.circleHoleColor = Color.parseColor("#4BB473")

        //Sell Price Entries
        val vb = LineDataSet(sellEntries, "Sell Price")
        vb.setDrawValues(true)
        vb.setDrawFilled(false)
        vb.lineWidth = 3f
        vb.color = Color.parseColor("#FF6A6A")
        vb.fillAlpha = Color.parseColor("#FF6A6A");
        vb.setCircleColor(R.color.colorRed)
        vb.circleHoleColor = Color.parseColor("#FF6A6A")
        val xAxisValues = lineChart.xAxis
        xAxisValues.setPosition(XAxis.XAxisPosition.TOP);
        xAxisValues.setDrawGridLines(true);
        xAxisValues.setGranularity(1f);
        xAxisValues.setGranularityEnabled(true);
        xAxisValues.valueFormatter = CustomValueFormatter()
        lineChart.legend.isEnabled = true
        val lineData = LineData()
        lineData.addDataSet(vl)
        lineData.addDataSet(vb)
        lineChart.data = lineData
        lineChart.axisRight.isEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.description.text = "Bitcoin Price vs Time"
        lineChart.setNoDataText("No Data Available!")
        lineChart.animateX(500, Easing.EaseInExpo)
        lineChart.invalidate()
    }

}


