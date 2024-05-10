package co.id.fadlurahmanf.core_call_notification.usecase

import android.content.Context
import co.id.fadlurahmanf.core_call_notification.repository.AppCallNotificationRepository
import co.id.fadlurahmanf.core_call_notification.service.AppCallNotificationPlayer
import com.github.fadlurahmanfdev.core_call_notification.domain.plugins.CallNotification

class ExampleCallNotificationUseCaseImpl(
    private val appCallNotificationRepository: AppCallNotificationRepository
) : ExampleCallNotificationUseCase {

    override fun askPermissionNotification(
        context: Context,
        onCompleteCheckPermission: (Boolean) -> Unit
    ) {
        return appCallNotificationRepository.askNotificationPermission(
            context,
            onCompleteCheckPermission = onCompleteCheckPermission
        )
    }

    override fun showBasicIncomingCallNotification(context: Context) {
        return CallNotification.showIncomingCallNotification(
            context,
            callNotificationId = 1,
            callerName = "Caller Name",
            callerNetworkImage = null,
            clazz = AppCallNotificationPlayer::class.java
        )
    }
}
