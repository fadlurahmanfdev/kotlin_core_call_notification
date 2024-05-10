package com.github.fadlurahmanfdev.core_call_notification.domain.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

abstract class CallNotificationReceiver : BroadcastReceiver() {
    companion object {
        const val ANSWER_CALL_NOTIFICATION = "co.id.fadlurahmanfdev.ANSWER_CALL_NOTIFICATION"
        const val DECLINE_CALL_NOTIFICATION = "co.id.fadlurahmanfdev.DECLINE_CALL_NOTIFICATION"
        const val HANG_UP_CALL_NOTIFICATION = "co.id.fadlurahmanfdev.HANG_UP_CALL_NOTIFICATION"
        const val PARAM_CALL_NOTIFICATION_ID = "PARAM_CALL_NOTIFICATION_ID"
        const val PARAM_CALLER_NAME =
            "PARAM_CALLER_NAME"
        const val PARAM_CALLER_NETWORK_IMAGE =
            "PARAM_CALLER_NETWORK_IMAGE"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        Log.d(CallNotificationReceiver::class.java.simpleName, "ACTION COMING -> ${intent?.action}")
        when (intent?.action) {
            ANSWER_CALL_NOTIFICATION -> {
                val callNotificationId = intent.getIntExtra(PARAM_CALL_NOTIFICATION_ID, -1)
                val callerName = intent.getStringExtra(PARAM_CALLER_NAME)
                val callerImage = intent.getStringExtra(PARAM_CALLER_NETWORK_IMAGE)
                if (callNotificationId != -1) {
                    onAcceptIncomingCall(
                        context,
                        callNotificationId = callNotificationId,
                        callerName = callerName ?: "-",
                        callerImage = callerImage
                    )
                }
            }

            DECLINE_CALL_NOTIFICATION -> {
                val callNotificationId = intent.getIntExtra(PARAM_CALL_NOTIFICATION_ID, -1)
                if (callNotificationId != -1) {
                    onDeclinedIncomingCall(context, callNotificationId)
                }
            }

            HANG_UP_CALL_NOTIFICATION -> {
                val callNotificationId = intent.getIntExtra(PARAM_CALL_NOTIFICATION_ID, -1)
                if (callNotificationId != -1) {
                    context.sendBroadcast(Intent(HANG_UP_CALL_NOTIFICATION))
                    onHangUpIncomingCall(context, callNotificationId)
                }
            }
        }
    }

    abstract fun onAcceptIncomingCall(
        context: Context,
        callNotificationId: Int,
        callerName: String,
        callerImage: String?
    )

    abstract fun onDeclinedIncomingCall(context: Context, callNotificationId: Int)

    abstract fun onHangUpIncomingCall(context: Context, callNotificationId: Int)
}