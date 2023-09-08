package com.example.notificationdemo

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput

class MainActivity : AppCompatActivity() {
    private val channelID = "com.example.notificationdemo.channel1"
    private var notificationManager: NotificationManager? = null
    private val KEY_REPLY = "key_reply"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(channelID, "DemoChannel", "this is a demo")
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            displayNotification()
        }
    }

    private fun displayNotification(){
        val notificationId = 45
        val tapResultIntent = Intent(this, SecondActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            tapResultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        //action reply
        val remoteInput: RemoteInput = RemoteInput.Builder(KEY_REPLY).run {
            setLabel("Insert your name here")
            build()
        }

        val replyAction : NotificationCompat.Action = NotificationCompat.Action.Builder(
            0,
            "Reply",
            pendingIntent,
        ).addRemoteInput(remoteInput)
            .build()

        //action button 1
        val intent2 = Intent(this, DetailsActivity::class.java)
        val pendingIntent2: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent2,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val action2 : NotificationCompat.Action =
            NotificationCompat.Action.Builder(0, "Details", pendingIntent2).build()

        //action button 2
        val intent3 = Intent(this, SettingActivity::class.java)
        val pendingIntent3: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent3,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val action3 : NotificationCompat.Action =
            NotificationCompat.Action.Builder(0, "Settings", pendingIntent3).build()

        val notification = NotificationCompat.Builder(this@MainActivity, channelID)
            .setContentTitle("Demo Title")
            .setContentText("This is a demo notification")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(action2)
            .addAction(action3)
            .addAction(replyAction)
            .build()
        notificationManager?.notify(notificationId, notification)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel(id: String, name: String, channelDescription: String){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply {
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }
}