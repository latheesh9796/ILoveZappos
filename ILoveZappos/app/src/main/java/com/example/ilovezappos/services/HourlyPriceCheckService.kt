package com.example.ilovezappos.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.content.Context
import com.firebase.jobdispatcher.JobService
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews
import android.widget.Toast
import com.example.ilovezappos.R
import com.example.ilovezappos.activities.MainActivity
import com.example.ilovezappos.models.CurrentPrice
import com.example.ilovezappos.networking.BitStampRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HourlyPriceCheckService : JobService() {
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "com.example.ilovezappos.notification"
    private val description = "The price has gone below the limit"

    override fun onStopJob(job: com.firebase.jobdispatcher.JobParameters): Boolean {
        return true
    }

    override fun onStartJob(job: com.firebase.jobdispatcher.JobParameters): Boolean {
        getCurrentPrice()
        return true
    }

    fun getCurrentPrice() {
        val call = BitStampRepository().bitStampAPI!!.getPresentPrice()
        call.enqueue(object : Callback<CurrentPrice> {
            override fun onFailure(call: Call<CurrentPrice>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<CurrentPrice>?, response: Response<CurrentPrice>?) {
                response?.let {
                    val data = it.body()
                    val sharedPreference =
                        getSharedPreferences("com.example.ilovezappos", Context.MODE_PRIVATE)
                    val alertValue = sharedPreference.getFloat("alertPrice", -1f)
                    // If the current price of bitcoin is lesser than the value set for alert. Trigger a notification and update LastUpdated value.
                    if (data.last.toFloat() < alertValue) {
                        createNotification(alertValue)
                    }
                    var editor = sharedPreference.edit()
                    editor.putLong("lastUpdated", System.currentTimeMillis())
                    editor.commit()
                }
            }
        })
    }

    fun createNotification(alertValue: Float) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val contentView = RemoteViews(packageName, R.layout.notification_layout)
        contentView.setTextViewText(R.id.notification_title, "I Love Zappos")
        contentView.setTextViewText(
            R.id.notification_content,
            "Come back here. Bitcoin value is now lesser than $${alertValue}"
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_alert_foreground)
                .setContent(contentView)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        this.resources,
                        R.drawable.ic_notify_foreground
                    )
                )
                .setContentIntent(pendingIntent)
        } else {
            builder = Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_alert_foreground)
                .setContent(contentView)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        this.resources,
                        R.drawable.ic_notify_foreground
                    )
                )
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234, builder.build())
    }

}
