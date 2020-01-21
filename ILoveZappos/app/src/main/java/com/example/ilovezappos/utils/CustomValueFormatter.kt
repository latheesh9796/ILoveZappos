package com.example.ilovezappos.utils

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class CustomValueFormatter : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        if (value > 1000000f) {
            return Utilities().convertMillisToDateString(value)
        } else {
            return value.toString()
        }
    }
}