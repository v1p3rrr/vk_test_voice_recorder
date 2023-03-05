package com.vpr.vk_test_voice_recorder.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.vpr.vk_test_voice_recorder.data.recorder.AudioPlayerImpl
import com.vpr.vk_test_voice_recorder.data.recorder.AudioRecorderImpl
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val recorder by lazy {
        AudioRecorderImpl(applicationContext)
    }
    private val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    private val player by lazy {
        AudioPlayerImpl(applicationContext)
    }

    var audioFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            permissions,
            0
        )

        val viewModel: VoiceRecordViewModel by viewModels()

        setContent {
            VoiceRecorderScreen()
        }
    }

}