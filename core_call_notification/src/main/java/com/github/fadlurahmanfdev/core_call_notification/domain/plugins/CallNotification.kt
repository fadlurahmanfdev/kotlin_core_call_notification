package com.github.fadlurahmanfdev.core_call_notification.domain.plugins

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import com.github.fadlurahmanfdev.core_call_notification.domain.receiver.CallNotificationReceiver
import com.github.fadlurahmanfdev.core_call_notification.domain.service.CallNotificationPlayer

class CallNotification {
    companion object {
        private fun getFlagPendingIntent(): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        }

        fun <T : CallNotificationPlayer> showIncomingCallNotification(
            context: Context,
            callNotificationId: Int,
            callerName: String,
            callerNetworkImage: String?,
            clazz: Class<T>
        ) {
            val intent = Intent(context, clazz)
            intent.apply {
                action = CallNotificationPlayer.SHOW_INCOMING_CALL_NOTIFICATION
                putExtra(CallNotificationPlayer.PARAM_CALL_NOTIFICATION_ID, callNotificationId)
                putExtra(CallNotificationPlayer.PARAM_CALLER_NAME, callerName)
                putExtra(CallNotificationPlayer.PARAM_CALLER_NETWORK_IMAGE, callerNetworkImage)
            }
            return ContextCompat.startForegroundService(context, intent)
        }

        fun <T : CallNotificationPlayer> showOngoingCallNotification(
            context: Context,
            callNotificationId: Int,
            callerName: String,
            callerNetworkImage: String?,
            clazz: Class<T>
        ) {
            val intent = Intent(context, clazz)
            intent.apply {
                action = CallNotificationPlayer.SHOW_ONGOING_CALL_NOTIFICATION
                putExtra(CallNotificationPlayer.PARAM_CALL_NOTIFICATION_ID, callNotificationId)
                putExtra(CallNotificationPlayer.PARAM_CALLER_NAME, callerName)
                putExtra(CallNotificationPlayer.PARAM_CALLER_NETWORK_IMAGE, callerNetworkImage)
            }
            return ContextCompat.startForegroundService(context, intent)
        }

        fun <T : CallNotificationReceiver> getAnswerCallPendingIntent(
            context: Context,
            callNotificationId: Int,
            callerName: String,
            callerNetworkImage: String?,
            clazz: Class<T>
        ): PendingIntent {
            val intent = Intent(context, clazz)
            intent.apply {
                action = CallNotificationReceiver.ANSWER_CALL_NOTIFICATION
                putExtra(CallNotificationReceiver.PARAM_CALL_NOTIFICATION_ID, callNotificationId)
                putExtra(CallNotificationPlayer.PARAM_CALLER_NAME, callerName)
                putExtra(CallNotificationPlayer.PARAM_CALLER_NETWORK_IMAGE, callerNetworkImage)
            }
            return PendingIntent.getBroadcast(context, 0, intent, getFlagPendingIntent())
        }

        fun <T : CallNotificationReceiver> getDeclineIncomingCallPendingIntent(
            context: Context,
            callNotificationId: Int,
            clazz: Class<T>
        ): PendingIntent {
            val intent = Intent(context, clazz)
            intent.apply {
                action = CallNotificationReceiver.DECLINE_CALL_NOTIFICATION
                putExtra(CallNotificationReceiver.PARAM_CALL_NOTIFICATION_ID, callNotificationId)
            }
            return PendingIntent.getBroadcast(context, 0, intent, getFlagPendingIntent())
        }

        fun <T : CallNotificationReceiver> sendHangUpOngoingCallBroadcast(
            context: Context,
            callNotificationId: Int,
            clazz: Class<T>
        ) {
            val intent = Intent(context, clazz)
            intent.apply {
                action = CallNotificationReceiver.HANG_UP_CALL_NOTIFICATION
                putExtra(CallNotificationReceiver.PARAM_CALL_NOTIFICATION_ID, callNotificationId)
            }
            context.sendBroadcast(intent)
        }

        fun <T : CallNotificationReceiver> getHangUpOngoingCallPendingIntent(
            context: Context,
            callNotificationId: Int,
            clazz: Class<T>
        ): PendingIntent {
            val intent = Intent(context, clazz)
            intent.apply {
                action = CallNotificationReceiver.HANG_UP_CALL_NOTIFICATION
                putExtra(CallNotificationReceiver.PARAM_CALL_NOTIFICATION_ID, callNotificationId)
            }
            return PendingIntent.getBroadcast(context, 0, intent, getFlagPendingIntent())
        }

        fun <T : CallNotificationPlayer> acceptIncomingCall(
            context: Context,
            callNotificationId: Int,
            callerName: String,
            callerImage: String?,
            clazz: Class<T>,
        ) {
            val intent = Intent(context, clazz)
            intent.apply {
                action = CallNotificationPlayer.ANSWER_CALL_NOTIFICATION
                putExtra(CallNotificationReceiver.PARAM_CALL_NOTIFICATION_ID, callNotificationId)
                putExtra(CallNotificationPlayer.PARAM_CALLER_NAME, callerName)
                putExtra(CallNotificationPlayer.PARAM_CALLER_NETWORK_IMAGE, callerImage)
            }
            ContextCompat.startForegroundService(context, intent)
        }

        fun <T : CallNotificationPlayer> declineIncomingCall(
            context: Context,
            callNotificationId: Int,
            clazz: Class<T>
        ) {
            val intent = Intent(context, clazz)
            intent.apply {
                action = CallNotificationPlayer.DECLINE_CALL_NOTIFICATION
                putExtra(
                    CallNotificationPlayer.PARAM_CALL_NOTIFICATION_ID,
                    callNotificationId
                )
            }
            context.stopService(intent)
        }

        fun <T : CallNotificationPlayer> hangUpIncomingCall(
            context: Context,
            callNotificationId: Int,
            clazz: Class<T>
        ) {
            val intent = Intent(context, clazz)
            intent.apply {
                action = CallNotificationPlayer.HANG_UP_CALL_NOTIFICATION
                putExtra(
                    CallNotificationPlayer.PARAM_CALL_NOTIFICATION_ID,
                    callNotificationId
                )
            }
            context.stopService(intent)
        }
    }
}