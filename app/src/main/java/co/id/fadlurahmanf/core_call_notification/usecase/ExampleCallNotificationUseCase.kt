package co.id.fadlurahmanf.core_call_notification.usecase

import android.content.Context

interface ExampleCallNotificationUseCase {
    fun askPermissionNotification(context: Context, onCompleteCheckPermission: (Boolean) -> Unit)
    fun showBasicIncomingCallNotification(context: Context)
}