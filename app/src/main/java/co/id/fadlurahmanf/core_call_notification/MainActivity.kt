package co.id.fadlurahmanf.core_call_notification

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import co.id.fadlurahmanf.core_call_notification.usecase.ExampleCallNotificationUseCaseImpl
import co.id.fadlurahmanf.core_call_notification.adapter.ListExampleAdapter
import co.id.fadlurahmanf.core_call_notification.model.FeatureModel
import co.id.fadlurahmanf.core_call_notification.repository.AppCallNotificationRepositoryImpl
import com.github.fadlurahmanfdev.core_call_notification.data.repositories.CallNotificationRepositoryImpl
import co.id.fadlurahmanf.core_call_notification.view_model.ExampleNotificationViewModel

class MainActivity : AppCompatActivity(), ListExampleAdapter.Callback {
    lateinit var rv: RecyclerView
    lateinit var viewModel: ExampleNotificationViewModel

    private val features: List<FeatureModel> = listOf<FeatureModel>(
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "Ask Permission",
            desc = "Ask Permission Notification",
            enum = "ASK_PERMISSION"
        ),
        FeatureModel(
            featureIcon = R.drawable.baseline_developer_mode_24,
            title = "Basic Incoming Call Notification",
            desc = "Show basic incoming call notification",
            enum = "BASIC_INCOMING_CALL_NOTIFICATION"
        ),
    )

    private lateinit var adapter: ListExampleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rv = findViewById(R.id.rv)
        rv.setItemViewCacheSize(features.size)
        rv.setHasFixedSize(true)

        viewModel = ExampleNotificationViewModel(
            exampleCallNotificationUseCase = ExampleCallNotificationUseCaseImpl(
                appCallNotificationRepository = AppCallNotificationRepositoryImpl(
                    callNotificationRepository = CallNotificationRepositoryImpl()
                ),
            ),
        )

        adapter = ListExampleAdapter()
        adapter.setCallback(this)
        adapter.setList(features)
        adapter.setHasStableIds(true)
        rv.adapter = adapter
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            Log.d(MainActivity::class.java.simpleName, "IS GRANTED -> $isGranted")
        }

    override fun onClicked(item: FeatureModel) {
        when (item.enum) {
            "ASK_PERMISSION" -> {
                viewModel.askPermission(this)
            }

            "BASIC_INCOMING_CALL_NOTIFICATION" -> {
                viewModel.showBasicIncomingCallNotification(this)
            }
        }
    }
}