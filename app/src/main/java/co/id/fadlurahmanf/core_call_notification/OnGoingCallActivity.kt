package co.id.fadlurahmanf.core_call_notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.fadlurahmanfdev.core_call_notification.presentation.BaseCallActivity

class OnGoingCallActivity : BaseCallActivity() {
    private val intentFilter = IntentFilter("co.id.fadlurahmanfdev.HANG_UP_CALL_NOTIFICATION")
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(
                OnGoingCallActivity::class.java.simpleName,
                "onReceive action -> ${intent?.action}"
            )
            finish()
        }
    }

    override fun onBaseCallActivityCreate(savedInstanceState: Bundle?) {
        registerReceiver(broadcastReceiver, intentFilter)
        Log.d(OnGoingCallActivity::class.java.simpleName, "register receiver success")
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }
}