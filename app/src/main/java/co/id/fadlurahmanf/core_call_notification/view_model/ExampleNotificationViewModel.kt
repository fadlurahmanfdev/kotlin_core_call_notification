package co.id.fadlurahmanf.core_call_notification.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import co.id.fadlurahmanf.core_call_notification.usecase.ExampleCallNotificationUseCase

class ExampleNotificationViewModel(
    private val exampleCallNotificationUseCase: ExampleCallNotificationUseCase
) : ViewModel() {

    fun askPermission(context: Context) {
        exampleCallNotificationUseCase.askPermissionNotification(
            context,
            onCompleteCheckPermission = { isGranted ->
                Log.d(
                    ExampleNotificationViewModel::class.java.simpleName,
                    "IS NOTIFICATION PERMISSION GRANTED: $isGranted"
                )
            }
        )
    }

    fun showBasicIncomingCallNotification(context: Context){
            exampleCallNotificationUseCase.showBasicIncomingCallNotification(context)
    }
}