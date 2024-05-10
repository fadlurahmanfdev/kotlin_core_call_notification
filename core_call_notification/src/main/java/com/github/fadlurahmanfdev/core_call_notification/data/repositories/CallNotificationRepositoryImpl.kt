package com.github.fadlurahmanfdev.core_call_notification.data.repositories

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import com.github.fadlurahmanfdev.kotlin_core_notification.others.BaseNotificationService

class CallNotificationRepositoryImpl : BaseNotificationService(),
    CallNotificationRepository {

    companion object {
        const val VOIP_CHANNEL_ID = "VOIP"
        const val VOIP_CHANNEL_NAME = "Panggilan"
        const val VOIP_CHANNEL_DESCRIPTION = "Notifikasi Panggilan"
    }

    override fun askNotificationPermission(
        context: Context,
        onCompleteCheckPermission: (isGranted: Boolean) -> Unit
    ) {
        return baseAskNotificationPermission(
            context,
            onCompleteCheckPermission = onCompleteCheckPermission
        )
    }

    override fun isNotificationPermissionGranted(context: Context): Boolean {
        return baseIsNotificationPermissionGranted(context)
    }

    override fun createNotificationChannel(
        context: Context,
        channelId: String,
        channelName: String,
        channelDescription: String,
        sound: Uri?,
    ) {
        return baseCreateNotificationChannel(
            context,
            channelId = channelId,
            channelName = channelName,
            channelDescription = channelDescription,
            sound = sound
        )
    }

    override fun getBasicIncomingCallNotification(
        context: Context,
        id: Int,
        @DrawableRes smallIcon: Int,
        answerIntent: PendingIntent,
        declinedIntent: PendingIntent,
        callerName: String,
    ): Notification {
        createNotificationChannel(
            context,
            channelId = VOIP_CHANNEL_ID,
            channelName = VOIP_CHANNEL_NAME,
            channelDescription = VOIP_CHANNEL_DESCRIPTION,
            sound = null
        )

        val style = NotificationCompat.CallStyle.forIncomingCall(
            Person.Builder().setName(callerName).build(),
            declinedIntent,
            answerIntent
        )
        val notificationBuilder = notificationBuilder(
            context,
            channelId = VOIP_CHANNEL_ID,
            smallIcon = smallIcon,
        ).apply {
            setStyle(style)
        }
        return notificationBuilder.build()
    }

//    fun <T : CallNotificationPlayer> showIncomingCallNotification(
//        context: Context,
//        clazz: Class<CallNotificationPlayer>,
//        id: Int
//    ) {
//        val intent = Intent(context, clazz)
//        intent.apply {
//            action = CallNotificationPlayer.SHOW_INCOMING_CALL_NOTIFICATION
//            putExtra(CallNotificationPlayer.PARAM_CALL_NOTIFICATION_ID, id)
//            putExtra(CallNotificationPlayer.PARAM_CALLER_NAME, "callerName")
//            putExtra(CallNotificationPlayer.PARAM_CALLER_NETWORK_IMAGE, "callerNetworkImage")
//        }
//        return ContextCompat.startForegroundService(
//            context,
//            intent
//        )
//    }

    override fun cancelIncomingCallNotification(context: Context, id: Int) {
        getNotificationManager(context).cancel(id)
//        CallNotificationPlayerService.stopIncomingCallNotificationPlayer(context)
    }
}