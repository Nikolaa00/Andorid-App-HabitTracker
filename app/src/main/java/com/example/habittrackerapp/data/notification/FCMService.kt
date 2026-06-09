package com.example.habittrackerapp.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.habittrackerapp.MainActivity
import com.example.habittrackerapp.R
import com.example.habittrackerapp.data.remote.FirestoreConstants
import com.example.habittrackerapp.util.NotificationConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var firestore: FirebaseFirestore

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCMService", "New token generated: $token")
        val userId = auth.currentUser?.uid
        if (userId != null) {
            saveTokenToFirestore(userId, token)
        }
    }

    private fun saveTokenToFirestore(userId: String, token: String) {
        firestore.collection(FirestoreConstants.USERS_COLLECTION)
            .document(userId)
            .update("fcmToken", token)
            .addOnSuccessListener {
                Log.d("FCMService", "FCM token saved successfully for user: $userId")
            }
            .addOnFailureListener { e ->
                Log.e("FCMService", "Error updating FCM token, attempting merge", e)
                val data = mapOf("fcmToken" to token)
                firestore.collection(FirestoreConstants.USERS_COLLECTION)
                    .document(userId)
                    .set(data, SetOptions.merge())
            }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FCMService", "Message received from: ${message.from}")

        message.notification?.let {
            showNotification(it.title ?: getString(R.string.app_name), it.body ?: "", message)
        } ?: run {
            // Handle data payload
            val title = message.data["title"] ?: getString(R.string.app_name)
            val body = message.data["body"] ?: ""
            if (body.isNotEmpty()) {
                showNotification(title, body, message)
            }
        }
    }

    private fun showNotification(title: String, message: String, remoteMessage: RemoteMessage? = null) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        val channel = NotificationChannel(
            NotificationConstants.HABIT_REMINDER_CHANNEL_ID,
            getString(R.string.channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = getString(R.string.channel_description)
        }
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Додавање на податоците од пораката во Intent-от за Firebase Analytics да го регистрира кликот
            remoteMessage?.data?.forEach { (key, value) ->
                putExtra(key, value)
            }
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, NotificationConstants.HABIT_REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
