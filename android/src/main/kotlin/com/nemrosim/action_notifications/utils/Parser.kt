package com.nemrosim.action_notifications.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import com.nemrosim.action_notifications.*
import io.flutter.plugin.common.MethodCall
import java.lang.Error

class Parser(private var call: MethodCall) {

    fun getLargeIconData(): Bitmap? {
        var bitmap: Bitmap? = null
        if (this.call.hasArgument(LARGE_ICON_BYTE_ARRAY_ARG)) {

            val bytesArrayOrNull = call.argument<ByteArray>(LARGE_ICON_BYTE_ARRAY_ARG)

            if (bytesArrayOrNull != null) {
                bitmap = BitmapFactory.decodeByteArray(bytesArrayOrNull, 0, bytesArrayOrNull.size)
            }

        }
        return bitmap
    }

    fun getChannelId(): String {

        val isChannelIdRequiredByTheSDKVersion = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        val isHasChannelId: Boolean = call.hasArgument(CHANNEL_ID_ARG_NAME)


        val notificationChannelId: String?
        if (isChannelIdRequiredByTheSDKVersion && isHasChannelId) {

            Log.i(LOG_TAG, "READING CHANNEL ID")

            notificationChannelId = call.argument<String>(CHANNEL_ID_ARG_NAME)

            Log.i(LOG_TAG, "DATA IS READ")


            return if (notificationChannelId != null) {
                notificationChannelId
            } else {
                val defaultId = "Notification default channel id";
                Log.w(LOG_TAG, "Channel id is set to default value: $defaultId")
                defaultId
            }

        } else {
            throw Error("You need to provide a channel id for SDK ${Build.VERSION.SDK_INT}");
        }
    }

    fun getSmallIcon(context: Context): Int? {

        val iconName = call.argument<String>(SMALL_ICON_NAME_ARG_NAME)
        val typeDef = call.argument<String>(SMALL_ICON_DEF_TYPE_ARG_NAME)

        if (iconName == null) {
            throw Error("'smallIconName' property is not set")
        }

        if (typeDef == null) {
            throw Error("'typeDef' property is not set")
        }


        val smallIconRes = context.resources.getIdentifier(
                iconName,
                typeDef,
                context.packageName
        )

        if (smallIconRes == 0) {
            throw Error("Small icon is not set. Double check input data. 'smallIconName' and 'typeDef' is equivalent to 'mipmap/ic_launcher' ")
        }

        return smallIconRes

    }

    fun getNotificationTitle(): String {

        val notificationTitle = call.argument<String>(NOTIFICATION_TITLE_ARG_NAME)

        if (notificationTitle == null) {
            return "Default title"
        } else {
            return notificationTitle
        }


    }

    fun getNotificationText(): String {

        val notificationText = call.argument<String>(NOTIFICATION_TEXT_ARG_NAME)

        if (notificationText == null) {
            return "Default notification text"
        } else {
            return notificationText
        }
    }

    fun getPriority(): Int {
        val priority = call.argument<Int>(PRIORITY_ARG_NAME)

        if (priority == null) {
            return 0
        } else {
            return priority
        }
    }


}