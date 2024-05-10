package com.github.fadlurahmanfdev.core_call_notification.domain.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log

abstract class CallNotificationPlayer : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var audioManager: AudioManager
    private lateinit var vibrator: Vibrator

    private var currentRingerMode: Int = AudioManager.RINGER_MODE_SILENT
    private var isRingerModeListenerRegistered: Boolean = false

    companion object {
        const val SHOW_INCOMING_CALL_NOTIFICATION =
            "co.id.fadlurahmanfdev.SHOW_INCOMING_CALL_NOTIFICATION"
        const val ANSWER_CALL_NOTIFICATION =
            "co.id.fadlurahmanfdev.ANSWER_CALL_NOTIFICATION"
        const val DECLINE_CALL_NOTIFICATION =
            "co.id.fadlurahmanfdev.DECLINE_CALL_NOTIFICATION"

        const val PARAM_CALL_NOTIFICATION_ID =
            "PARAM_CALL_NOTIFICATION_ID"
        const val PARAM_CALLER_NAME =
            "PARAM_CALLER_NAME"
        const val PARAM_CALLER_NETWORK_IMAGE =
            "PARAM_CALLER_NETWORK_IMAGE"
    }

    override fun onCreate() {
        super.onCreate()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(
            CallNotificationPlayer::class.java.simpleName,
            "ACTION COMING -> ${intent?.action}"
        )
        when (intent?.action) {
            SHOW_INCOMING_CALL_NOTIFICATION -> {
                val callNotificationId = intent.getIntExtra(PARAM_CALL_NOTIFICATION_ID, -1)
                val callerName = intent.getStringExtra(PARAM_CALLER_NAME)
                val callerImage = intent.getStringExtra(PARAM_CALLER_NETWORK_IMAGE)
                if (callNotificationId != -1) {
                    onStartForegroundIncomingCallNotification(
                        applicationContext,
                        callNotificationId = callNotificationId,
                        callerName = callerName ?: "-",
                        callerImage = callerImage
                    )
                    onIncomingNotificationPlayerPlaying()
                }
            }

            ANSWER_CALL_NOTIFICATION -> {
                onAnswerCallNotification()
            }

            DECLINE_CALL_NOTIFICATION -> {
                onDeclineCallNotification()
            }
        }
        return START_STICKY
    }

    abstract fun onGetIncomingCallNotification(
        context: Context,
        callNotificationId: Int,
        callerName: String,
        callerImage: String?,
        answerIntent: PendingIntent,
        declinedIntent: PendingIntent,
        onGetNotification: (Notification) -> Unit
    )

    open fun onStartForegroundIncomingCallNotification(
        context: Context,
        callNotificationId: Int,
        callerName: String,
        callerImage: String?
    ) {
        onGetIncomingCallNotification(
            applicationContext,
            callNotificationId = callNotificationId,
            callerName = callerName,
            callerImage = callerImage,
            answerIntent = onAnswerIntent(context, callNotificationId = callNotificationId),
            declinedIntent = onDeclinedIntent(context, callNotificationId = callNotificationId),
            onGetNotification = { notification ->
                startForeground(callNotificationId, notification)
            },
        )
    }

    abstract fun onAnswerIntent(context: Context, callNotificationId: Int): PendingIntent
    abstract fun onDeclinedIntent(context: Context, callNotificationId: Int): PendingIntent

    open fun onIncomingNotificationPlayerPlaying() {
        stopRinging()
        startRinging()
        listenRingerMode()
    }

    private fun onAnswerCallNotification() {
        stopForegroundService()
    }

    open fun onDeclineCallNotification() {
        stopForegroundService()
    }

    private fun startRinging() {
        when (audioManager.ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> {
                startRingingNormalMode()
            }

            AudioManager.RINGER_MODE_VIBRATE -> {
                startRingingVibrateMode()
            }

            AudioManager.RINGER_MODE_SILENT -> {
                startRingingSilentMode()
            }
        }
    }

    private val ringerModeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                AudioManager.RINGER_MODE_CHANGED_ACTION -> {
                    if (currentRingerMode == audioManager.ringerMode) {
                        return
                    }
                    when (audioManager.ringerMode) {
                        AudioManager.RINGER_MODE_NORMAL -> {
                            stopRinging()
                            startRingingNormalMode()
                            listenRingerMode()
                        }

                        AudioManager.RINGER_MODE_VIBRATE -> {
                            stopRinging()
                            startRingingVibrateMode()
                            listenRingerMode()
                        }

                        AudioManager.RINGER_MODE_SILENT -> {
                            stopRinging()
                            startRingingSilentMode()
                            listenRingerMode()
                        }
                    }
                }
            }
        }
    }

    private fun startRingingNormalMode() {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(applicationContext, soundUri)

        // set volume based on ringtone volume
        mediaPlayer?.setAudioAttributes(
            AudioAttributes.Builder()
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .build()
        )

        mediaPlayer?.prepare()
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()

        playVibrateEffect()
        currentRingerMode = AudioManager.RINGER_MODE_NORMAL
    }

    private fun startRingingVibrateMode() {
        playVibrateEffect()
        currentRingerMode = AudioManager.RINGER_MODE_VIBRATE
    }

    private fun startRingingSilentMode() {
        currentRingerMode = AudioManager.RINGER_MODE_SILENT
    }

    private fun playVibrateEffect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0L, 2000L, 2000L), 0))
        } else {
            vibrator.vibrate(longArrayOf(0L, 2000L, 2000L), 0)
        }
    }

    private fun listenRingerMode() {
        if (!isRingerModeListenerRegistered) {
            registerReceiver(
                ringerModeReceiver,
                IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION)
            )
        }
        isRingerModeListenerRegistered = true
    }

    private fun stopRinging() {
        stopListenRingerMode()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        cancelVibrator()
    }

    private fun stopListenRingerMode() {
        if (isRingerModeListenerRegistered) {
            unregisterReceiver(ringerModeReceiver)
        }
        isRingerModeListenerRegistered = false
    }

    private fun cancelVibrator() {
        vibrator.cancel()
    }

    override fun onDestroy() {
        stopRinging()
        super.onDestroy()
    }

    private fun stopForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            stopForeground(true)
        }
    }
}