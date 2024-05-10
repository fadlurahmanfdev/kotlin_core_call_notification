package com.github.fadlurahmanfdev.core_call_notification.domain.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

abstract class CallNotificationReceiver : BroadcastReceiver() {
    companion object {
        const val ANSWER_CALL_NOTIFICATION = "co.id.fadlurahmanfdev.ANSWER_CALL_NOTIFICATION"
        const val DECLINE_CALL_NOTIFICATION = "co.id.fadlurahmanfdev.DECLINE_CALL_NOTIFICATION"
        const val PARAM_CALL_NOTIFICATION_ID = "PARAM_CALL_NOTIFICATION_ID"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        Log.d(CallNotificationReceiver::class.java.simpleName, "ACTION COMING -> ${intent?.action}")
        when (intent?.action) {
            ANSWER_CALL_NOTIFICATION -> {
                val callNotificationId = intent.getIntExtra(PARAM_CALL_NOTIFICATION_ID, -1)
                if (callNotificationId != -1) {
                    onAcceptIncomingCall(context, callNotificationId)
                }
            }

            DECLINE_CALL_NOTIFICATION -> {
                val callNotificationId = intent.getIntExtra(PARAM_CALL_NOTIFICATION_ID, -1)
                if (callNotificationId != -1) {
                    onDeclinedIncomingCall(context, callNotificationId)
                }
            }
        }
    }

    abstract fun onAcceptIncomingCall(context: Context, callNotificationId: Int)

    abstract fun onDeclinedIncomingCall(context: Context, callNotificationId: Int)
}