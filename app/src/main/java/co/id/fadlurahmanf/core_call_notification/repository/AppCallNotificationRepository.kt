package co.id.fadlurahmanf.core_call_notification.repository

import android.app.Notification
import android.app.PendingIntent
import android.content.Context

interface AppCallNotificationRepository {
    fun askNotificationPermission(context: Context, onCompleteCheckPermission: (Boolean) -> Unit)
    fun createCustomVOIPChannel(context: Context)
    fun getBasicIncomingCallNotification(
        context: Context,
        id: Int,
        callerName: String,
        callerImage: String?,
        answerIntent: PendingIntent,
        declinedIntent: PendingIntent,
        onGetNotification: (Notification) -> Unit
    )
    fun getBasicOngoingCallNotification(
        context: Context,
        id: Int,
        callerName: String,
        callerImage: String?,
        hangUpIntent: PendingIntent,
        onGetNotification: (Notification) -> Unit
    )
}