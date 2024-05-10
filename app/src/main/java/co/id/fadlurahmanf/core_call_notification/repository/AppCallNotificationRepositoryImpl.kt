package co.id.fadlurahmanf.core_call_notification.repository

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.util.Log
import androidx.annotation.DrawableRes
import co.id.fadlurahmanf.core_call_notification.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.fadlurahmanfdev.core_call_notification.data.repositories.CallNotificationRepository
import com.github.fadlurahmanfdev.core_call_notification.data.repositories.CallNotificationRepositoryImpl

class AppCallNotificationRepositoryImpl(
    private val callNotificationRepository: CallNotificationRepository,
) : AppCallNotificationRepository {
    companion object {
        @DrawableRes
        val BANK_MAS_LOGO_ICON = R.drawable.il_logo_bankmas
        const val CUSTOM_VOIP_CHANNEL_ID = "CUSTOM_VOIP"
        const val CUSTOM_VOIP_CHANNEL_NAME = "CUSTOM Panggilan"
        const val CUSTOM_VOIP_CHANNEL_DESCRIPTION =
            "CUSTOM Notifikasi Panggilan"
    }

    override fun askNotificationPermission(
        context: Context,
        onCompleteCheckPermission: (Boolean) -> Unit
    ) {
        return callNotificationRepository.askNotificationPermission(
            context,
            onCompleteCheckPermission = onCompleteCheckPermission,
        )
    }

    override fun createCustomVOIPChannel(context: Context) {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        callNotificationRepository.createNotificationChannel(
            context,
            channelId = CUSTOM_VOIP_CHANNEL_ID,
            channelName = CUSTOM_VOIP_CHANNEL_NAME,
            channelDescription = CUSTOM_VOIP_CHANNEL_DESCRIPTION,
            sound = sound
        )
    }

    private fun getImageBitmap(
        context: Context,
        imageUrl: String,
        onGetImageBitmap: (Bitmap?) -> Unit
    ) {
        Glide.with(context).asBitmap().load(imageUrl)
            .into(
                object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        Log.d(
                            AppCallNotificationRepositoryImpl::class.java.simpleName,
                            "on resource bitmap caller image ready"
                        )
                        onGetImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        Log.d(
                            CallNotificationRepositoryImpl::class.java.simpleName,
                            "on resource bitmap caller image failed"
                        )
                        onGetImageBitmap(null)
                    }
                },
            )
    }

    override fun getBasicIncomingCallNotification(
        context: Context,
        id: Int,
        callerName: String,
        callerImage: String?,
        answerIntent: PendingIntent,
        declinedIntent: PendingIntent,
        onGetNotification: (Notification) -> Unit
    ) {
        if (callerImage != null) {
            getImageBitmap(context, imageUrl = callerImage, onGetImageBitmap = { bitmap ->
                onGetNotification(
                    callNotificationRepository.getBasicIncomingCallNotification(
                        context,
                        id = id,
                        smallIcon = BANK_MAS_LOGO_ICON,
                        callerName = callerName,
                        callerImage = bitmap,
                        answerIntent = answerIntent,
                        declinedIntent = declinedIntent
                    )
                )
            })
        } else {
            onGetNotification(
                callNotificationRepository.getBasicIncomingCallNotification(
                    context,
                    id = id,
                    smallIcon = BANK_MAS_LOGO_ICON,
                    callerName = callerName,
                    callerImage = null,
                    answerIntent = answerIntent,
                    declinedIntent = declinedIntent
                )
            )
        }
    }

    override fun getBasicOngoingCallNotification(
        context: Context,
        id: Int,
        callerName: String,
        callerImage: String?,
        hangUpIntent: PendingIntent,
        onGetNotification: (Notification) -> Unit
    ) {
        if (callerImage != null) {
            getImageBitmap(context, imageUrl = callerImage, onGetImageBitmap = { bitmap ->
                onGetNotification(
                    callNotificationRepository.getBasicOngoingCallNotification(
                        context,
                        id = id,
                        smallIcon = BANK_MAS_LOGO_ICON,
                        callerName = callerName,
                        callerImage = bitmap,
                        hangUpIntent = hangUpIntent
                    )
                )
            })
        } else {
            onGetNotification(
                callNotificationRepository.getBasicOngoingCallNotification(
                    context,
                    id = id,
                    smallIcon = BANK_MAS_LOGO_ICON,
                    callerName = callerName,
                    callerImage = null,
                    hangUpIntent = hangUpIntent
                )
            )
        }
    }
}