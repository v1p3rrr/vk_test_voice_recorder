package com.vpr.vk_test_voice_recorder.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import com.vpr.vk_test_voice_recorder.presentation.screens.voice_recorder.VoiceRecorderScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            permissions,
            0
        )

        //val viewModel: VoiceRecordViewModel by viewModels()

        setContent {
            VoiceRecorderScreen()
        }
    }

}