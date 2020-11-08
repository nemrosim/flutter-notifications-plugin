package com.nemrosim.action_notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

fun getMainActivityClass(context: Context): Class<*>? {
    val packageName = context.packageName
    val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
    val className = launchIntent!!.component!!.className

    Log.i(LOG_TAG, "CLASS_NAME $className")
    return try {
        Class.forName(className)
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
        null
    }
}


fun showSimpleNotification(
        context: Context,
        channelId: String,
        smallIcon: Int?,
        largeIcon: Bitmap? = null,
        notificationTitle: String,
        notificationText: String,
        showWhen: Boolean?,
        shouldUseBigText: Boolean,
        priority: Int? = NotificationCompat.PRIORITY_DEFAULT) {
    val service: String = Context.NOTIFICATION_SERVICE

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Log.i(LOG_TAG, "Build SDK is ${Build.VERSION_CODES.O}");
        val name = "NAME"
        val descriptionText = "DESCRIPTION"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(channelId, name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = context.getSystemService(service) as NotificationManager
        notificationManager.createNotificationChannel(mChannel);

    } else {
        throw Error("NOT IMPLEMENTED")

    }

    val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(notificationText)


    val intent = Intent(context, getMainActivityClass(context))
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


    with(NotificationManagerCompat.from(context)) {

        val builder = NotificationCompat.Builder(context, channelId)
                .setLargeIcon(largeIcon)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(if (shouldUseBigText) bigTextStyle else null)
                // Set the intent that will fire when the user taps the notification
//                .setContentIntent(pendingIntent)
                .setContentIntent(pendingIntent)
                // Automatically close notification when user taps it
                .setAutoCancel(true)

        if (priority != null) {
            builder.setPriority(priority);

        }

        if (smallIcon != null) {
            builder.setSmallIcon(smallIcon);
        }

        if (showWhen != null) {
            builder.setShowWhen(showWhen)
        }


        notify(1, builder.build())
    }
}

class AlertDetails {

}
