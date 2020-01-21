package com.example.ilovezappos.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.ilovezappos.fragments.OrderBookFragment
import com.example.ilovezappos.fragments.PriceFragment
import com.example.ilovezappos.R
import com.example.ilovezappos.widgets.AlertBottomSheet
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : CustomActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNavBar()
        setupListeners()
    }

    private fun setupListeners() {
        alertIcon.setOnClickListener {
            val alertBottomSheet = AlertBottomSheet()
            alertBottomSheet.show(supportFragmentManager, "ALERT")
        }
    }

    private fun setupBottomNavBar() {
        bottomNavigationBar.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener() { it ->
            when (it.itemId) {
                R.id.price -> {
                    titleBar.text = "Price Graph"
                    openFragment(PriceFragment.newInstance())
                }
                R.id.orderBook -> {
                    titleBar.text = "Order Book"
                    openFragment(OrderBookFragment.newInstance())
                }
            }
            true
        })
        bottomNavigationBar.selectedItemId = R.id.price
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
