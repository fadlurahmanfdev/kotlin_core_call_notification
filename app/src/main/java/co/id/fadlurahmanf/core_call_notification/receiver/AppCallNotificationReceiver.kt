package co.id.fadlurahmanf.core_call_notification.receiver

import android.content.Context
import android.content.Intent
import co.id.fadlurahmanf.core_call_notification.OnGoingCallActivity
import co.id.fadlurahmanf.core_call_notification.service.AppCallNotificationPlayer
import com.github.fadlurahmanfdev.core_call_notification.domain.plugins.CallNotification
import com.github.fadlurahmanfdev.core_call_notification.domain.receiver.CallNotificationReceiver

class AppCallNotificationReceiver : CallNotificationReceiver() {
    override fun onAcceptIncomingCall(
        context: Context,
        callNotificationId: Int,
        callerName: String,
        callerImage: String?
    ) {
        val intent = Intent(context, OnGoingCallActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)

        CallNotification.acceptIncomingCall(
            context,
            callNotificationId = callNotificationId,
            callerName = callerName,
            callerImage = callerImage,
            clazz = AppCallNotificationPlayer::class.java
        )
    }

    override fun onDeclinedIncomingCall(context: Context, callNotificationId: Int) {
        CallNotification.declineIncomingCall(
            context,
            callNotificationId = callNotificationId,
            clazz = AppCallNotificationPlayer::class.java
        )
    }

    override fun onHangUpIncomingCall(context: Context, callNotificationId: Int) {
        CallNotification.hangUpIncomingCall(
            context,
            callNotificationId = callNotificationId,
            clazz = AppCallNotificationPlayer::class.java,
        )
    }
}