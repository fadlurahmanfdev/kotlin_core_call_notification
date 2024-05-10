package co.id.fadlurahmanf.core_call_notification.service


import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import co.id.fadlurahmanf.core_call_notification.receiver.AppCallNotificationReceiver
import co.id.fadlurahmanf.core_call_notification.repository.AppCallNotificationRepository
import co.id.fadlurahmanf.core_call_notification.repository.AppCallNotificationRepositoryImpl
import com.github.fadlurahmanfdev.core_call_notification.data.repositories.CallNotificationRepository
import com.github.fadlurahmanfdev.core_call_notification.data.repositories.CallNotificationRepositoryImpl
import com.github.fadlurahmanfdev.core_call_notification.domain.plugins.CallNotification
import com.github.fadlurahmanfdev.core_call_notification.domain.service.CallNotificationPlayer
import com.github.fadlurahmanfdev.kotlin_core_notification.data.repositories.NotificationRepository
import com.github.fadlurahmanfdev.kotlin_core_notification.data.repositories.NotificationRepositoryImpl

class AppCallNotificationPlayer : CallNotificationPlayer() {
    private lateinit var notificationRepository: NotificationRepository
    private lateinit var callNotificationRepository: CallNotificationRepository
    private lateinit var appCallNotificationRepository: AppCallNotificationRepository
    override fun onCreate() {
        super.onCreate()
        notificationRepository = NotificationRepositoryImpl()
        callNotificationRepository = CallNotificationRepositoryImpl()
        appCallNotificationRepository = AppCallNotificationRepositoryImpl(
            callNotificationRepository = callNotificationRepository
        )
    }

    override fun onGetIncomingCallNotification(
        context: Context,
        callNotificationId: Int,
        callerName: String,
        callerImage: String?,
        answerIntent: PendingIntent,
        declinedIntent: PendingIntent,
        onGetNotification: (Notification) -> Unit
    ) {
        return appCallNotificationRepository.getBasicIncomingCallNotification(
            context,
            id = callNotificationId,
            callerName = callerName,
            callerImage = callerImage,
            answerIntent = answerIntent,
            declinedIntent = declinedIntent,
            onGetNotification = onGetNotification
        )
    }

    override fun onAnswerIntent(context: Context, callNotificationId: Int): PendingIntent {
        return CallNotification.getAnswerCallPendingIntent(
            context,
            callNotificationId = callNotificationId,
            clazz = AppCallNotificationReceiver::class.java
        )
    }

    override fun onDeclinedIntent(context: Context, callNotificationId: Int): PendingIntent {
        return CallNotification.getDeclineIncomingCallPendingIntent(
            context,
            callNotificationId = callNotificationId,
            clazz = AppCallNotificationReceiver::class.java
        )
    }
}