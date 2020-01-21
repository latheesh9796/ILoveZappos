package com.example.ilovezappos.widgets

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.Nullable
import com.example.ilovezappos.R
import com.jaygoo.widget.OnRangeChangedListener
import kotlinx.android.synthetic.main.fragment_alert.*
import com.jaygoo.widget.RangeSeekBar
import com.firebase.jobdispatcher.Trigger
import com.firebase.jobdispatcher.Lifetime
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.example.ilovezappos.services.HourlyPriceCheckService
import java.util.concurrent.TimeUnit
import android.text.Spannable
import android.graphics.Typeface
import android.os.Handler
import android.text.style.StyleSpan
import android.text.SpannableString
import com.example.myloadingbutton.MyLoadingButton


class AlertBottomSheet : BottomSheetDialogFragment(), MyLoadingButton.MyLoadingButtonClick {
    private var seekbarValue: Float? = null
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_alert, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setSeekbarListener()
        setButtonListener()
    }

    private fun setButtonListener() {
        alertButton.setAnimationDuration(400)
        alertButton.setMyButtonClickListener(this)
    }

    private fun scheduleJob() {
        val sharedPreference =
            this.activity!!.getSharedPreferences("com.example.ilovezappos", Context.MODE_PRIVATE)
        val lastUpdatedTime = sharedPreference.getLong("lastUpdated", -1)
        val driver = GooglePlayDriver(this.context!!)
        val firebaseJobDispatcher = FirebaseJobDispatcher(driver)
        // Run Immediately as it's the first time
        if (lastUpdatedTime.equals(-1)) {
            val alertJobScheduler = firebaseJobDispatcher.newJobBuilder()
                .setService(HourlyPriceCheckService::class.java!!)
                .setTag("ALERT_JOB_SCHEDULE")
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .setRecurring(true)
                .setTrigger(
                    Trigger.executionWindow(
                        0,
                        0
                    )
                )
                .setReplaceCurrent(true)
                .build()
            firebaseJobDispatcher.mustSchedule(alertJobScheduler)
        }
        // If the last updated time was more than 1 hour, restart the job again.
        else if (lastUpdatedTime < (System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1))) {
            val alertJobScheduler = firebaseJobDispatcher.newJobBuilder()
                .setService(HourlyPriceCheckService::class.java!!)
                .setTag("ALERT_JOB_SCHEDULE")
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .setRecurring(true)
                .setTrigger(
                    Trigger.executionWindow(
                        0,
                        TimeUnit.HOURS.toSeconds(1).toInt()
                    )
                )
                .setReplaceCurrent(true)
                .build()
            firebaseJobDispatcher.mustSchedule(alertJobScheduler)
        }
    }

    private fun setSeekbarListener() {
        val sharedPreference =
            this.activity!!.getSharedPreferences("com.example.ilovezappos", Context.MODE_PRIVATE)
        val maxPrice = sharedPreference.getFloat("currentPrice", 8500.00f)
        val oldAlertPrice = sharedPreference.getFloat("alertPrice", -1f)
        alert_seekBar.setRange(0f, maxPrice)
        val initialProgress = (maxPrice * 0.90).toFloat()
        if (oldAlertPrice != -1f && oldAlertPrice <= maxPrice) {
            alert_seekBar.setProgress(oldAlertPrice)
            seekbarValue = oldAlertPrice
        } else {
            alert_seekBar.setProgress(initialProgress)
            seekbarValue = initialProgress
        }
        val boldText = "$ " + "%.2f".format(seekbarValue)
        val normalText = "We will notify you know if the bitcoin price falls below "
        val formattedString = SpannableString(normalText + boldText)
        formattedString.setSpan(
            StyleSpan(Typeface.BOLD),
            normalText.length,
            normalText.length + boldText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        alert_description.text = formattedString
        alert_seekBar.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onRangeChanged(
                view: RangeSeekBar,
                leftValue: Float,
                rightValue: Float,
                isFromUser: Boolean
            ) {
                seekbarValue = leftValue
                val boldText = "$ " + "%.2f".format(seekbarValue)
                val normalText = "We will notify you know if the bitcoin price falls below "
                val formattedString = SpannableString(normalText + boldText)
                formattedString.setSpan(
                    StyleSpan(Typeface.BOLD),
                    normalText.length,
                    normalText.length + boldText.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                alert_description.text = formattedString
            }

            override fun onStartTrackingTouch(view: RangeSeekBar, isLeft: Boolean) {
                //start tracking touch
            }

            override fun onStopTrackingTouch(view: RangeSeekBar, isLeft: Boolean) {
                //stop tracking touch
            }
        })
    }

    override fun onMyLoadingButtonClick() {
        val sharedPreference =
            this.activity!!.getSharedPreferences("com.example.ilovezappos", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putFloat("alertPrice", seekbarValue!!)
        editor.commit()
        scheduleJob()
        alertButton.showDoneButton();
        Handler().postDelayed({
            dismiss()
        }, 800)
    }
}