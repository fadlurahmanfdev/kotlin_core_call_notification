package com.github.fadlurahmanfdev.core_call_notification.data.repositories

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
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
        callerName: String,
        callerImage: Bitmap?,
        answerIntent: PendingIntent,
        declinedIntent: PendingIntent,
    ): Notification {
        createNotificationChannel(
            context,
            channelId = VOIP_CHANNEL_ID,
            channelName = VOIP_CHANNEL_NAME,
            channelDescription = VOIP_CHANNEL_DESCRIPTION,
            sound = null
        )

        val personBuilder = Person.Builder().apply {
            if (callerImage != null) {
                setIcon(IconCompat.createWithBitmap(callerImage))
            }
            setName(callerName)
        }
        val style = NotificationCompat.CallStyle.forIncomingCall(
            personBuilder.build(),
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

    override fun getBasicOngoingCallNotification(
        context: Context,
        id: Int,
        smallIcon: Int,
        callerName: String,
        callerImage: Bitmap?,
        hangUpIntent: PendingIntent
    ): Notification {
        createNotificationChannel(
            context,
            channelId = VOIP_CHANNEL_ID,
            channelName = VOIP_CHANNEL_NAME,
            channelDescription = VOIP_CHANNEL_DESCRIPTION,
            sound = null
        )

        val personBuilder = Person.Builder().apply {
            if (callerImage != null) {
                setIcon(IconCompat.createWithBitmap(callerImage))
            }
            setName(callerName)
        }
        val style = NotificationCompat.CallStyle.forOngoingCall(
            personBuilder.build(),
            hangUpIntent,
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

    override fun cancelIncomingCallNotification(context: Context, id: Int) {
        getNotificationManager(context).cancel(id)
    }
}