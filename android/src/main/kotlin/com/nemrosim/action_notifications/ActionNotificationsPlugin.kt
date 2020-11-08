package com.nemrosim.action_notifications

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import com.nemrosim.action_notifications.utils.Parser as Parser
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result



/** ActionNotificationsPlugin */
class ActionNotificationsPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var context: Context


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "action_notifications")
        channel.setMethodCallHandler(this)

    }

    private fun getResourceFromContext(context: Context, resName: String): String? {
        val stringRes = context.resources.getIdentifier(resName, "string", context.packageName)
        require(stringRes != 0) { String.format("The 'R.string.%s' value it's not defined in your project's resources file.", resName) }
        return context.getString(stringRes)
    }

    private fun checkProps(@NonNull call: MethodCall) {
        val isHasShowWhen: Boolean = call.hasArgument(SHOW_WHEN_ARG)
        val isHasShouldUseBigText: Boolean = call.hasArgument(SHOULD_USE_BIG_TEXT_ARG)

        if (!isHasShowWhen) {
            throw Error("$SHOW_WHEN_ARG prop is not set")
        }

        if (!isHasShouldUseBigText) {
            throw Error("$SHOULD_USE_BIG_TEXT_ARG prop is not set")
        }


    }


    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "showSimpleNotification") {

            checkProps(call)


            val showWhen = call.argument<Boolean>(SHOW_WHEN_ARG) as Boolean
            val shouldUseBigText = call.argument<Boolean>(SHOULD_USE_BIG_TEXT_ARG) as Boolean


            val parser = Parser(call);


            showSimpleNotification(
                    context,
                    channelId = parser.getChannelId(),
                    smallIcon = parser.getSmallIcon(context),
                    largeIcon = parser.getLargeIconData(),
                    notificationTitle = parser.getNotificationTitle(),
                    notificationText = parser.getNotificationText(),
                    showWhen = showWhen,
                    shouldUseBigText = shouldUseBigText,
                    priority = parser.getPriority()
            )


        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
