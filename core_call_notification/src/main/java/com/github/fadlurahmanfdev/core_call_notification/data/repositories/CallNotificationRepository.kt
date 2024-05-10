package com.github.fadlurahmanfdev.core_call_notification.data.repositories

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.DrawableRes

interface CallNotificationRepository {
    /**
     * Determine whether you have been granted a particular permission.
     * @return isGranted: return true if permission is [PackageManager.PERMISSION_GRANTED] and return false if
     * permission is [PackageManager.PERMISSION_DENIED].
     */
    fun askNotificationPermission(
        context: Context,
        onCompleteCheckPermission: (isGranted: Boolean) -> Unit
    )

    /**
     * Determine whether you have been granted a notification permission.
     * @return return true if permission is [PackageManager.PERMISSION_GRANTED] and return false if
     * permission is [PackageManager.PERMISSION_DENIED].
     */
    fun isNotificationPermissionGranted(context: Context): Boolean

    fun createNotificationChannel(
        context: Context,
        channelId: String,
        channelName: String,
        channelDescription: String,
        sound: Uri?,
    )

    fun getBasicIncomingCallNotification(
        context: Context,
        id: Int,
        @DrawableRes smallIcon: Int,
        callerName: String,
        callerImage: Bitmap?,
        answerIntent: PendingIntent,
        declinedIntent: PendingIntent,
    ): Notification

    fun getBasicOngoingCallNotification(
        context: Context,
        id: Int,
        @DrawableRes smallIcon: Int,
        callerName: String,
        callerImage: Bitmap?,
        hangUpIntent: PendingIntent,
    ): Notification

    fun cancelIncomingCallNotification(context: Context, id: Int)
}